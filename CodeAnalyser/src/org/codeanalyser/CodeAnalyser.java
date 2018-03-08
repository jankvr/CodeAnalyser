package org.codeanalyser;

import java.util.ArrayList;

import org.codeanalyser.commandanalyser.CommandAnalyser;
import org.codeanalyser.commandanalyser.NonCommandAnalyser;
import org.codeanalyser.methodanalyser.MethodAnalyser;
import org.codeanalyser.model.Command;
import org.codeanalyser.model.EmptyListException;

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
	
	/**
	 * Analysing the code which includes computing indices 
	 * for all commands in the source code.
	 */
	public void analyse() {
		// Find '{', '}' or ';' in strings 
		nonCommandAnalyser.computeIndices();
		// Compute method indices
		methodAnalyser.setNonCommandItems(nonCommandAnalyser.getItems());
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
	
	public String editCode() throws EmptyListException {
		if (!this.analysed) {
			throw new EmptyListException("Not analysed");
		}
		
		if (commandAnalyser.getItems().isEmpty()) {
			return originalSourceCode;
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<Command> commands = (ArrayList<Command>) commandAnalyser.getItems();
		int commandSize = commands.size() - 1;
		StringBuilder sb = new StringBuilder(originalSourceCode);
		
		for (int i = commandSize; i >= 0; i--) {
			Command command = getCommand(commands, i);
			
			if (command == null) {
				continue;
			}
			
			int commandStart = computeCommandStart(command);
			
			for (int j = commandStart; j >= 0; j--) {
				
				char c = originalSourceCode.charAt(j);
				
				if (c == '{' || c == '}' || c == ';') {
					final int current = j;
					int index = j;
					
					boolean nonCommand = isNonCommand(current);
					
					if (!nonCommand) {
						index = stepBack(index, sb);
						sb.insert(index, makeAnalyseCommand(i));
						break;
					}
				}
			}
		}
		
		return sb.toString();
	}
	
	// ========== PRIVATE METHODS ============
	
	private int computeCommandStart(Command command) {
		return command.getStart() > 0 
				? command.getStart() - 1 
				: 0;
	}
	
	private int stepBack(int index, StringBuilder sb) {
		int newIndex = index;
		
		if (index < sb.toString().length()) {
			newIndex = index + 1;
		}
		
		return newIndex;
	}
	
	private boolean isNonCommand(int current) {
		return this.nonCommandAnalyser
				.getItems()
				.stream()
				.anyMatch(n -> current >= n.getStart() && current <= n.getEnd());
	}
	
	private Command getCommand(ArrayList<Command> commands, int index) {
		Command command;
		
		try {
			command = commands.get(index);
		}
		catch (IndexOutOfBoundsException e) {
			command = null;
		}
		
		return command;
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
