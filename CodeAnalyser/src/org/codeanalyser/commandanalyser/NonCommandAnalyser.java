package org.codeanalyser.commandanalyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codeanalyser.interfaces.IAnalyser;
import org.codeanalyser.model.Command;
import org.codeanalyser.model.Item;

public class NonCommandAnalyser implements IAnalyser {
	private final ArrayList<Command> nonCommands;
	private final String source;
	private static final String NON_COMMAND_EXP = "(\".*)([;|\\{|\\}])(.*\")";
	
	public NonCommandAnalyser(String source) {
		this.source = source;
		this.nonCommands = new ArrayList<Command>();
	}
	
	public void computeIndices() {
		// Compile regexp and make a pattern
		final Pattern pattern = Pattern.compile(NON_COMMAND_EXP);
		final Matcher matcher = pattern.matcher(this.source);

		// Iterate through the groups
		while (matcher.find()) {
			String commandHeader = matcher.group(0);
			int startIndex = matcher.start();
			int endIndex = matcher.end();
			
			Command command = new Command(commandHeader, startIndex, endIndex); 

			nonCommands.add(command);
		}
	}

	@Override
	public Collection<? extends Item> getItems() {
		return nonCommands;
	}

	@Override
	public void setItems(Collection<? extends Item> items) {}

	@Override
	public void setNonCommandItems(Collection<? extends Item> items) {}
}
