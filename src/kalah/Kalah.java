package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;

/**
 * This class is the starting point for a Kalah implementation using
 * the test infrastructure. Remove this comment (or rather, replace it
 * with something more appropriate)
 */
public class Kalah {
	private IO _io;
	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}
	public void play(IO io) {
		
		int numberOfPlayer = 2;
		int houseNumber = 1;
		int userTurn = 1;
		
		int houseEachPlayer = 6;
		int storeEachPlayer = 1;
		
		_io = io;
		// Initialize Board
		Board board = new Board(houseEachPlayer, storeEachPlayer, numberOfPlayer);
		// Have winer or not
		boolean winer = false;
		
		while (houseNumber > 0) {
			
			board.displayBoard(_io);
			// player house is empty, game over
			if (board.checkHouseEmpty(userTurn)) {
				winer = true;
				break;
			}
			
			// Input chosen house number
			houseNumber = _io.readInteger(String.format("Player P%d's turn - Specify house number or 'q' to quit: ", userTurn), 1, houseEachPlayer, -1, "q");
			// if nor a valid input, ask user input again
			if (houseNumber < 1) {
				break;
			}
			// if chosen pit is no empty, ask user choose again
			if (!board.checkPitEffective(userTurn,houseNumber)) {
				_io.println("House is empty. Move again.");
			}
			else {
				int currentPitIndex = board.pickHouse(userTurn, houseNumber);
				if (!board.checkContinue(userTurn, currentPitIndex)) {
					if (userTurn == 1) {
						userTurn = 2;
					}else {
						userTurn = 1;
					}
				}
			}
			
		}
		// Finish the game, and output the winner if exist
		_io.println("Game over");
		board.displayBoard(_io);
		if (winer) {
			board.getWinner(_io, numberOfPlayer);
			
		}
	}
}
