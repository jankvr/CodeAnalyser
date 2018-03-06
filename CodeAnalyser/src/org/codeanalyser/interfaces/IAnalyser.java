package org.codeanalyser.interfaces;

import java.util.Collection;

import org.codeanalyser.model.Item;

/**
 * Interface for computing method and command indices and returning these indices.
 * 
 * @author Jan Kovar
 */
public interface IAnalyser {
	
	public void computeIndices();
	
	public Collection<? extends Item> getItems();
}
