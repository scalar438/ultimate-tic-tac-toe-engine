package com.theaigames.tictactoe;

import java.io.IOException;

import com.theaigames.engine.io.IOPlayer;
import com.theaigames.game.player.AbstractPlayer;

public class Field {
	
	private int[][] mBoard;
	private int[][] mMacroboard;

	private int mCols = 0, mRows = 0;
	private String mLastError = "";
	private int mLastX = 0, mLastY = 0;
	private Boolean mAllMicroboardsActive = true;
	
	public Field() {
		mCols = 9;
		mRows = 9;
		mBoard = new int[mCols][mRows];
		mMacroboard = new int[mCols / 3][mRows / 3];
		clearBoard();
	}
	
	public void clearBoard() {
		for (int x = 0; x < mCols; x++) {
			for (int y = 0; y < mRows; y++) {
				mBoard[x][y] = 0;
			}
		}
	}
	
	public void dumpBoard() {
		System.out.print("\n\n");
		for (int y = 0; y < mRows; y++) {
			for (int x = 0; x < mCols; x++) {
				System.out.print(mBoard[x][y]);
				if (x < mCols-1) {
					String s = ", ";
					if (x % 3 == 2) {
						s = "| ";
					}
					System.out.print(s);
				}
			}
			if (y % 3 == 2) {
				System.out.print("\n");
				for (int x = 0; x < mCols-1; x++) {
					System.out.print("---");
				}
			}
			System.out.print("\n");
		}
		System.out.print("Macroboard:\n");
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {

				System.out.print(mMacroboard[x][y]);
			}
			System.out.print("\n");
		}
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}
	
	/**
	 * Adds a move to the board
	 * @param args : int x, int y, int move
	 * @return : true if disc fits, otherwise false
	 */
	public Boolean addMove(int x, int y, int move) {
		mLastError = "";
		mLastX = x;
		mLastY = y;
		if (x < mCols && y < mRows && x >= 0 && y >= 0) { /* Move within range */
			if (isInActiveMicroboard(x, y)) { /* Move in active microboard */
				if (mBoard[x][y] == 0) { /*Field is available */
					mBoard[x][y] = move;
					mLastX = x;
					mLastY = y;
					mAllMicroboardsActive = false;
					updateMacrofields();
					return true;
				}
				mLastError = "Position is full.";
			} else {
				mLastError = "Not in active macroboard.";
			}
		} else {
			mLastError = "Move out of bounds.";
		}
		return false;
	}
	
	public Boolean isInActiveMicroboard(int x, int y) {
		if (mAllMicroboardsActive) { return true; }
		return (x/3 == getActiveMicroboardX() && y/3 == getActiveMicroboardY()); 
	}
	
	public int getActiveMicroboardX() {
		if (mAllMicroboardsActive) return -1;
		return mLastX%3;
	}
	
	public int getActiveMicroboardY() {
		if (mAllMicroboardsActive) return -1;
		return mLastY%3;
	}
	
	/**
	 * Returns reason why addMove returns false
	 * @param args : 
	 * @return : reason why addMove returns false
	 */
	public String getLastError() {
		return mLastError;
	}
	
	/**
	 * Returns last inserted column
	 * @param args : 
	 * @return : last inserted column
	 */
	public int getLastX() {
		return mLastX;
	}
	
	/**
	 * Returns last inserted row
	 * @param args : 
	 * @return : last inserted row
	 */
	public int getLastY() {
		return mLastY;
	}
	
	@Override
	/**
	 * Creates comma separated String with player names for every cell.
	 * @param args : 
	 * @return : String with player names for every cell, or 'empty' when cell is empty.
	 */
	public String toString() {
		String r = "";
		int counter = 0;
		for (int y = 0; y < mRows; y++) {
			for (int x = 0; x < mCols; x++) {
				if (counter > 0) {
					r += ",";
				}
				r += mBoard[x][y];
				counter++;
			}
		}
		return r;
	}
	
	/**
	 * Checks whether the field is full
	 * @param args : 
	 * @return : Returns true when field is full, otherwise returns false.
	 */
	public boolean isFull() {
		for (int x = 0; x < mCols; x++)
		  for (int y = 0; y < mRows; y++)
		    if (mBoard[x][y] == 0)
		      return false; // At least one cell is not filled
		// All cells are filled
		return true;
	}
	
	/**
	 * Checks if there is a winner, if so, returns player id.
	 * @param args : 
	 * @return : Returns player id if there is a winner, otherwise returns 0.
	 */
	public int getWinner() {
		updateMacrofields();
		return checkMacroboardWinner();
	}
	
	/**
	 * Checks the microboards for wins and updates internal representation (macroboard)
	 * @param args : 
	 * @return : 
	 */
	public void updateMacrofields() {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				int winner = checkMicroboardWinner(x, y);
				mMacroboard[x][y] = winner;
			}
		}
	}
	
	/**
	 * Checks the microboard for a winner
	 * @param args : int x (0-2), int y (0-2)
	 * @return : player id of winner or 0
	 */
	public int checkMicroboardWinner(int macroX, int macroY) {
		int startX = macroX*3;
		int startY = macroY*3;
		int remainderX = macroX%3;
		int remainderY = macroY%3;
		/* Check horizontal wins */
		for (int y = startY; y < startY+3; y++) {
			if (mBoard[startX+0][y] == mBoard[startX+1][y] && mBoard[startX+1][y] == mBoard[startX+2][y] && mBoard[startX+0][y] > 0) {
				System.out.println("FOUND A HORIZONTAL WIN AT " + y);
				return mBoard[startX+0][y];
			}
		}
		/* Check vertical wins */
		for (int x = startX; x < startX+3; x++) {
			if (mBoard[x][startY+0] == mBoard[x][startY+1] && mBoard[x][startY+1] == mBoard[x][startY+2] && mBoard[x][startY+0] > 0) {
				System.out.println("FOUND A VERTICAL WIN AT " + x);
				return mBoard[x][startY+0];
			}
		}
		/* Check diagonal wins */
		if (mBoard[startX][startY] == mBoard[startX+1][startY+1] && mBoard[startX+1][startY+1] == mBoard[startX+2][startY+2] && mBoard[startX+0][startY+0] > 0) {
			System.out.println("FOUND A DIAGONAL WIN AT " + startX);
			return mBoard[startX][startY];
		}
		if (mBoard[startX+2][startY] == mBoard[startX+1][startY+1] && mBoard[startX+1][startY+1] == mBoard[startX][startY+2] && mBoard[startX+2][startY+0] > 0) {
			System.out.println("FOUND A ANTIDIAGONAL WIN AT " + startX);
			return mBoard[startX+2][startY];
		}
		return 0;
	}
	
	public int checkMacroboardWinner() {
		/* Check horizontal wins */
		for (int y = 0; y < 3; y++) {
			if (mMacroboard[0][y] == mMacroboard[1][y] && mMacroboard[1][y] == mMacroboard[2][y] && mMacroboard[0][y] > 0) {
				System.out.println("FOUND A HORIZONTAL MACROWIN AT " + y);
				return mMacroboard[0][y];
			}
		}
		/* Check vertical wins */
		for (int x = 0; x < 3; x++) {
			if (mMacroboard[x][0] == mMacroboard[x][1] && mMacroboard[x][1] == mMacroboard[x][2] && mMacroboard[x][0] > 0) {
				System.out.println("FOUND A VERTICAL MACROWIN AT " + x);
				return mMacroboard[x][0];
			}
		}
		/* Check diagonal wins */
		if (mMacroboard[0][0] == mMacroboard[1][1] && mMacroboard[1][1] == mMacroboard[2][2] && mMacroboard[0][0] > 0) {
			System.out.println("FOUND A DIAGONAL MACROWIN");
			return mMacroboard[0][0];
		}
		if (mMacroboard[2][0] == mMacroboard[1][1] && mMacroboard[1][1] == mMacroboard[0][2] && mMacroboard[2][0] > 0) {
			System.out.println("FOUND A ANTIDIAGONAL MACROWIN");
			return mMacroboard[2][0];
		}
		return 0;
	}

	public int getNrColumns() {
		return mCols;
	}
	
	public int getNrRows() {
		return mRows;
	}
}
