package org.tjuscs.sevenwonders.core;

/**
 * The Class FreeSciSymbolAction.
 */
public class FreeSciSymbolAction implements Action, DelayedAction  {
	
	/** The board. */
	Board board;
		
	/* (non-Javadoc)
	 * @see org.tjuscs.sevenwonders.core.Action#activate(org.tjuscs.sevenwonders.core.Board)
	 */
	public void activate(Board brd) {	
		board = brd;
		GameManager.getManager().addEOGDelayedAction(this);			
	}
	
	/* (non-Javadoc)
	 * @see org.tjuscs.sevenwonders.core.DelayedAction#doDelayedAction()
	 */
	public void doDelayedAction() {		
		CommandOption[] options = new CommandOption[3];
		Card crd = new Card("Free Cog", 3, 3, CardColor.WHITE);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.COG, 1);
				
		options[0] = new CommandOption(crd, 0, null, null, null, true, true);
		
		crd = new Card("Free Compass", 3, 3, CardColor.WHITE);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.COMPASS, 1);
		
		options[1] = new CommandOption(crd, 0, null, null, null, true, true);
		
		crd = new Card("Free Tablet", 3, 3, CardColor.WHITE);
		crd.addCost(Resource.FREE, 0);
		crd.addGoods(Resource.TABLET, 1);
		
		options[2] = new CommandOption(crd, 0, null, null, null, true, true);
				
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println("TESTING ");
		System.out.println("TESTING FreeSciSymbolAction:");
		System.out.println("TESTING on: " + board.getName());
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		board.getPlayerChoice( options );		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "free science symbol at the end of the game ";
	}

}




