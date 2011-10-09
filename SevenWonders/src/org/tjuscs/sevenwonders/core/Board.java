package org.tjuscs.sevenwonders.core;

import java.util.*;

/**
 * The Class Board.
 */
public class Board {

	/** The goods. */
	EnumMap<Resource, Integer> goods;

	/** The actions. */
	List<Action> actions;

	/** The free list. */
	Set<String> freeList;

	/** The structures. */
	Set<Card> structures;

	/** The color count. */
	Map<CardColor, Integer> colorCount;

	/** The brd name. */
	String brdName;

	/** The initial res. */
	Resource initialRes;

	/** The right neighbor. */
	Board leftNeighbor, rightNeighbor;

	/** The military victory points. */
	int militaryVPS[][];

	/** The stages. */
	Stage[] stages;

	/** The stages completed. */
	int stagesCompleted;

	/** The or cards by res. */
	EnumMap<Resource, ArrayList<Card>> orCardsbyRes;

	/** The or cards. */
	ArrayList<Card> orCards;

	/** The sell or list. */
	ArrayList<SimpleResList> orList, sellOrList;

	/** The res list. */
	SimpleResList resList;

	/** The player. */
	Player player;

	/** The turns resource income. */
	int turnsResourceIncome;

	// used for action that allows play of seventh card
	/** The save seventh card. */
	boolean saveSeventhCard;

	/** The seventh card. */
	Card seventhCard;

	/** The index. */
	int index; // used to build GameManager.out[] String

	/**
	 * Gets the index.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 * 
	 * @param index
	 *            the new index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/** The manf cost. */
	int leftRawCost = 2, rightRawCost = 2, manfCost = 2;

	/**
	 * Instantiates a new board.
	 */
	public Board() {
		this("NO Name", Resource.FREE, 3);
	}

	/**
	 * Instantiates a new board.
	 * 
	 * @param name
	 *            the name
	 * @param initRes
	 *            the init res
	 * @param numStages
	 *            the num stages
	 */
	public Board(String name, Resource initRes, int numStages) {
		goods = new EnumMap<Resource, Integer>(Resource.class);
		brdName = name;
		initialRes = initRes;
		goods.put(Resource.COIN, 3);
		goods.put(Resource.VP, 0);
		militaryVPS = new int[2][4];
		goods.put(initialRes, 1);
		stagesCompleted = 0;
		saveSeventhCard = false;

		freeList = new HashSet<String>();
		structures = new HashSet<Card>();
		actions = new ArrayList<Action>();
		stages = new Stage[numStages];
		colorCount = new EnumMap<CardColor, Integer>(CardColor.class);
		orCardsbyRes = new EnumMap<Resource, ArrayList<Card>>(Resource.class);
		orCards = new ArrayList<Card>();
		orList = new ArrayList<SimpleResList>();
		sellOrList = new ArrayList<SimpleResList>();
		resList = new SimpleResList();
		resList.addResource(initRes);
		turnsResourceIncome = 0;
	}

	/**
	 * Adds the card.
	 * 
	 * @param c
	 *            the c
	 */
	public void addCard(Card c) {
		if (structures.contains(c)) {
			System.out.println("ERROR: Board already contains " + c);
			return;
		}

		if (!c.hasOrResources()) {
			// System.out.println("non-or card");
			resList.addCard(c);
		}

		Set<Resource> cardGoods = c.getGoods();
		int newAmt;
		for (Resource r : cardGoods) {
			newAmt = c.goodsCnt(r);
			if (goods.containsKey(r)) {
				newAmt += goods.get(r);
			}
			goods.put(r, newAmt);

			Resource[] orOpts = r.getOptionalRes(); // trying to add OR cards
			if (orOpts != null) {
				orCards.add(c);
				SimpleResList srl = new SimpleResList(c);
				orList.add(srl);
				if (c.getColor() == CardColor.BROWN
						|| c.getColor() == CardColor.GREY)
					sellOrList.add(srl);
				for (Resource res : orOpts) {
					if (!orCardsbyRes.containsKey(res)) {
						orCardsbyRes.put(res, new ArrayList<Card>());
					}
					orCardsbyRes.get(res).add(c);
				}
			}
		}

		CardColor cardClr = c.getColor();
		int numOfThatColor = 0;
		if (colorCount.containsKey(cardClr)) {
			numOfThatColor = colorCount.get(cardClr);
		}
		colorCount.put(cardClr, numOfThatColor + 1);
		if (c.hasAction())
			c.getAction().activate(this); // activate any actions
		structures.add(c);

		if (c.freeList != null)
			for (String name : c.freeList) {
				freeList.add(name);
			}
	} // end of addCard method

