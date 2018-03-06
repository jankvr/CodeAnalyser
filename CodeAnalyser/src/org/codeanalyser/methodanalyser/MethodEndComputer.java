package org.codeanalyser.methodanalyser;

import java.util.ArrayList;

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
	
	public MethodEndComputer(String source) {
		this.source = source;
		this.openingBrackets = new ArrayList<Character>();
		this.endingBrackets = new ArrayList<Character>();
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
			char c = source.charAt(i);
			
			// If there is a bracket, put it in the right list
			if (c == '{') {
				this.openingBrackets.add(c);
			}
			else if (c == '}') {
				this.endingBrackets.add(c);
			}
			
			// If there is something in lists and the list sizes are equal, 
			// we find the right index.
			// Then we should clean the lists and exit the loop.
			if ((openingBrackets.size() > 0 || endingBrackets.size() > 0)
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
