package org.tjuscs.sevenwonders.core;

/**
 * The Interface Action.
 */
interface Action {

	/**
	 * Activate.
	 * 
	 * @param brd
	 *            the brd
	 */
	public void activate(Board brd);

	/**
	 * To string.
	 * 
	 * @return the string
	 */
	public String toString();
}
