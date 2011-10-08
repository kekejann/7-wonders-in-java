package org.tjuscs.sevenwonders.core;

/**
 * The Class CommandOption.
 */
public class CommandOption {	
	
	/** The command. */
	Command command; 
	
	/** The number options. */
	int needBuy, leftHas, rightHas, totalCost, numberOptions;
	
	/** The card. */
	Card card;
	
	/** The can build stage. */
	boolean options, availableFree, buildable, canSell, canBuildStage;
	
	/** The ops. */
	int[][] ops;
	
	/** The Constant WEST. */
	static final int EAST = 0, WEST = 1;
	
	/** The needs srl. */
	SimpleResList leftSRL, rightSRL, needsSRL;
	
	/** The reason. */
	String reason;
	
	
	
	/**
	 * Instantiates a new command option.
	 * 
	 * @param crd
	 *            the crd
	 * @param ndBuy
	 *            the nd buy
	 * @param needs
	 *            the needs
	 * @param left
	 *            the left
	 * @param right
	 *            the right
	 * @param bldable
	 *            the bldable
	 * @param free
	 *            the free
	 */
	public CommandOption(Card crd, int ndBuy, SimpleResList needs, SimpleResList left, SimpleResList right, boolean bldable, boolean free){
		card = crd;
		if (left != null)
			leftHas = left.getTotalRes();
		if ( right != null)
			rightHas = right.getTotalRes();
		availableFree = free;
		needBuy = ndBuy;
		buildable = bldable;
		numberOptions = 1;
		reason = "";
		canSell = true;
		canBuildStage = false;
		command = Command.NONE;
		leftSRL = left; 
		rightSRL = right;
		needsSRL = needs;
	}
	
	/**
	 * Gets the reason.
	 * 
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Sets the reason.
	 * 
	 * @param reason
	 *            the new reason
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * Checks if is buildable.
	 * 
	 * @return true, if is buildable
	 */
	public boolean isBuildable() {
		return buildable;
	}

	/**
	 * Checks if is available free.
	 * 
	 * @return true, if is available free
	 */
	public boolean isAvailableFree() {
		return availableFree;
	}
	
	/**
	 * Can build stage.
	 * 
	 * @return true, if successful
	 */
	public boolean canBuildStage(){ 
		return canBuildStage;
	}
	
	/**
	 * Can sell.
	 * 
	 * @return true, if successful
	 */
	public boolean canSell(){ 		
		return canSell;
	}
	
	/**
	 * Sets the can sell.
	 * 
	 * @param canSell
	 *            the new can sell
	 */
	public void setCanSell(boolean canSell){
		this.canSell = canSell;
	}
	
	/**
	 * Sets the can build stage.
	 * 
	 * @param canBldStage
	 *            the new can build stage
	 */
	public void setCanBuildStage(boolean canBldStage){
		canBuildStage = canBldStage;
	}
	
	/**
	 * Sets the available free.
	 * 
	 * @param availableFree
	 *            the new available free
	 */
	public void setAvailableFree(boolean availableFree) {
		this.availableFree = availableFree;
	}
	
	/**
	 * Gets the left has.
	 * 
	 * @return the left has
	 */
	public int getLeftHas() {
		return leftSRL.getTotalRes();  // old way leftHas;
	}
	
	/**
	 * Sets the right has.
	 * 
	 * @param right
	 *            the new right has
	 */
	public void setRightHas(int right) {
		this.rightHas = right;
	}
	
	/**
	 * Gets the right has.
	 * 
	 * @return the right has
	 */
	public int getRightHas() {
		return rightSRL.getTotalRes(); // old rightHas;
	}
	
	/**
	 * Sets the left has.
	 * 
	 * @param left
	 *            the new left has
	 */
	public void setleftHas(int left) {
		this.leftHas = left;
	}
	
	/**
	 * Gets the needs srl.
	 * 
	 * @return the needs srl
	 */
	public SimpleResList getNeedsSRL() {
		return  needsSRL;
	}
	
	/**
	 * Gets the left srl.
	 * 
	 * @return the left srl
	 */
	public SimpleResList getLeftSRL() {
		return  leftSRL;
	}