	/**
	 * Gets the stages completed.
	 * 
	 * @return the stages completed
	 */
	public int getStagesCompleted() {
		return stagesCompleted;
	}

	/**
	 * Adds the stage.
	 * 
	 * @param stg
	 *            the stg
	 */
	public void addStage(Stage stg) {
		if (!stg.hasOrResources()) {
			// System.out.println("non-or card");
			resList.addCard(stg);
		}

		Set<Resource> stageGoods = stg.getGoods();
		int newAmt;
		for (Resource r : stageGoods) {
			newAmt = stg.goodsCnt(r);
			if (goods.containsKey(r)) {
				newAmt += goods.get(r);
			}
			goods.put(r, newAmt);

			Resource[] orOpts = r.getOptionalRes(); // trying to add OR cards
			if (orOpts != null) {
				// orCards.add(c);
				SimpleResList srl = new SimpleResList(stg);
				orList.add(srl);
			}
		}
		if (stg.hasAction())
			stg.getAction().activate(this); // activate any actions

		stages[stg.getStageNumber()] = stg;
		stagesCompleted++;
	}

	/**
	 * Gets the number of stages.
	 * 
	 * @return the number of stages
	 */
	public int getNumberOfStages() {
		return stages.length;
	}

	/**
	 * Gets the player choice.
	 * 
	 * @param cmdOps
	 *            the cmd ops
	 * @return the player choice
	 */
	public void getPlayerChoice(CommandOption[] cmdOps) {
		implementCommand(player.makeChoice(cmdOps));
	}

