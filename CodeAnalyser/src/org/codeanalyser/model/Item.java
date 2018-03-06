package org.codeanalyser.model;

public class Item {
	private final String itemHeader;
	private final int start;
	private final int end;
	
	public Item(String methodHeader, int start, int end) {
		this.itemHeader = methodHeader;
		this.start = start;
		this.end = end;
	}
	
	public String getItemHeader() {
		return itemHeader;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
}
