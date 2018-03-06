package org.codeanalyser;

import java.util.ArrayList;
import java.util.HashSet;

import org.codeanalyser.interfaces.ICodeCoverage;

public class CodeCoverage implements ICodeCoverage {
	private final ArrayList<Integer> stackTrace;
	private final HashSet<Integer> calledCommands;
	private final int totalCommands;
	
	public CodeCoverage(int totalCommands) {
		this.stackTrace = new ArrayList<Integer>();
		this.calledCommands = new HashSet<Integer>();
		this.totalCommands = totalCommands;
	}
	
	public void add(int row) {
		stackTrace.add(row);
		
		if (!calledCommands.contains(row)) {
			calledCommands.add(row);
		}
	}
	
	public double computeCodeCoveragePercentage() {
		if (calledCommands.isEmpty()) {
			return 0;
		}
		
		double calledCommandsSize = (double) this.calledCommands.size();
		double functionalRowsSize = (double) this.totalCommands;
		
		double coverage = (calledCommandsSize / functionalRowsSize) * 100;
		
		return coverage;
	}
	
	public void printCalledCommands() {
		for (int rowNumber : this.calledCommands) {
			System.out.print(rowNumber + " ");
		}
	}
	
	public void printCompleteStackTrace() {
		for (int rowNumber : this.stackTrace) {
			System.out.print(rowNumber + " ");
		}
	}
	
	public void clearStackTrace() {
		this.stackTrace.clear();
	}
}