	/**
	 * Implement command.
	 * 
	 * @param cmdOpt
	 *            the cmd opt
	 */
	private void implementCommand(CommandOption cmdOpt) {
		Command cmd = cmdOpt.getCommand();
		Card card = cmdOpt.getCard();
		System.out.println("\n\nBefore:  " + this + ": " + cmd.toString()
				+ "  " + card);
		switch (cmd) {
		case BUILD_CARD:
			if (!cmdOpt.isAvailableFree()) {
				SimpleResList leftSRL = cmdOpt.getLeftSRL();
				SimpleResList rightSRL = cmdOpt.getRightSRL();

				// System.out.println("\n\nbuilding:  " + card);
				// System.out.println("building:total:  " +
				// cmdOpt.getTotalNeeded());
				// System.out.println("building:needs:  " +
				// cmdOpt.getNeedsSRL());
				// System.out.println("\nbuilding:leftRes:  " + leftSRL );
				// System.out.println("building:rightRes:  " + rightSRL );
				// System.out.println("needsBuyDecision: " +
				// cmdOpt.needsBuyDecision() );
				int cost, lCost = 0, rCost = 0;
				if (!cmdOpt.needsBuyDecision()) {
					// left first
					lCost = leftSRL.getNumRawGoods() * leftRawCost
							+ leftSRL.getNumManfGoods() * manfCost;
					// right second
					rCost = rightSRL.getNumRawGoods() * rightRawCost
							+ rightSRL.getNumManfGoods() * manfCost;
					cost = lCost + rCost;
				} else {
					// resolve conflicted resource
					player.makeBuyDecision(cmdOpt.getNeedsSRL(), leftSRL,
							rightSRL);
					// left first
					// System.out.println("after decision:");
					// System.out.println("building:leftRes:  " + leftSRL );
					// System.out.println("building:rightRes:  " + rightSRL );

					lCost = leftSRL.getNumRawGoods() * leftRawCost
							+ leftSRL.getNumManfGoods() * manfCost;
					// right second
					rCost = rightSRL.getNumRawGoods() * rightRawCost
							+ rightSRL.getNumManfGoods() * manfCost;
					cost = lCost + rCost;
				}

				if (cost > this.getTotalCoins()) {
					// System.out.println("Coin cost: " + cost );
					// System.out.println("Cost exceeds total coins: " + cost );
					// System.out.println(card +
					// " is instead sold for 3 coins ");
					GameManager.getManager().out[getIndex()].append("Selling "
							+ card);
					this.addToCoins(3);
					discardCard(card);
					break;
				}

				if (lCost > 0) {
					this.leftNeighbor.buyResources(lCost);
					System.out.print("Paying " + this.leftNeighbor.getName()
							+ " " + lCost);
					GameManager.getManager().out[getIndex()].append("Paying "
							+ lCost + " to " + leftNeighbor.getName() + "\n");
					GameManager.getManager().out[leftNeighbor.getIndex()]
							.append("\nPaid to us " + lCost + " by "
									+ getName());
					if (this.leftRawCost < 2)
						System.out.print(" <raw discount> ");
					if (this.manfCost < 2)
						System.out.print(" <manf discount> ");
					System.out.println();
				}
				if (rCost > 0) {
					this.rightNeighbor.buyResources(rCost);
					System.out.print("Paying " + this.rightNeighbor.getName()
							+ " " + rCost);
					GameManager.getManager().out[getIndex()].append("Paying "
							+ rCost + " to " + rightNeighbor.getName() + "\n");
					GameManager.getManager().out[rightNeighbor.getIndex()]
							.append("\nPaid to us " + rCost + " by "
									+ getName());
					if (this.rightRawCost < 2)
						System.out.print(" <raw discount> ");
					if (this.manfCost < 2)
						System.out.print(" <manf discount> ");
					System.out.println();
				}
				System.out.println("Coin cost: " + cost);

				this.addToCoins(-cost);
			}
			GameManager.getManager().out[getIndex()].append("Building " + card);
			// pay "to bank" coin cost for the card
			if (card.cost.containsKey(Resource.COIN)) {
				this.addToCoins(-card.cost.get(Resource.COIN));
			}
			addCard(card);

			break;
		case BUILD_STAGE:
			// command = (this.canBuildStage() )? cmd : Command.NONE;
			break;
		case SELL_CARD:
			this.addToCoins(3);
			discardCard(card);
			GameManager.getManager().out[getIndex()]
					.append("\nSelling " + card);
			break;
		case NONE:
		default:
			System.out
					.println("OOOPs, I haven't implemented that ability into takeTurn yet");
		}

		System.out.println("After:  " + this + ": ");
	}

	/**
	 * Take turn.
	 * 
	 * @param hand
	 *            the hand
	 * @param trnNum
	 *            the trn num
	 */
	public void takeTurn(Hand hand, int trnNum) {
		CommandOption opt = player.makeChoice(buildCommandOptions(hand));
		this.implementCommand(opt);

		Card card = opt.getCard();
		hand.remove(card.getName());

		if (trnNum == 6 && saveSeventhCard) {
			System.out.println("\nTESTING: " + this.toString() + " has: "
					+ hand.size() + " cards left in after 6th turn ");
			seventhCard = hand.get(0);
			hand.remove(seventhCard.getName());
		}
	}

