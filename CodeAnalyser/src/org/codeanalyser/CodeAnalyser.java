package org.codeanalyser;

import java.util.ArrayList;

import org.codeanalyser.commandanalyser.CommandAnalyser;
import org.codeanalyser.commandanalyser.NonCommandAnalyser;
import org.codeanalyser.methodanalyser.MethodAnalyser;
import org.codeanalyser.model.Command;

/**
 * Main class for code analysis. Adds special command to every command in source code
 * 
 * Typical usage:
 * 		CodeAnalyser analyser = new CodeAnalyser(string, "ANALYSER");
 *		analyser.analyse();
 *		String editedCode = analyser.editCode();
 * 
 * @author Jan Kovar
 *
 */
public class CodeAnalyser {
	private final MethodAnalyser methodAnalyser;
	private final CommandAnalyser commandAnalyser;
	private final NonCommandAnalyser nonCommandAnalyser;
	
	private final String analyseClass;
	private boolean analysed;
	private final String originalSourceCode;
	
	public CodeAnalyser(String sourceCode, String analyseClass) {
		this.methodAnalyser = new MethodAnalyser(sourceCode);
		this.commandAnalyser = new CommandAnalyser(sourceCode);
		this.nonCommandAnalyser = new NonCommandAnalyser(sourceCode);
		this.analyseClass = analyseClass;
		this.analysed = false;
		this.originalSourceCode = sourceCode;
	}
	
	public void analyse() {
		// Compute method indices
		methodAnalyser.computeIndices();
		// Find '{', '}' or ';' in strings 
		nonCommandAnalyser.computeIndices();
		// Set method and noncommand indices
		commandAnalyser.setItems(methodAnalyser.getItems());
		commandAnalyser.setNonCommandItems(nonCommandAnalyser.getItems());
		// Compute indices
		commandAnalyser.computeIndices();
		
		analysed = true;
	}
	
	public MethodAnalyser getMethodAnalyser() {
		return this.methodAnalyser;
	}
	
	public CommandAnalyser getCommandAnalyser() {
		return this.commandAnalyser;
	}
	
	public String editCode() throws Exception {
		if (!this.analysed) {
			throw new Exception("Not analysed");
		}
		
		if (commandAnalyser.getItems().isEmpty()) {
			return originalSourceCode;
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<Command> commands = (ArrayList<Command>) commandAnalyser.getItems();
		int commandSize = commands.size() - 1;
		StringBuilder sb = new StringBuilder(originalSourceCode);
		
		for (int i = commandSize; i >= 0; i--) {
			Command command = null;
			
			try {
				command = commands.get(i);
			}
			catch (IndexOutOfBoundsException e) {}
			
			if (command == null) {
				continue;
			}
			
			int commandStart = command.getStart() > 0 ? command.getStart() - 1 : 0;
			
			for (int j = commandStart; j >= 0; j--) {
				
				char c = originalSourceCode.charAt(j);
				
				if (c == '{' || c == '}' || c == ';') {
					final int current = j;
					int index = j;
					
					boolean nonCommand = //false;
							this.nonCommandAnalyser
								.getItems()
								.stream()
								.anyMatch(n -> current >= n.getStart() && current <= n.getEnd());
					
					if (nonCommand) {
						continue;
					}
					
					if (index < sb.toString().length()) {
						index += 1;
					}
					
					sb.insert(index, makeAnalyseCommand(i));
					break;
				}
			}
		}
		
		return sb.toString();
	}
	
	private String makeAnalyseCommand(int i) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(analyseClass);
		sb.append(".add(");
		sb.append(i);
		sb.append(");");
		return sb.toString();
	}
}
