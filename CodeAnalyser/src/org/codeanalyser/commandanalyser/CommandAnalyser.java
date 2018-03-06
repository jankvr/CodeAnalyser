package org.codeanalyser.commandanalyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codeanalyser.interfaces.IAnalyser;
import org.codeanalyser.model.Command;
import org.codeanalyser.model.Item;

public class CommandAnalyser implements IAnalyser {
	private ArrayList<Item> methods;
	private ArrayList<Command> nonCommands;
	private final ArrayList<Command> commands;
	private final String source;
	private static final String METHOD_EXP = ";";
	
	public CommandAnalyser(String source) {
		this.source = source;
		this.commands = new ArrayList<Command>();
	}
	
	@Override
	public void computeIndices() {
		if (this.methods == null) {
			return;
		}
		
		// Compile regexp and make a pattern
		final Pattern pattern = Pattern.compile(METHOD_EXP);
		final Matcher matcher = pattern.matcher(this.source);

		// Iterate through the groups
		while (matcher.find()) {
			String commandHeader = matcher.group(0);
			final int startIndex = matcher.start();
			
			Command command = new Command(commandHeader, startIndex, 0); 
			
			boolean nonCommand = //false;
					this.nonCommands
						.stream()
						.anyMatch(n -> startIndex >= n.getStart() && startIndex <= n.getEnd());
			
			if (nonCommand) {
				continue;
			}
			
			for (Item method : this.methods) {
				if (command.getStart() >= method.getStart() 
						&& command.getStart() <= method.getEnd()) {
					command.setInMethod();
					commands.add(command);
					break;
				}
			}
		}
	}

	@Override
	public Collection<? extends Item> getItems() {
		return commands;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setItems(Collection<? extends Item> items) {
		this.methods = (ArrayList<Item>) items;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setNonCommandItems(Collection<? extends Item> items) {
		this.nonCommands = (ArrayList<Command>) items;
	}
}