	// TODO need to add logic for using 'or' x3 and x4 resources and then
	// neighbor.
	/**
	 * Builds the command options.
	 * 
	 * @param hand
	 *            the hand
	 * @return the command option[]
	 */
	@SuppressWarnings("unused")
	public CommandOption[] buildCommandOptions(Hand hand) {
		CommandOption[] options = new CommandOption[hand.size()];
		Card card = null;
		Set<Resource> costSet = null;
		int budget = goods.get(Resource.COIN);
		int resNeeded;
		SimpleResList cardCost;
		System.out.println("FreeList: " + freeList);

		loop: for (int i = 0; i < hand.size(); i++) {
			card = hand.get(i);
			System.out.println(card);

			// check if the board already has a copy of this card
			for (Card crd : structures) {
				if (crd.getName().equals(card.getName())) {
					options[i] = new CommandOption(card, 0, null, null, null,
							false, false);
					options[i].setReason(" already has one ");
					// System.out.println("board already has a copy of " +
					// card.getName());
					// GameManager.getManager().out[getIndex()].append("\nBoard already has a copy of "
					// + card.getName());
					// GameManager.getManager().out[0].append("\nSIDE NOTE:" +
					// this.getName()+ " already has a copy of " +
					// card.getName());
					continue loop;
				}
			}

			if (freeList.contains(card.getName())) {
				System.out.println("\t is in FreeList");
				options[i] = new CommandOption(card, 0, null, null, null, true,
						true);
				options[i].setReason(" in Free List ");
			} else {
				cardCost = new SimpleResList();
				cardCost.buildCostList(card);
				if (canBuild(card, cardCost)) {
					options[i] = new CommandOption(card, 0, null, null, null,
							true, true);
					System.out.println("\tis buildable");
				} else { // does needs zero out with both neighbors?
					System.out.println("remaining cost " + cardCost);
					int totalCost = cardCost.getTotalRes();

					// no money to buy from neighbors
					if (getTotalCoins() == 0) {
						options[i] = new CommandOption(card, totalCost, null,
								null, null, false, false);
						continue;
					}
					// not enough money to cover all the needed goods
					if (cardCost.getNumRawGoods()
							* (leftRawCost < rightRawCost ? leftRawCost
									: rightRawCost)
							+ cardCost.getNumManfGoods() * this.manfCost > this
								.getTotalCoins()) {
						options[i] = new CommandOption(card, totalCost, null,
								null, null, false, false);
						continue;
					}

					int rightProvide = rightNeighbor.canBuy(new SimpleResList(
							cardCost));
					int leftProvide = leftNeighbor.canBuy(new SimpleResList(
							cardCost));
					System.out.println("East Neighbor could provide "
							+ rightProvide);
					System.out.println("West Neighbor could provide "
							+ leftProvide);
					SimpleResList testCost1 = new SimpleResList(cardCost);
					SimpleResList testCost2 = new SimpleResList(cardCost);
					rightNeighbor.canBuy(testCost1);
					leftNeighbor.canBuy(testCost1);
					if (testCost1.getTotalRes() == 0)
						System.out.println("Needs covered by neighbors RL ");
					leftNeighbor.canBuy(testCost2);
					rightNeighbor.canBuy(testCost2);
					if (testCost1.getTotalRes() == 0) {
						System.out.println("Needs covered by neighbors LR ");

						testCost1 = new SimpleResList(cardCost);
						rightNeighbor.canBuy(testCost1);

						testCost2 = new SimpleResList(cardCost);
						leftNeighbor.canBuy(testCost2);

						SimpleResList rightProv = SimpleResList.subtract(
								cardCost, testCost1);
						SimpleResList leftProv = SimpleResList.subtract(
								cardCost, testCost2);

						options[i] = new CommandOption(card, totalCost,
								cardCost, rightProv, leftProv, true, false);
					} else
						options[i] = new CommandOption(card, totalCost, null,
								null, null, false, false);
				}
			}
		}
		return options;
	} // end of buildCommandOptions method

	// this method will be using the new SRL classes to resolve the question
	/**
	 * Can build.
	 * 
	 * @param bld
	 *            the bld
	 * @param cardCost
	 *            the card cost
	 * @return true, if successful
	 */
	public boolean canBuild(Card bld, SimpleResList cardCost) {
		boolean canAfford = false;

		if (bld.isFreeToBuild()) {
			// System.out.println("\t\tcanBuild: card is free" );
			return true;
		}

		if (cardCost.subtract(resList) == 0)
			return true;

		// use orlist and sellorlist to see if this helps
		ArrayList<SimpleResList> compOrList = new ArrayList<SimpleResList>(
				orList);
		boolean listChanged = true;
		int numMatch;
		while (listChanged) {
			listChanged = false;
			for (SimpleResList srl : compOrList) {
				numMatch = srl.findNumMatches(cardCost);
				if (numMatch == 0) {
					compOrList.remove(srl);
					listChanged = true;
					break;
				}
				if (numMatch == 1) {
					cardCost.subtract(srl);
					compOrList.remove(srl);
					listChanged = true;
					break;
				}
			}
			if (cardCost.getTotalRes() == 0
					|| cardCost.getTotalRes() <= compOrList.size()) {
				return true;
			}
		}
		System.out.println("canBuild:::numOrsLeft " + compOrList.size()
				+ " goodStillneeded: " + cardCost.getTotalRes());

		return canAfford;
	}

