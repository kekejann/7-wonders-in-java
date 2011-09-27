import java.util.*;


public class GameManager {
	ArrayList<DelayedAction> EOTurnDelayedActionList;
	ArrayList<DelayedAction> EOTurnRemoveList;
	List<DelayedAction> EOAgeDelayedActionList;
	List<DelayedAction> EOGameDelayedActionList;
	private static GameManager gm;
	CardManager cardManager;
	Player[] players;
	Board[] boards;
	Hand[] hands;
	int numPlayers;
	
	public StringBuilder[] out;
	
	public static final int LEFT = 0, RIGHT = 1;
	
	public static GameManager getManager(){
		if(gm == null)
			gm = new GameManager();
		return gm;
	}
	
	private GameManager(){
		EOTurnDelayedActionList = new ArrayList<DelayedAction>();
		EOAgeDelayedActionList = new ArrayList<DelayedAction>();
		EOGameDelayedActionList = new ArrayList<DelayedAction>();
		EOTurnRemoveList = new ArrayList<DelayedAction>();
	}

	public void startGame(int numPlyers){
		this.numPlayers = numPlyers;
		cardManager = new CardManager(numPlayers);
		players = new Player[numPlayers];
		boards = BoardFactory.makeAandBSideBoards(numPlayers);
		
		out = new StringBuilder[numPlayers];
		for(int i = 0; i < out.length; i++){
			out[i] = new StringBuilder();
			boards[i].setIndex(i);			
		}
		
		boards[0].setLeftNeighbor(boards[numPlayers-1]);
		boards[0].setRightNeighbor( boards[1] ) ;
		
		for(int i = 1; i < boards.length-1; i++){			
			boards[i].setLeftNeighbor( boards[i-1] );
			boards[i].setRightNeighbor( boards[i+1] ) ;
		}
		
		boards[numPlayers-1].setLeftNeighbor( boards[numPlayers-2] );
		boards[numPlayers-1].setRightNeighbor( boards[0] ) ;
		Player player;
		for (Board board: boards){
			player = new RandBot();
			player.setBoard(board);
			board.setPlayer(player);			
		}
	}
	
	
	public CardManager getCardManager(){
		return cardManager;
	}
	
	public void addEOTDelayedAction(DelayedAction da){
		EOTurnDelayedActionList.add(da);
	}

	public void addEOADelayedAction(DelayedAction da){
		EOAgeDelayedActionList.add(da);
	}

	public void addEOGDelayedAction(DelayedAction da){
		EOGameDelayedActionList.add(da);
	}

	public void removeEOTDelayedAction(DelayedAction da){
		EOTurnRemoveList.add(da);
	}

	public void removeEOADelayedAction(DelayedAction da){
		EOAgeDelayedActionList.remove(da);
	}

	public void removeEOGDelayedAction(DelayedAction da){
		EOGameDelayedActionList.remove(da);
	}

	public void startAge(int ageNum){		
		hands = cardManager.setupHands(ageNum);
		System.out.println("Start Age " + ageNum + "\n");
		for( StringBuilder sb: out){
			sb.append("\nStart Age " + ageNum + "\n");
//			sb.append(b)
		}
//		for(Hand hand: hands){
//			System.out.println(hand);
//		}
	}
	
	public void startTurn(int trnNum){  			
		int ind = -1; 
		System.out.println("\nTurn " + trnNum );
		
		for(Board board: boards){
			out[ind+1].append("\nTurn " + trnNum );
			out[ind+1].append("\n" + board);
			System.out.println("\n\n" + board);
			board.takeTurn(hands[(ind+trnNum)%numPlayers], trnNum);
			ind++;
//			out[ind].append("\n" + board );
		}		
	}
	
	public void doEndOfTurn(int trnNum){
		for(Board board: boards){
			board.addTurnSales();
		}
		
		for(DelayedAction da : EOTurnDelayedActionList ){
			da.doDelayedAction();
		}
		
		for(DelayedAction da : EOTurnRemoveList ){
			EOTurnDelayedActionList.remove(da);
		}
	}

