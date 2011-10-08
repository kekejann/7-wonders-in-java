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
	 * �غϽ���ʱ����ʱ�����б�
	 */
	ArrayList<DelayedAction> EOTurnDelayedActionList;

	/**
	 * The end-Of-turn remove list. <br>
	 * �غϽ���ʱ����ʱ��������б�
	 */
	ArrayList<DelayedAction> EOTurnRemoveList;

	/**
	 * The end-of-age delayed action list. <br>
	 * ʱ������ʱ����ʱ�����б�
	 */
	List<DelayedAction> EOAgeDelayedActionList;

	/**
	 * The end-of-game delayed action list. <br>
	 * ��Ϸ����ʱ����ʱ�����б�
	 */
	List<DelayedAction> EOGameDelayedActionList;

	/**
	 * The reference to an object of GameManager class. <br>
	 * GameManager���ʵ��������
	 */
	private static GameManager gm;

	/**
	 * The card manager. <br>
	 * CardManager���ʵ��������
	 */
	CardManager cardManager;

	/**
	 * The players. <br>
	 * Player���ʵ��������
	 */
	Player[] players;

	/**
	 * The boards.<br>
	 * Board���ʵ��������
	 */
	Board[] boards;

	/**
	 * The hands.<br>
	 * Hand���ʵ��������
	 */
	Hand[] hands;

	/**
	 * The number of players. <br>
	 * �������
	 */
	int numPlayers;

	/**
	 * The testing output. <br>
	 * �������
	 */
	public StringBuilder[] out;

	/**
	 * The Constant LEFT & RIGHT. <br>
	 * ��������LEFT��RIGHT
	 */
	public static final int LEFT = 0, RIGHT = 1;

	/**
	 * Build a GameManager object and get the reference to it.<br>
	 * �½�GameManager��Ķ��󲢻�������á�
	 * 
	 * @return the reference to the GameManager object<br>
	 *         GameManager���������
	 */
	public static GameManager getManager() {
		if (gm == null)
			gm = new GameManager();
		return gm;
	}

	/**
	 * Instantiates a new game manager�� it makes four lists of DelayedAction.<br>
	 * GameManager���캯��,�������ĸ���ʱ�����б�
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
	 * ��ʼ��Ϸ
	 * 
	 * @param numPlyers
	 *            the number of players. �������
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
	 * ��ȡCardManager
	 * 
	 * @return the reference to the CardManager object.<br>
	 *         CardManager���������
	 */
	public CardManager getCardManager() {
		return cardManager;
	}

	/**
	 * Adds the end-of-turn delayed action.<br>
	 * ��ӻغϽ���ʱ����ʱ����
	 * 
	 * @param da
	 *            the delayed action. ��ʱ����
	 */
	public void addEOTDelayedAction(DelayedAction da) {
		EOTurnDelayedActionList.add(da);
	}

	/**
	 * Adds the end-of-age delayed action.<br>
	 * ���ʱ������ʱ����ʱ����
	 * 
	 * @param da
	 *            the delayed action. ��ʱ����
	 */
	public void addEOADelayedAction(DelayedAction da) {
		EOAgeDelayedActionList.add(da);
	}

	/**
	 * Adds the end-of-game delayed action.<br>
	 * �����Ϸ����ʱ����ʱ����
	 * 
	 * @param da
	 *            the delayed action. ��ʱ����
	 */
	public void addEOGDelayedAction(DelayedAction da) {
		EOGameDelayedActionList.add(da);
	}

	/**
	 * Removes the end-of-turn delayed action.<br>
	 * ɾ���غϽ���ʱ����ʱ����
	 * 
	 * @param da
	 *            the delayed action. ��ʱ����
	 */
	public void removeEOTDelayedAction(DelayedAction da) {
		EOTurnRemoveList.add(da);
	}

	/**
	 * Removes the end-of-age delayed action.<br>
	 * ɾ��ʱ������ʱ����ʱ����
	 * 
	 * @param da
	 *            the delayed action.��ʱ����
	 */
	public void removeEOADelayedAction(DelayedAction da) {
		EOAgeDelayedActionList.remove(da);
	}

	/**
	 * Removes the end-of-game delayed action.<br>
	 * ɾ����Ϸ����ʱ����ʱ����
	 * 
	 * @param da
	 *            the delayed action.��ʱ����
	 */
	public void removeEOGDelayedAction(DelayedAction da) {
		EOGameDelayedActionList.remove(da);
	}

	/**
	 * Start an age.<br>
	 * ��ʼһ��ʱ��
	 * 
	 * @param ageNum
	 *            the age number.ʱ�����(1/2/3)
	 */
	public void startAge(int ageNum) {
		hands = cardManager.setupHands(ageNum);
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
	 * ��ʼһ���غ�
	 * 
	 * @param trnNum
	 *            the turn number. �غ����
	 */
	public void startTurn(int trnNum) {
		int ind = -1;
		System.out.println("\nTurn " + trnNum);

		for (Board board : boards) {
			out[ind + 1].append("\nTurn " + trnNum);
			out[ind + 1].append("\n" + board);
			System.out.println("\n\n" + board);
			board.takeTurn(hands[(ind + trnNum) % numPlayers], trnNum);
			ind++;
			// out[ind].append("\n" + board );
		}
	}

	/**
	 * Do end of turn.<br>
	 * �غϽ���ʱ�Ľ���
	 * 
	 * @param trnNum
	 *            the turn number.�غ����
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
	 * ʱ������ʱ�Ľ���
	 * 
	 * @param ageNum
	 *            the age number��ʱ�����
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
	 * ��Ϸ����ʱ�Ľ���
	 */
	public void doEndOfGame() {
		for (DelayedAction da : EOGameDelayedActionList) {
			da.doDelayedAction();
		}
		// TODO do Victory Calculations

	} // end of doEndOfGame method

	/**
	 * The main method.<br>
	 * ���������������
	 * 
	 * @param args
	 *            the arguments.�����в����б�
	 */
	public static void main(String[] args) {
		GameManager gm = GameManager.getManager();
		gm.startGame(4);

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
			}
		}

		gm.doEndOfGame();

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
		}

		// display one of the board's game history
		System.out.println(gm.out[0].toString());

	} // end of GameManager.main

}