	/**
	 * Can build next stage.
	 * 
	 * @return true, if successful
	 */
	public boolean canBuildNextStage() {
		System.out.print("Checking if " + stages[stagesCompleted]
				+ " can be built:");
		SimpleResList costSRL = SimpleResList
				.buildCostList(stages[stagesCompleted]);
		System.out
				.println((canBuildStage(stages[stagesCompleted], costSRL) ? " true "
						: " false "));
		// TODO needs code to see if there is enough money to buy the goods from
		// the neighbors(if they have the goods).....look buildCommandOptions
		return canBuildStage(stages[stagesCompleted], costSRL); // this is only
																// checking to
																// see if this
																// board can
																// afford it
	}

	// this method will be using the new SRL classes to resolve the question
	/**
	 * Can build stage.
	 * 
	 * @param bld
	 *            the bld
	 * @param stageCost
	 *            the stage cost
	 * @return true, if successful
	 */
	public boolean canBuildStage(Stage bld, SimpleResList stageCost) {
		boolean canAfford = false;

		if (stageCost.subtract(resList) == 0)
			return true;

		// use orlist and sellorlist to see if this helps
		ArrayList<SimpleResList> compOrList = new ArrayList<SimpleResList>(
				orList);

		boolean listChanged = true;
		int numMatch;
		while (listChanged) {
			listChanged = false;
			for (SimpleResList srl : compOrList) {
				numMatch = srl.findNumMatches(stageCost);
				if (numMatch == 0) {
					compOrList.remove(srl);
					listChanged = true;
					break;
				}
				if (numMatch == 1) {
					stageCost.subtract(srl);
					compOrList.remove(srl);
					listChanged = true;
					break;
				}
			}
			if (stageCost.getTotalRes() == 0
					|| stageCost.getTotalRes() <= compOrList.size()) {
				return true;
			}
		}
		System.out.println("canBuildStage:::numOrsLeft " + compOrList.size()
				+ " goodStillneeded: " + stageCost.getTotalRes());
		if (!canAfford) {

			System.out.println("\t\t Could buy "
					+ this.leftNeighbor.canBuy(new SimpleResList(stageCost))
					+ " resources from left neighbor");
			System.out.println("\t\t Could buy "
					+ this.rightNeighbor.canBuy(new SimpleResList(stageCost))
					+ " resources from right neighbor");
		}
		return canAfford;
	}

	// this is used by neighbors to see if this board has certain resources
	// available to buy
	// returns the number of goods that can be bought
	/**
	 * Can buy.
	 * 
	 * @param cardCost
	 *            the card cost
	 * @return the int
	 */
	public int canBuy(SimpleResList cardCost) {
		int totalNeeded = cardCost.getTotalRes();
		int numLeft = totalNeeded;

		if ((numLeft = cardCost.subtract(resList)) == 0)
			return totalNeeded;

		ArrayList<SimpleResList> copySellOrList = new ArrayList<SimpleResList>(
				sellOrList);

		boolean listChanged = true;
		int numMatch;
		while (listChanged) {
			listChanged = false;
			for (SimpleResList srl : copySellOrList) {
				numMatch = srl.findNumMatches(cardCost);
				if (numMatch == 0) {
					copySellOrList.remove(srl);
					listChanged = true;
					break;
				}
				if (numMatch == 1) {
					// int dif = cardCost.getTotalRes();
					numLeft = cardCost.subtract(srl);
					copySellOrList.remove(srl);
					listChanged = true;
					break;
				}
			}
			if (cardCost.getTotalRes() == 0
					|| cardCost.getTotalRes() <= copySellOrList.size()) {
				return totalNeeded;
			}
		}
		return totalNeeded - numLeft;
	}

	/**
	 * Gets the number completed stages.
	 * 
	 * @return the number completed stages
	 */
	public int getNumberCompletedStages() {
		return this.stagesCompleted;
	}

	/**
	 * Gets the resource list.
	 * 
	 * @return the resource list
	 */
	public SimpleResList getResourceList() {
		return new SimpleResList(resList);
	}