	/**
	 * Sets the left srl.
	 * 
	 * @param leftSRL
	 *            the new left srl
	 */
	public void setLeftSRL(SimpleResList leftSRL) {
		this.leftSRL = leftSRL;
	}

	/**
	 * Gets the right srl.
	 * 
	 * @return the right srl
	 */
	public SimpleResList getRightSRL() {
		return rightSRL;
	}

	/**
	 * Sets the right srl.
	 * 
	 * @param rightSRL
	 *            the new right srl
	 */
	public void setRightSRL(SimpleResList rightSRL) {
		this.rightSRL = rightSRL;
	}

	/**
	 * Gets the card.
	 * 
	 * @return the card
	 */
	public Card getCard() {
		return card;
	}
	
	/**
	 * Sets the card.
	 * 
	 * @param card
	 *            the new card
	 */
	public void setCard(Card card) {
		this.card = card;
	}
	
	/**
	 * Checks if is options.
	 * 
	 * @return true, if is options
	 */
	public boolean isOptions() {
		return options;
	}
	
	/**
	 * Sets the options.
	 * 
	 * @param options
	 *            the new options
	 */
	public void setOptions(boolean options) {
		this.options = options;
	}
	
	/**
	 * Gets the total needed.
	 * 
	 * @return the total needed
	 */
	public int getTotalNeeded() {
		return leftSRL.getTotalRes() + rightSRL.getTotalRes();
	}
	
	/**
	 * Gets the number options.
	 * 
	 * @return the number options
	 */
	public int getNumberOptions() {
		return numberOptions;
	}
	
	/**
	 * Gets the east option num.
	 * 
	 * @param num
	 *            the num
	 * @return the east option num
	 */
	public int getEastOptionNum(int num){
		return ops[EAST][num];
	}
	
	/**
	 * Gets the west option num.
	 * 
	 * @param num
	 *            the num
	 * @return the west option num
	 */
	public int getWestOptionNum(int num){
		return ops[WEST][num];
	}
	
	/**
	 * Gets the options.
	 * 
	 * @return the options
	 */
	public int[][] getOptions() {
		return ops;
	}
	
	/**
	 * Needs buy decision.
	 * 
	 * @return true, if successful
	 */
	public boolean needsBuyDecision(){
		System.out.println("NBD: needBuy: " + needBuy);
		System.out.println("NBD: neighbourGoods: " + (getLeftHas() + getRightHas()) );
		if(needBuy != getLeftHas() + getRightHas() )
			return true;		
		else
			return false;
	}
	
		
	/**
	 * Adds the options.
	 * 
	 * @param east
	 *            the east
	 * @param west
	 *            the west
	 */
	public void addOptions(int east, int west){
		if(numberOptions == 1){
			ops = new int[2][20];
			ops[EAST][0] = rightHas;
			ops[WEST][0] = leftHas;
		}
		ops[EAST][numberOptions] = east;
		ops[WEST][numberOptions] = west;
		numberOptions++;
	}

	/**
	 * Sets the command.
	 * 
	 * @param cmd
	 *            the new command
	 */
	public void setCommand( Command cmd ){
		switch(cmd){
		case BUILD_CARD:
			command = (this.isBuildable() )? cmd : Command.NONE;
			break;
		case BUILD_STAGE:
			command = (this.canBuildStage() )? cmd : Command.NONE;
			break;
		case SELL_CARD:
			command = (this.canSell() )? cmd : Command.NONE;			
			break;
		case NONE:
		default:
			command = Command.NONE;				
		}		
	}
	
	/**
	 * Gets the command.
	 * 
	 * @return the command
	 */
	public Command getCommand(){
		return command;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuilder strB = new StringBuilder();
		
		strB.append(String.format("%s: %s buildable:[<%d>: E%d, W%d]" , card, (isBuildable()? "is": "isn't"), needBuy, leftHas, rightHas ));
		
//		strB.append(" ");
		strB.append(reason);
	
		return strB.toString();
	}
	
}  // end of CommandOption class
