package org.tjuscs.sevenwonders.core;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GameManager.
 * 
 */
public class GameManager {

	/**
	 * The end-of-turn delayed action list.<br>
	 * 回合结束时的延时动作列表
	 */
	ArrayList<DelayedAction> EOTurnDelayedActionList;

	/**
	 * The end-Of-turn remove list. <br>
	 * 回合结束时的延时动作清除列表
	 */
	ArrayList<DelayedAction> EOTurnRemoveList;

	/**
	 * The end-of-age delayed action list. <br>
	 * 时代结束时的延时动作列表
	 */
	List<DelayedAction> EOAgeDelayedActionList;

	/**
	 * The end-of-game delayed action list. <br>
	 * 游戏结束时的延时动作列表
	 */
	List<DelayedAction> EOGameDelayedActionList;

	/**
	 * The reference to an object of GameManager class. <br>
	 * GameManager类的实例的引用
	 */
	private static GameManager gm;

	/**
	 * The card manager. <br>
	 * 卡牌的管理类。
	 */
	CardManager cardManager;

	/**
	 * The players. <br>
	 * 存储所有玩家的数组。
	 */
	Player[] players;

	/**
	 * The boards.<br>
	 * 存储所有奇迹板的数组。
	 */
	Board[] boards;

	/**
	 * The hands.<br>
	 * 存储手牌堆的数组。
	 */
	Hand[] hands;

	/**
	 * The number of players. <br>
	 * 玩家数量
	 */
	int numPlayers;

	/**
	 * The testing output. <br>
	 * 存储测试输出的数组
	 */
	public StringBuilder[] out;

	/**
	 * The Constant LEFT & RIGHT. <br>
	 * 两个常量LEFT和RIGHT
	 */
	public static final int LEFT = 0, RIGHT = 1;

	/**
	 * Build a GameManager object and get the reference to it.<br>
	 * 新建GameManager类的对象并获得其引用。
	 * 
	 * @return the reference to the GameManager object<br>
	 *         GameManager对象的引用
	 */
	public static GameManager getManager() {
		if (gm == null)
			gm = new GameManager();
		return gm;
	}

	/**
	 * Instantiates a new game manager， it makes four lists of DelayedAction.<br>
	 * GameManager构造函数,建立了四个延时动作列表。
	 * 
	 * @see DelayedAction
	 */
	private GameManager() {
		EOTurnDelayedActionList = new ArrayList<DelayedAction>();
		EOAgeDelayedActionList = new ArrayList<DelayedAction>();
		EOGameDelayedActionList = new ArrayList<DelayedAction>();
		EOTurnRemoveList = new ArrayList<DelayedAction>();
	}

	/**
	 * Start the game.<br>
	 * 开始游戏
	 * <p>
	 * Set number of players, make a CardManager, make Players, make Boards, and
	 * set the built-in AI player and two neighbors for every board.(Including
	 * make a StringBuilder array for debug output)<br>
	 * 设置玩家数量，根据人数新建一个CardManager、对应数量的Player和Board，并且确定每个Board两边相邻的Board，
	 * 最后设置每个Board的玩家为内置AI.（还包括建立一个StringBuilder对象数组来管理调试输出）
	 * 
	 * @param numPlyers
	 *            the number of players. 玩家数量
	 */
	public void startGame(int numPlyers) {
		this.numPlayers = numPlyers;
		cardManager = new CardManager(numPlayers);
		players = new Player[numPlayers];
		boards = BoardFactory.makeAandBSideBoards(numPlayers);

		out = new StringBuilder[numPlayers];

		for (int i = 0; i < out.length; i++) {
			out[i] = new StringBuilder();
			boards[i].setIndex(i);
		}

		boards[0].setLeftNeighbor(boards[numPlayers - 1]);
		boards[0].setRightNeighbor(boards[1]);

		for (int i = 1; i < boards.length - 1; i++) {
			boards[i].setLeftNeighbor(boards[i - 1]);
			boards[i].setRightNeighbor(boards[i + 1]);
		}

		boards[numPlayers - 1].setLeftNeighbor(boards[numPlayers - 2]);
		boards[numPlayers - 1].setRightNeighbor(boards[0]);
		Player player;
		for (Board board : boards) {
			player = new RandBot();
			player.setBoard(board);
			board.setPlayer(player);
		}
	}

	/**
	 * Gets the card manager.<br>
	 * 获取CardManager
	 * 
	 * @return the reference to the CardManager object.<br>
	 *         CardManager对象的引用
	 */
	public CardManager getCardManager() {
		return cardManager;
	}

	/**
	 * Adds the end-of-turn delayed action.<br>
	 * 添加回合结束时的延时动作
	 * 
	 * @param da
	 *            the delayed action. 延时动作
	 */
	public void addEOTDelayedAction(DelayedAction da) {
		EOTurnDelayedActionList.add(da);
	}