	/**
	 * Gets the sell or list.
	 * 
	 * @return the sell or list
	 */
	public ArrayList<SimpleResList> getSellOrList() {
		return new ArrayList<SimpleResList>(sellOrList); // may be shallow copy
	}

	/**
	 * Gets the color count.
	 * 
	 * @param crdClr
	 *            the crd clr
	 * @return the color count
	 */
	public int getColorCount(CardColor crdClr) {
		int count = 0;
		if (colorCount.containsKey(crdClr)) {
			count = colorCount.get(crdClr);
		}
		return count;
	}

	/**
	 * Adds the to coins.
	 * 
	 * @param more
	 *            the more
	 */
	public void addToCoins(int more) {
		int coins = more;
		if (goods.containsKey(Resource.COIN)) {
			coins = goods.get(Resource.COIN);
			coins += more;
		}
		goods.put(Resource.COIN, coins);
	}

	/**
	 * Buy resources.
	 * 
	 * @param coins
	 *            the coins
	 */
	public void buyResources(int coins) {
		turnsResourceIncome += coins;
	}

	/**
	 * Adds the turn sales.
	 */
	void addTurnSales() {
		if (this.turnsResourceIncome > 0)
			addToCoins(this.turnsResourceIncome);
		turnsResourceIncome = 0;
	}

	/**
	 * Adds the to v ps.
	 * 
	 * @param more
	 *            the more
	 */
	public void addToVPs(int more) {
		int vps = more;
		if (goods.containsKey(Resource.VP)) {
			vps = goods.get(Resource.VP);
			vps += more;
		}
		goods.put(Resource.VP, vps);
	}

	/**
	 * Gets the number of defeats.
	 * 
	 * @return the number of defeats
	 */
	public int getNumberOfDefeats() {
		int numDefeats = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 1; j <= 3; j++) {
				if (militaryVPS[i][j] == -1)
					numDefeats++;
			}
		}
		return numDefeats;
	}

	/**
	 * Gets the player.
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player.
	 * 
	 * @param plyer
	 *            the new player
	 */
	public void setPlayer(Player plyer) {
		player = plyer;
	}

	/**
	 * Gets the left neighbor.
	 * 
	 * @return the left neighbor
	 */
	public Board getLeftNeighbor() {
		return leftNeighbor;
	}

	/**
	 * Sets the left neighbor.
	 * 
	 * @param leftNeighbor
	 *            the new left neighbor
	 */
	public void setLeftNeighbor(Board leftNeighbor) {
		this.leftNeighbor = leftNeighbor;
	}

	/**
	 * Gets the right neighbor.
	 * 
	 * @return the right neighbor
	 */
	public Board getRightNeighbor() {
		return rightNeighbor;
	}

	/**
	 * Sets the right neighbor.
	 * 
	 * @param rightNeighbor
	 *            the new right neighbor
	 */
	public void setRightNeighbor(Board rightNeighbor) {
		this.rightNeighbor = rightNeighbor;
	}

	/**
	 * Sets the stage.
	 * 
	 * @param stg
	 *            the new stage
	 */
	public void setStage(Stage stg) {
		stages[stg.stageNum - 1] = stg;
	}

	/**
	 * Score v ps.
	 * 
	 * @return the int
	 */
	public int scoreVPs() {
		int[] symbols = new int[3];
		int numSets, total = 0;
		if (goods.containsKey(Resource.COG))
			symbols[0] = goods.get(Resource.COG);
		else
			symbols[0] = 0;
		if (goods.containsKey(Resource.COMPASS))
			symbols[1] = goods.get(Resource.COMPASS);
		else
			symbols[1] = 0;
		if (goods.containsKey(Resource.TABLET))
			symbols[2] = goods.get(Resource.TABLET);
		else
			symbols[2] = 0;
		numSets = symbols[0];
		total = symbols[0] * symbols[0];
		for (int i = 1; i < 3; i++) {
			if (symbols[i] < numSets)
				numSets = symbols[i];
			total += symbols[i] * symbols[i];
		}
		total += numSets * 7;
		return total;
	}

	// for player to call to get info on SCISYM points
	// index 0 = cog, 1 = compass, 2 = tablet
	/**
	 * Score v ps.
	 * 
	 * @param symbols
	 *            the symbols
	 * @return the int
	 */
	public static int scoreVPs(int[] symbols) {
		int numSets, total = 0;
		numSets = symbols[0];
		total = symbols[0] * symbols[0];
		for (int i = 1; i < 3; i++) {
			if (symbols[i] < numSets)
				numSets = symbols[i];
			total += symbols[i] * symbols[i];
		}
		total += numSets * 7;
		return total;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String str = brdName;

		// str += "\nSRL:" + resList + "\n";
		// str += "orList: " + orList + "\n";
		// str += "sellOrList: " + sellOrList + "\n";

		Set<Resource> goodsSet = goods.keySet();

		if (goodsSet.isEmpty())
			str += " Provides no resources ";
		else {
			str += " Provides: ";
			for (Resource r : goodsSet) {
				str += goods.get(r) + " " + r.toString() + "  ";
			}
		}
		str += "\n";
		return str;
	}

	/**
	 * Gets the total coins.
	 * 
	 * @return the total coins
	 */
	public int getTotalCoins() {
		return goods.get(Resource.COIN);
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return brdName;
	}

	/**
	 * Discard card.
	 * 
	 * @param card
	 *            the card
	 */
	private void discardCard(Card card) {
		GameManager.getManager().getCardManager().discardCard(card);
	}

}

