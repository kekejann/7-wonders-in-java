package org.tjuscs.sevenwonders.core;

/**
 * The Enum Command.
 */
public enum Command {

	/** The BUIL d_ card. */
	BUILD_CARD("Build Card"),

	/** The BUIL d_ stage. */
	BUILD_STAGE("Build Stage"),

	/** The SEL l_ card. */
	SELL_CARD("Sell Card"),

	/** The NONE. */
	NONE("No Action:ERROR");

	/**
	 * Instantiates a new command.
	 * 
	 * @param s
	 *            the s
	 */
	Command(String s) {
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

}
