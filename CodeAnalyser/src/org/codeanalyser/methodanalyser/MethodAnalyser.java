package org.codeanalyser.methodanalyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codeanalyser.interfaces.IAnalyser;
import org.codeanalyser.model.Command;
import org.codeanalyser.model.Item;

/**
 * Class is used for finding exact indices of all methods in the given code.
 * 
 * @author Jan Kovar
 */
public class MethodAnalyser implements IAnalyser {
	private final String source;
	private final ArrayList<Item> methods;
	private static final String METHOD_EXP = "(public|private|protected)(.*)?(\\((.*)\\))([^;])( *\\{)?";
	private ArrayList<Command> nonCommands;
	private boolean nonCommandsSet;
	
	public 
	MethodAnalyser(String source) {
		this.source = source;
		this.methods = new ArrayList<>();
		this.nonCommandsSet = false;
		
	}
	
	/**
	 * Main method for computing indices.
	 * 
	 * RegExp is used for getting method header and index 
	 * and then {@link MethodEndComputer} is being used to compute ending index.
	 */
	@Override
	public void computeIndices() {
		// Compile regexp and make a pattern
		final Pattern pattern = Pattern.compile(METHOD_EXP);
		final Matcher matcher = pattern.matcher(this.source);

		MethodEndComputer computer = new MethodEndComputer(source, nonCommands, nonCommandsSet);
		
		// Iterate through the groups
		while (matcher.find()) {
			String methodHeader = matcher.group(0);
			int startIndex = matcher.start();
			
			int endIndex = computer.findEndIndex(startIndex);
			
			Item method = new Item(methodHeader, startIndex, endIndex); 

			methods.add(method);
		}
	}

	@Override
	public List<Item> getItems() {
		return methods;
	}

	@Override
	public void setItems(Collection<? extends Item> items) {
		throw new UnsupportedOperationException("Not needed");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setNonCommandItems(Collection<? extends Item> items) {
		this.nonCommands = (ArrayList<Command>) items;
		this.nonCommandsSet  = true;
	}
}