	public void doEndOfAge(int ageNum){
		int [] victVP = { 0, 1, 3, 5};
		
		for(DelayedAction da : EOAgeDelayedActionList){
			da.doDelayedAction();
		}
	
		for(Board board: boards){			
			int ourSheild = (board.goods.containsKey(Resource.SHEILD) ? board.goods.get(Resource.SHEILD): 0 );
			int lftNghborSheild = (board.getLeftNeighbor().goods.containsKey(Resource.SHEILD) ? board.getLeftNeighbor().goods.get(Resource.SHEILD): 0 );
			int rghtNghborSheild = (board.getRightNeighbor().goods.containsKey(Resource.SHEILD) ? board.getRightNeighbor().goods.get(Resource.SHEILD): 0 );
			if( ourSheild > lftNghborSheild )
				board.militaryVPS[LEFT][ageNum] = victVP[ageNum]; 
			else if(ourSheild < lftNghborSheild ){
				board.militaryVPS[LEFT][ageNum] = -1;
			}
			if( ourSheild > rghtNghborSheild )
				board.militaryVPS[RIGHT][ageNum] = victVP[ageNum]; 
			else if(ourSheild < rghtNghborSheild ){
				board.militaryVPS[RIGHT][ageNum] = -1;
			}
			board.addToVPs(board.militaryVPS[LEFT][ageNum]);
			board.addToVPs(board.militaryVPS[RIGHT][ageNum]);
		}
		
	}	// end of doEndOfAge method

	
	public void doEndOfGame(){
		for(DelayedAction da : EOGameDelayedActionList){
			da.doDelayedAction();
		}
		// TODO do Victory Calculations
		
	}	// end of doEndOfGame method

	
	public static void main(String[] args) {
		
		GameManager gm = GameManager.getManager();
		gm.startGame( 4 );
		
		Player player = new HumanDialogBasedPlayer();
		player.setBoard(gm.boards[0]);
		gm.boards[0].setPlayer(player);
		
//		Player player;
		for(Board b: gm.boards){
//			player = new RandBot(b);
			System.out.print( b );
			System.out.println( b.stages[0].name + "\n" );
//			b.setPlayer(player);
//			System.out.println( "\t" + b.getLeftNeighbor().getName() + "      " + b.getRightNeighbor().getName() + "\n");
		}
		
		for (int age = 1; age <= 3; age++){
		
			gm.startAge(age);
			
			for (int trn = 1; trn < 7; trn ++){
				gm.startTurn(trn);
				gm.doEndOfTurn(trn);
			}	
			
			gm.doEndOfAge(age);
			
			
			System.out.println( "\n\nAfter Age " + age );
			for( StringBuilder sb: gm.out){
				sb.append( "\n\nAfter Age " + age );				
			}
			
			int ind = 0;
			for(Board b: gm.boards){
				System.out.print( b );			
				System.out.print("Can Build Stage: " + b.canBuildStage(b.stages[b.stagesCompleted], new SimpleResList(b.stages[b.stagesCompleted]) ) );
				System.out.println("  Cost: " + b.stages[b.stagesCompleted].cost);
				System.out.println( "\tLeftMilVP: " + b.militaryVPS[LEFT][age] + "   RghtMilVP: " + b.militaryVPS[RIGHT][age] + "\n");
				gm.out[ind].append( "\n" + b);
				gm.out[ind].append("\tCan Build Stage: " + b.canBuildStage(b.stages[b.stagesCompleted], new SimpleResList(b.stages[b.stagesCompleted]) ) );
				gm.out[ind].append("\t\t Stage Cost: " + b.stages[b.stagesCompleted] ) ;
				gm.out[ind].append( "\n\tLeftMilVP: " + b.militaryVPS[LEFT][age] + "   RghtMilVP: " + b.militaryVPS[RIGHT][age] + "\n");
				for( Card card : b.structures){
					gm.out[ind].append( "\n\t" + card);
					System.out.println( "\t" + card );
				}
				gm.out[ind].append( "\n");
				System.out.println( "\n" );
				ind++;
			}
		}
		
		gm.doEndOfGame();
		
		int ind = 0;
		String[] dirStr = {"vs Left Neighbor : ", "vs Right Neighbor: " }; 
		int totalMilVPs = 0, oneNghbr = 0;
		System.out.println( "\nEnd of Game Scoring" );
		System.out.println( "\n" );
		for(Board b: gm.boards){
			System.out.print( b );
			gm.out[ind].append( "\n\nEnd of Game Scoring" );
			gm.out[ind].append( "\n" );
			gm.out[ind].append("\n" + b);
			totalMilVPs = 0;
			for(int side = 0; side < 2; side++){
				System.out.print( "\t" + dirStr[side] );
				gm.out[ind].append("\t" + dirStr[side]);
				oneNghbr = 0;
				for(int i = 1; i < 4; i++ ){
					System.out.print( "\t" + b.militaryVPS[side][i] );
					gm.out[ind].append("\t" + b.militaryVPS[side][i]);
					oneNghbr += b.militaryVPS[side][i];
				}
				totalMilVPs += oneNghbr;
				gm.out[ind].append("\t <" + oneNghbr + ">\n" );
				System.out.print( "\t <" + oneNghbr + ">\n" );								
			}
			gm.out[ind].append("\tTotal Military Score: " + totalMilVPs + "\n" );
			System.out.print( "\tTotal Military Score: " + totalMilVPs + "\n" );
			int sciScore = b.scoreVPs();
			System.out.println( "\tScience: " + sciScore);
			System.out.println( "\tTotal Score: " + ( sciScore + b.goods.get(Resource.VP) ) );
			gm.out[ind].append( "\n\tScience: " + sciScore);
			gm.out[ind].append( "\n\tTotal Score: " + ( sciScore + b.goods.get(Resource.VP) ) );
			
			gm.out[ind].append( "\n");
			System.out.println( "\n" );
			ind++;
		}
		
		// display one of the board's game history
		System.out.println( gm.out[0].toString());
		
		
		
		
	
		
		
		
		
		
		
//		EnumMap<Resource, Integer> test = new EnumMap<Resource, Integer>(Resource.class);
//		Integer num;
//		Resource res = Resource.WOOD;
//		test.put(Resource.WOOD, 1);
//		test.put(Resource.CLOTH, 3);
//		test.put(Resource.ORE, 4);
//
//
//		System.out.println( res.toString() + " has " + test.get(res) );
//		num = test.get(res);
//		test.put(res, ++num);
//
//		System.out.println( res.toString() + " has " + test.get(res) );
//
//		Set<Resource> set = test.keySet();
//		System.out.println("This card cost: " );
//		for(Resource r: set){
//			System.out.println( test.get(r) + " " + r.toString() );
//		}
     
//		GameManager gm = GameManager.getManager();
		
//		System.out.println("testing CardManager");
		

/*		
		
		CardManager cMan = new CardManager(3);
		
		Board[] brds = new Board[3];
		Hand[] hands;
		hands = cMan.setupHands(1);
		hands = cMan.setupHands(2);
		hands = cMan.setupHands(3);
		hands = cMan.setupHands(1);
		
		int cnt = 1;
		
		for(Hand hand : hands){			
			System.out.println(String.format("hand %d: size %d", cnt++ ,hand.size() ) );
		}
		
		brds[0] = BoardFactory.ALEXANDRIA_A.buildBoard();
		brds[1] = BoardFactory.EPHESUS_A.buildBoard();		// A's east neighbor
		brds[2] = BoardFactory.RHODES_A.buildBoard();  		// A's west neighbor

		
		brds[0].setLeftNeighbor(brds[2]);
		brds[0].setRightNeighbor(brds[1]);
		brds[1].setLeftNeighbor(brds[0]);
		brds[1].setRightNeighbor(brds[2]);
		brds[2].setLeftNeighbor(brds[1]);
		brds[2].setRightNeighbor(brds[0]);
		
			
		
//		CommandOption[] options= brds[0].buildCommandOptions(hands[0]);
//		System.out.println("Buildable:");
//		for(CommandOption op : options){			
//			if(op.isBuildable())
//				System.out.println(String.format("\t %s", op ));
//		}
//		
//		System.out.println("UnBuildable:");
//		for(CommandOption op : options){			
//			if( ! op.isBuildable())
//				System.out.println(String.format("\t %s", op ));
//		}
		
		
        for(int i = 0; i < 3; i++ ) { 				// add 6 Age I cards to each of the boards
			brds[i].addCard(hands[i].remove(0));
			brds[i].addCard(hands[i].remove(0));
			brds[i].addCard(hands[i].remove(0));
			brds[i].addCard(hands[i].remove(0));
			brds[i].addCard(hands[i].remove(0));
			brds[i].addCard(hands[i].remove(0));
        }
		
        
        System.out.print( "\n\nWest: " + brds[2] +  "\n" );
        System.out.print( "Home: " + brds[0] +  "\n");
        System.out.print( "East: " + brds[1] +  "\n");
  
  
        hands = cMan.setupHands(2);
        
        System.out.println( "\n" );
        System.out.print( brds[0] );
        for(Card c : brds[0].structures){
        	System.out.println("\t" + c);
        }
        
        
        CommandOption[] options; 
        options = brds[0].buildCommandOptions(hands[0]);
		System.out.println("Buildable:");
		for(CommandOption op : options){			
			if(op.isBuildable())
				System.out.println(String.format("\t %s", op ));
		}
		
		System.out.println("UnBuildable:");
		for(CommandOption op : options){			
			if( ! op.isBuildable())
				System.out.println(String.format("\t %s", op ));
		}
		
		 for(int i = 0; i < 3; i++ ) {
			System.out.println("\n");
			System.out.println(brds[i]);
			System.out.println("Has " + brds[i].getNumberOfStages() + " stages");
			for(int s = 0; s < brds[i].stages.length; s++)
				System.out.println(brds[i].stages[s]);
		 }
			
		
        
//        Card c = hand2.remove("Vineyard");
//        if(c == null){
//        	System.out.println("Vineyard not found" );
//        	c = hand.remove(0);
//        }
//        
//        System.out.println( "Before Vineyard");
//		System.out.println( brds[1]);
//		
//    	brds[1].addCard(c);
//    	
////    	gm.doEndOfTurn();
//    	
//    	c = hand2.remove("Bazar");
//        if(c == null){
//        	System.out.println("Bazar not found" );
//        	c = hand.remove(0);
//        }
//        
//        System.out.println( "Before Bazar");
//		System.out.println( brds[2]);
//		
//    	brds[2].addCard(c);
//    	
//    	gm.doEndOfTurn();
//    	
//    	System.out.println( "After Vineyard and Bazar");
//    	System.out.println( brds[1]);
//    	System.out.println( brds[2]);
//    	
////    	gm.doEndOfTurn();
////
////    	System.out.println( "After Vineyard and Bazar");
////    	System.out.println( brds[1]);
////    	System.out.println( brds[2]);
//    	
//    	c = hand3.remove("Haven");
//        if(c == null){
//        	System.out.println("Haven not found" );
//        	c = hand.remove(0);
//        }
//        
//        brds[1].addCard(c);
//        gm.doEndOfTurn();
//
//    	System.out.println( "After Haven");
//    	System.out.print( brds[1] );
//    	for(Card crd : brds[1].structures){
//			System.out.println("\t" + crd.name + "\t" + crd.color.toString());
//		}
//    	System.out.print( brds[2] );
//	
////		System.out.println( "Remaining hand(AGE I Cards): " + hand );

			
*/
	}  // end of GameManager.main


}