	/**
	 * Adds the end-of-age delayed action.<br>
	 * 添加时代结束时的延时动作
	 * 
	 * @param da
	 *            the delayed action. 延时动作
	 */
	public void addEOADelayedAction(DelayedAction da) {
		EOAgeDelayedActionList.add(da);
	}

	/**
	 * Adds the end-of-game delayed action.<br>
	 * 添加游戏结束时的延时动作
	 * 
	 * @param da
	 *            the delayed action. 延时动作
	 */
	public void addEOGDelayedAction(DelayedAction da) {
		EOGameDelayedActionList.add(da);
	}

	/**
	 * Removes the end-of-turn delayed action.<br>
	 * 删除回合结束时的延时动作
	 * 
	 * @param da
	 *            the delayed action. 延时动作
	 */
	public void removeEOTDelayedAction(DelayedAction da) {
		EOTurnRemoveList.add(da);
	}

	/**
	 * Removes the end-of-age delayed action.<br>
	 * 删除时代结束时的延时动作
	 * 
	 * @param da
	 *            the delayed action.延时动作
	 */
	public void removeEOADelayedAction(DelayedAction da) {
		EOAgeDelayedActionList.remove(da);
	}

	/**
	 * Removes the end-of-game delayed action.<br>
	 * 删除游戏结束时的延时动作
	 * 
	 * @param da
	 *            the delayed action.延时动作
	 */
	public void removeEOGDelayedAction(DelayedAction da) {
		EOGameDelayedActionList.remove(da);
	}

	/**
	 * Start an age.<br>
	 * 开始一个时代
	 * <p>Get the hand decks for this age<br>
	 * 获取此时代的手牌。
	 * @param ageNum
	 *            the age number.时代序号(1/2/3)
	 */
	public void startAge(int ageNum) {
		hands = cardManager.setupHands(ageNum);

		// debug output
		System.out.println("Start Age " + ageNum + "\n");
		for (StringBuilder sb : out) {
			sb.append("\nStart Age " + ageNum + "\n");
			// sb.append(b)
		}
		// for(Hand hand: hands){
		// System.out.println(hand);
		// }
	}

	/**
	 * Start a turn.<br>
	 * 开始一个回合
	 * <p>Pass the hand decks(clockwise/counter-clockwise) to the corresponding boards, and ask every board to take its turn one by one.<br>
	 * (按顺时针或逆时针)将手牌传给相应的奇迹，并逐个地请求奇迹来完成该回合。 
	 * @param trnNum
	 *            the turn number. 回合序号
	 */
	public void startTurn(int trnNum) {
		int ind = -1;
		System.out.println("\nTurn " + trnNum);

		for (Board board : boards) {
			out[ind + 1].append("\nTurn " + trnNum);
			out[ind + 1].append("\n" + board);
			System.out.println("\n\n" + board);
			
			board.takeTurn(hands[(ind + trnNum) % numPlayers], trnNum);	//Q: What if Age 2 ?
			
			ind++;
			// out[ind].append("\n" + board );
		}
	}

	/**
	 * Do end of turn.<br>
	 * 回合结束时的结算
	 * <p>Add the income of every board into their treasury<br>
	 * 各方将该回合的收入加入各自的金库中
	 * @param trnNum
	 *            the turn number.回合序号
	 */
	public void doEndOfTurn(int trnNum) {
		for (Board board : boards) {
			board.addTurnSales();
		}

		for (DelayedAction da : EOTurnDelayedActionList) {
			da.doDelayedAction();
		}

		for (DelayedAction da : EOTurnRemoveList) {
			EOTurnDelayedActionList.remove(da);
		}
	}

	/**
	 * Do end of age.<br>
	 * 时代结束时的结算
	 * 
	 * @param ageNum
	 *            the age number。时代序号
	 */
	public void doEndOfAge(int ageNum) {
		int[] victVP = { 0, 1, 3, 5 };

		for (DelayedAction da : EOAgeDelayedActionList) {
			da.doDelayedAction();
		}

		for (Board board : boards) {
			int ourSheild = (board.goods.containsKey(Resource.SHEILD) ? board.goods
					.get(Resource.SHEILD) : 0);
			int lftNghborSheild = (board.getLeftNeighbor().goods
					.containsKey(Resource.SHEILD) ? board.getLeftNeighbor().goods
					.get(Resource.SHEILD) : 0);
			int rghtNghborSheild = (board.getRightNeighbor().goods
					.containsKey(Resource.SHEILD) ? board.getRightNeighbor().goods
					.get(Resource.SHEILD) : 0);
			if (ourSheild > lftNghborSheild)
				board.militaryVPS[LEFT][ageNum] = victVP[ageNum];
			else if (ourSheild < lftNghborSheild) {
				board.militaryVPS[LEFT][ageNum] = -1;
			}
			if (ourSheild > rghtNghborSheild)
				board.militaryVPS[RIGHT][ageNum] = victVP[ageNum];
			else if (ourSheild < rghtNghborSheild) {
				board.militaryVPS[RIGHT][ageNum] = -1;
			}
			board.addToVPs(board.militaryVPS[LEFT][ageNum]);
			board.addToVPs(board.militaryVPS[RIGHT][ageNum]);
		}

	} // end of doEndOfAge method

