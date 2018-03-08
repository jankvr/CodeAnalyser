package org.codeanalyser.methodanalyser;

import java.util.ArrayList;
import java.util.List;

import org.codeanalyser.model.Command;

/**
 * Class for computing the end index for the given start index
 * 
 * @author Jan Kovar
 *
 */

public class MethodEndComputer {
	private final String source;
	private final ArrayList<Character> openingBrackets;
	private final ArrayList<Character> endingBrackets;
	private final boolean nonCommandsSet;
	private final ArrayList<Command> nonCommands;
	
	public MethodEndComputer(String source, List<Command> nonCommands, boolean nonCommandsSet) {
		this.source = source;
		this.nonCommands = (ArrayList<Command>) nonCommands;
		this.nonCommandsSet = nonCommandsSet;
		this.openingBrackets = new ArrayList<>();
		this.endingBrackets = new ArrayList<>();
	}

	/**
	 * Method for computing the end index
	 * 
	 * @param startIndex
	 * @return
	 */
	public int findEndIndex(int startIndex) {
		int endingIndex = -1;
		
		// Iterate from the given index through the EOF
		for (int i = startIndex; i < source.length(); i++) {
			final int index = i;
			
			char c = source.charAt(i);
			boolean nonCommand = false;
			
			if (nonCommandsSet) {
				nonCommand = this.nonCommands
						.stream()
						.anyMatch(n -> index >= n.getStart() && index <= n.getEnd());
			}
			
			// If there is a bracket, put it in the right list
			if (!nonCommand) {
				if (c == '{') {
					this.openingBrackets.add(c);
				}
				else if (c == '}') {
					this.endingBrackets.add(c);
				}
			}
			
			// If there is something in lists and the list sizes are equal, 
			// we find the right index.
			// Then we should clean the lists and exit the loop.
			if ((!openingBrackets.isEmpty() || !endingBrackets.isEmpty())
					&& openingBrackets.size() == endingBrackets.size()) {
				endingIndex = i;
				openingBrackets.clear();
				endingBrackets.clear();
				break;
			}
		}
		
		return endingIndex;
	}
	
}
