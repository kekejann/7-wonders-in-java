package org.tjuscs.sevenwonders.core;

import java.util.Random;

/**
 * A factory for creating Board objects.
 */
public class BoardFactory {

	/**
	 * Make a side boards.
	 * 
	 * @param num
	 *            the number of boards
	 * @return the board[]
	 */
	public static Board[] makeASideBoards(int num) {
		Board[] boardPile = new Board[7];
		boardPile[0] = BoardInfo.ALEXANDRIA_A.buildBoard();
		boardPile[1] = BoardInfo.BABYLON_A.buildBoard();
		boardPile[2] = BoardInfo.EPHESUS_A.buildBoard();
		boardPile[3] = BoardInfo.GIZA_A.buildBoard();
		boardPile[4] = BoardInfo.HALICARNASSUS_A.buildBoard();
		boardPile[5] = BoardInfo.OYLMPIA_A.buildBoard();
		boardPile[6] = BoardInfo.RHODES_A.buildBoard();

		shuffle(boardPile);
		Board[] returnBoards = new Board[num];
		for (int i = 0; i < returnBoards.length; i++) {
			returnBoards[i] = boardPile[i];
		}
		return returnBoards;
	}

	/**
	 * Make b side boards.
	 * 
	 * @param num
	 *            the number of boards
	 * @return the board[]
	 */
	public static Board[] makeBSideBoards(int num) {
		Board[] boardPile = new Board[7];
		boardPile[0] = BoardInfo.ALEXANDRIA_B.buildBoard();
		boardPile[1] = BoardInfo.BABYLON_B.buildBoard();
		boardPile[2] = BoardInfo.EPHESUS_B.buildBoard();
		boardPile[3] = BoardInfo.GIZA_B.buildBoard();
		boardPile[4] = BoardInfo.HALICARNASSUS_B.buildBoard();
		boardPile[5] = BoardInfo.OYLMPIA_B.buildBoard();
		boardPile[6] = BoardInfo.RHODES_B.buildBoard();

		shuffle(boardPile);
		Board[] returnBoards = new Board[num];
		for (int i = 0; i < returnBoards.length; i++) {
			returnBoards[i] = boardPile[i];
		}
		return returnBoards;
	}

	/**
	 * Make A and B side boards.
	 * 
	 * @param num
	 *            the number of boards
	 * @return the board[]
	 */
	public static Board[] makeAandBSideBoards(int num) {
		Board[] boardPile = new Board[7];
		Random rndGen = new Random(System.currentTimeMillis());

		boardPile[0] = (rndGen.nextInt(100) <= 50 ? BoardInfo.ALEXANDRIA_A
				.buildBoard() : BoardInfo.ALEXANDRIA_B.buildBoard());
		boardPile[1] = (rndGen.nextInt(100) <= 50 ? BoardInfo.BABYLON_A
				.buildBoard() : BoardInfo.BABYLON_B.buildBoard());
		boardPile[2] = (rndGen.nextInt(100) <= 50 ? BoardInfo.EPHESUS_A
				.buildBoard() : BoardInfo.EPHESUS_B.buildBoard());
		boardPile[3] = (rndGen.nextInt(100) <= 50 ? BoardInfo.GIZA_A
				.buildBoard() : BoardInfo.GIZA_B.buildBoard());
		boardPile[4] = (rndGen.nextInt(100) <= 50 ? BoardInfo.HALICARNASSUS_A
				.buildBoard() : BoardInfo.HALICARNASSUS_B.buildBoard());
		boardPile[5] = (rndGen.nextInt(100) <= 50 ? BoardInfo.OYLMPIA_A
				.buildBoard() : BoardInfo.OYLMPIA_B.buildBoard());
		boardPile[6] = (rndGen.nextInt(100) <= 50 ? BoardInfo.RHODES_A
				.buildBoard() : BoardInfo.RHODES_B.buildBoard());

		shuffle(boardPile);
		Board[] returnBoards = new Board[num];
		for (int i = 0; i < returnBoards.length; i++) {
			returnBoards[i] = boardPile[i];
		}
		return returnBoards;
	}

	/**
	 * Shuffle.
	 * 
	 * @param brds
	 *            the boards
	 */
	private static void shuffle(Board[] brds) {
		Board temp;
		int randNum;
		Random rndGen = new Random(System.currentTimeMillis());
		for (int sh = 0; sh < 4; sh++)
			for (int i = 0; i < brds.length; i++) {
				temp = brds[i];
				randNum = rndGen.nextInt(brds.length);
				brds[i] = brds[randNum];
				brds[randNum] = temp;
			}
	}
}
