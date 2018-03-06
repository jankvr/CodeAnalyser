package org.codeanalyser.model;

public class Command extends Item {

	private boolean inMethod;
	
	public Command(String methodHeader, int start, int end) {
		super(methodHeader, start, end);
		this.inMethod = false;
	}
	
	public void setInMethod() {
		this.inMethod = true;
	}
	
	public boolean isInMethod() {
		return this.inMethod;
	}
	
}