/*
 * 
 * @SuppressWarnings("unchecked") // this one uses the old complicated way with
 * the EnumMaps public boolean canBuildOldWay(Card bld){ Set<Resource> costSet =
 * null; EnumMap<Resource,Integer> resNeededMap = new
 * EnumMap<Resource,Integer>(Resource.class) ; costSet = bld.getCosts(); boolean
 * canAfford = true; int numResNeeded, resProvided; for(Resource res: costSet ){
 * // calculate resources needed if( res == Resource.FREE ){ //
 * System.out.println("\tis free to build " ); return true; } numResNeeded =
 * bld.costCnt(res); resProvided = ( goods.containsKey(res) ? goods.get(res) : 0
 * ); if( numResNeeded > resProvided ){ int buyNum = numResNeeded - resProvided;
 * canAfford = false; // System.out.println("\tnot enough " + res.toString() +
 * ": needs " + buyNum + " more"); resNeededMap.put(res, buyNum); } }
 * 
 * if( canAfford ) return true;
 * 
 * 
 * //###################################### // Remove unneeded OR cards
 * //###################################### ArrayList<Card> copyOrCards = (
 * ArrayList<Card> ) orCards.clone(); boolean resNeeded; int nrNeeded; //
 * System.out.println("copyOrCards size before: " + copyOrCards.size());
 * ArrayList<Card> removeList = new ArrayList<Card>(); Resource[] resArray;
 * for(Card c : copyOrCards){ // System.out.println(c + " is in copyOrCards");
 * resNeeded = false; nrNeeded = 0; for(Resource r : c.getGoods() ){ resArray =
 * r.getOptionalRes(); for (Resource res : resArray ){ // System.out.println(res
 * + " checking if needed "); if(resNeededMap.containsKey(res)){ resNeeded =
 * true; nrNeeded++; // System.out.println(c + " is needed "); } } if( resNeeded
 * == false) removeList.add(c); } // use card to reduce resources needed
 * for(Resource r : c.getGoods() ){ resArray = r.getOptionalRes(); for (Resource
 * res : resArray ){ // System.out.println(res + " checking if needed ");
 * if(resNeededMap.containsKey(res)){ resNeededMap.put(res,
 * resNeededMap.get(res)-1); if (resNeededMap.get(res) == 0)
 * resNeededMap.remove(res); removeList.add(c); System.out.println(c +
 * " is being used for " + res); } }
 * 
 * } }
 * 
 * if( ! removeList.isEmpty() ) for( Card c : removeList) copyOrCards.remove(c);
 * 
 * 
 * // System.out.println("copyOrCards size after: " + copyOrCards.size());
 * 
 * 
 * if (resNeededMap.isEmpty()) canAfford = true; else for(Resource r :
 * resNeededMap.keySet()){ System.out.println(r + " still needs: " +
 * resNeededMap.get(r)); } return canAfford; }
 */