	/**
	 * Do end of game.<br>
	 * 游戏结束时的结算
	 */
	public void doEndOfGame() {
		for (DelayedAction da : EOGameDelayedActionList) {
			da.doDelayedAction();
		}
		// TODO do Victory Calculations

	} // end of doEndOfGame method

	/**
	 * The main method.<br>
	 * 主函数，程序入口
	 * 
	 * @param args
	 *            the arguments.命令行参数列表
	 */
	public static void main(String[] args) {
		GameManager gm = GameManager.getManager();
		gm.startGame(4);

		// Set the player of the first board as the Human Player
		Player player = new HumanDialogBasedPlayer();
		player.setBoard(gm.boards[0]);
		gm.boards[0].setPlayer(player);

		// Player player;
		for (Board b : gm.boards) {
			// player = new RandBot(b);
			System.out.print(b);
			System.out.println(b.stages[0].name + "\n");
			// b.setPlayer(player);
			// System.out.println( "\t" + b.getLeftNeighbor().getName() +
			// "      " + b.getRightNeighbor().getName() + "\n");
		}

		// The age loop
		for (int age = 1; age <= 3; age++) {

			gm.startAge(age);
			for (int trn = 1; trn < 7; trn++) {
				gm.startTurn(trn);
				gm.doEndOfTurn(trn);
			}
			gm.doEndOfAge(age);

			System.out.println("\n\nAfter Age " + age);
			for (StringBuilder sb : gm.out) {
				sb.append("\n\nAfter Age " + age);
			}

			// Debug output
			int ind = 0;
			for (Board b : gm.boards) {
				System.out.print(b);
				System.out
						.print("Can Build Stage: "
								+ b.canBuildStage(b.stages[b.stagesCompleted],
										new SimpleResList(
												b.stages[b.stagesCompleted])));
				System.out.println("  Cost: "
						+ b.stages[b.stagesCompleted].cost);
				System.out.println("\tLeftMilVP: " + b.militaryVPS[LEFT][age]
						+ "   RghtMilVP: " + b.militaryVPS[RIGHT][age] + "\n");
				gm.out[ind].append("\n" + b);
				gm.out[ind]
						.append("\tCan Build Stage: "
								+ b.canBuildStage(b.stages[b.stagesCompleted],
										new SimpleResList(
												b.stages[b.stagesCompleted])));
				gm.out[ind].append("\t\t Stage Cost: "
						+ b.stages[b.stagesCompleted]);
				gm.out[ind].append("\n\tLeftMilVP: " + b.militaryVPS[LEFT][age]
						+ "   RghtMilVP: " + b.militaryVPS[RIGHT][age] + "\n");
				for (Card card : b.structures) {
					gm.out[ind].append("\n\t" + card);
					System.out.println("\t" + card);
				}
				gm.out[ind].append("\n");
				System.out.println("\n");
				ind++;
			}// End of debug output
		}// End of the age loop

		gm.doEndOfGame();

		// Show game result
		int ind = 0;
		String[] dirStr = { "vs Left Neighbor : ", "vs Right Neighbor: " };
		int totalMilVPs = 0, oneNghbr = 0;
		System.out.println("\nEnd of Game Scoring");
		System.out.println("\n");
		for (Board b : gm.boards) {
			System.out.print(b);
			gm.out[ind].append("\n\nEnd of Game Scoring");
			gm.out[ind].append("\n");
			gm.out[ind].append("\n" + b);
			totalMilVPs = 0;
			for (int side = 0; side < 2; side++) {
				System.out.print("\t" + dirStr[side]);
				gm.out[ind].append("\t" + dirStr[side]);
				oneNghbr = 0;
				for (int i = 1; i < 4; i++) {
					System.out.print("\t" + b.militaryVPS[side][i]);
					gm.out[ind].append("\t" + b.militaryVPS[side][i]);
					oneNghbr += b.militaryVPS[side][i];
				}
				totalMilVPs += oneNghbr;
				gm.out[ind].append("\t <" + oneNghbr + ">\n");
				System.out.print("\t <" + oneNghbr + ">\n");
			}
			gm.out[ind].append("\tTotal Military Score: " + totalMilVPs + "\n");
			System.out.print("\tTotal Military Score: " + totalMilVPs + "\n");
			int sciScore = b.scoreVPs();
			System.out.println("\tScience: " + sciScore);
			System.out.println("\tTotal Score: "
					+ (sciScore + b.goods.get(Resource.VP)));
			gm.out[ind].append("\n\tScience: " + sciScore);
			gm.out[ind].append("\n\tTotal Score: "
					+ (sciScore + b.goods.get(Resource.VP)));

			gm.out[ind].append("\n");
			System.out.println("\n");
			ind++;
		}// End of show of game result

		// display one of the board's game history
		System.out.println(gm.out[0].toString());

	} // end of GameManager.main

}
