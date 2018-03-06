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
	public void setItems(Collection<? extends Item> items);
	public Collection<? extends Item> getItems();
	public void setNonCommandItems(Collection<? extends Item> items);
}
