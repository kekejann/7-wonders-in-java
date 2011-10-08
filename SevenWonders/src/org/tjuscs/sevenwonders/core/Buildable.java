package org.tjuscs.sevenwonders.core;
import java.util.Set;


/**
 * The Interface Buildable.
 */
public interface Buildable {
	
	/**
	 * Gets the costs.
	 * 
	 * @return the costs
	 */
	public Set<Resource> getCosts();
	
	/**
	 * Cost cnt.
	 * 
	 * @param r
	 *            the r
	 * @return the int
	 */
	public int costCnt( Resource r);
	
	/**
	 * Gets the goods.
	 * 
	 * @return the goods
	 */
	public Set<Resource> getGoods();
	
	/**
	 * Goods cnt.
	 * 
	 * @param r
	 *            the r
	 * @return the int
	 */
	public int goodsCnt( Resource r);
	
	/**
	 * Checks for action.
	 * 
	 * @return true, if successful
	 */
	public boolean hasAction();	
	
	/**
	 * Checks for resources.
	 * 
	 * @return true, if successful
	 */
	public boolean hasResources();	
	
	/**
	 * Checks for or resources.
	 * 
	 * @return true, if successful
	 */
	public boolean hasOrResources();
}
