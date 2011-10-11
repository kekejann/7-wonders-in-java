package org.tjuscs.sevenwonders.core;

/**
 * The Interface Player.
 */
public interface Player {

	/**
	 * Sets the board.
	 * 
	 * @param b
	 *            the new board
	 */
	public void setBoard(Board b);

	/**
	 * Make choice.
	 * 
	 * @param options
	 *            the options
	 * @return the command option
	 */
	public CommandOption makeChoice(CommandOption[] options);

	/**
	 * Make buy decision.
	 * 
	 * @param needs
	 *            the needs
	 * @param leftGoods
	 *            the left goods
	 * @param rightGoods
	 *            the right goods
	 */
	public void makeBuyDecision(SimpleResList needs, SimpleResList leftGoods,
			SimpleResList rightGoods);
}
