package org.tjuscs.sevenwonders.core;

/**
 * The Enum CardColor.
 */
public enum CardColor {

	/** The YELLOW. */
	YELLOW("yellow"),

	/** The BROWN. */
	BROWN("brown"),

	/** The BLUE. */
	BLUE("blue"),

	/** The GREY. */
	GREY("grey"),

	/** The PURPLE. */
	PURPLE("purple"),

	/** The RED. */
	RED("red"),

	/** The GREEN. */
	GREEN("green"),

	/** The WHITE. */
	WHITE("white"); // used for virtual cards...ie SCI symbols

	/**
	 * Instantiates a new card color.
	 * 
	 * @param s
	 *            the s
	 */
	CardColor(String s) {
		str = s;
	}

	/** The str. */
	private String str;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	public String toString() {
		return str;
	}
	// public String abbrev(){ return abr; }
}
