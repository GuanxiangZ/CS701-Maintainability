package kalah;

import com.qualitascorpus.testsupport.IO;

public class Board {
	
	//All the houses and stores for all players store in this list. order: player2store, player1house, player1store, player2house ...
	private OverrideArraryList<Pit> boardArrayList = new OverrideArraryList<Pit>();
	private int numberOfPlayer = 2;
	private int houseEachPlayer = 6;
	private int storeEachPlayer = 1;
	private int seedEachHouse = 4;
	private int seedEachStore = 0;
	
	public Board(int houseEachPlayer,int storeEachPlayer,int numberOfPlayer) {
		this.houseEachPlayer = houseEachPlayer;
		this.storeEachPlayer = storeEachPlayer;
		this.numberOfPlayer = numberOfPlayer;
		
		//Initialize Board
		this.InitializeBoard(this.numberOfPlayer, this.houseEachPlayer, this.storeEachPlayer);
		
	}
	
	public void InitializeBoard(int numberOfPlayer, int houseEachPlayer, int storeEachPlayer) {
		int storeUserId = numberOfPlayer;
		for (int houseUserId = 1; houseUserId < numberOfPlayer + 1; houseUserId++) {
			
			for (int s = 0; s < storeEachPlayer; s++) {
				//add store to board
				boardArrayList.add(new Store(storeUserId, seedEachStore));
			}
			
			for (int h = 0; h < houseEachPlayer; h++) {
				//add house to board
				boardArrayList.add(new House(houseUserId, seedEachHouse));
			}
			
			storeUserId -= 1;
		}
		
	}
	
	public String repeatString(String s, int n) {
		String afterReaptString = "";
		for (int time = 0; time < n; time ++) {
			afterReaptString += s;
		}
		return afterReaptString;
	}
	
	public void displayBoard(IO io) {
		
		//Pit print order, true for increasing, false for decreasing 
		boolean displayOrder = false;
		//Get start and end pit for each player
		int start = boardArrayList.size() - houseEachPlayer - storeEachPlayer;
		int end = boardArrayList.size();
		
		io.println("+----+" + repeatString("-------+", houseEachPlayer) + "----+");
		while (end > 0) {
			//Output decreasing
			if (displayOrder == false) {
				//Display player id
				String output = String.format("| P%d |", boardArrayList.get(start + houseEachPlayer + storeEachPlayer).getPlayerId());
				//House Pit Number(ID)
				int houseNumber = houseEachPlayer; 
				for (int i = end - 1; i > start; i--) {
					//Display house pit
					output += String.format("%2d[%2d] |", houseNumber, boardArrayList.get(i).getNumberOfSeeds());
					houseNumber -= 1;
				}
				//Display store pit
				output += String.format(" %2d |", boardArrayList.get(start).getNumberOfSeeds());
				io.println(output);
				displayOrder = true;
			}
			//Output Increasing
			else {
				String output = "|";
				//House Pit Number(ID)
				int houseNumber = 1; 
				//Display store pit
				output += String.format(" %2d |", boardArrayList.get(start).getNumberOfSeeds());
				for (int i = start + 1; i < end; i++) {
					//Display house pit
					output += String.format("%2d[%2d] |", houseNumber, boardArrayList.get(i).getNumberOfSeeds());
					houseNumber += 1;
				}
				//Display player id
				output += String.format(" P%d |", boardArrayList.get(start + houseEachPlayer + storeEachPlayer).getPlayerId());
				io.println(output);
				displayOrder = false;
			}
			//get start and end pit for next player
			end = start;
			start = start - houseEachPlayer - storeEachPlayer;
			if (end > 0) {
				io.println("|    |" + repeatString("+-------", houseEachPlayer).substring(1) + "|    |");
			}
			
		}
		io.println("+----+" + repeatString("-------+", houseEachPlayer) + "----+");
	}
	
	public int getIndexByHouseAndPlayer(int playerId, int houseNumber) {
		int houseIndex = (playerId - 1) * (houseEachPlayer + storeEachPlayer) + houseNumber ;
		return houseIndex;
	}
	
	public int pickHouse(int playerId, int houseNumber) {
		// Get House position in Array by player id and house number
		int houseIndex = getIndexByHouseAndPlayer(playerId, houseNumber);
		House choosenHouse = (House) boardArrayList.get(houseIndex);
		//Remove all seeds in chosen house, and get the number of seeds
		int seeds = choosenHouse.removeAllSeed();
		int index = houseIndex;
		//place one seed in each flowing pit
		while (seeds > 0) {
			index += 1;
			if (boardArrayList.get(index).checkIsStore() && boardArrayList.get(index).getPlayerId() != playerId) {
			} else {
				// if this is last seed & the pit is empty & the pit is house & pit own by player
				if (seeds == 1 && boardArrayList.get(index).getNumberOfSeeds() == 0 && !boardArrayList.get(index).isStore && boardArrayList.get(index).getPlayerId() == playerId) {
					// get opposite house
					int oppsiteHouseIndex = getOppsiteHouse(index);
					House oppsiteHouse = (House) boardArrayList.get(oppsiteHouseIndex);
					// if opposite house not empty, Capture!
					if (oppsiteHouse.getNumberOfSeeds() != 0) {
						// remove all seeds in opposite house
						int oppsiteHouseSeeds = oppsiteHouse.removeAllSeed();
						seeds += oppsiteHouseSeeds;
						// find player store, and put all seeds in store
						int playerStoreIndex = boardArrayList.get(index).getPlayerId() * (houseEachPlayer + storeEachPlayer);
						while (seeds > 0) {
							boardArrayList.get(playerStoreIndex).addSeed();
							seeds -= 1;
						}
					} else {
						// If the opposite house is empty, no capture
						boardArrayList.get(index).addSeed();
						seeds -= 1;
					}
					index = oppsiteHouseIndex;
					
					
				} else {
					boardArrayList.get(index).addSeed();
					seeds -= 1;
				}
				
			}
		}
		
		//Return current pit index
		return index;
				
	}
	
	public int getOppsiteHouse(int houseIndex) {
		int realHouseArrayIndex = houseIndex % boardArrayList.size();
		int oppsiteHouseIndex = realHouseArrayIndex + (boardArrayList.get(realHouseArrayIndex).getPlayerId() * (houseEachPlayer + storeEachPlayer) - realHouseArrayIndex) * numberOfPlayer;
		return oppsiteHouseIndex;
	}
	
	public boolean checkPitOwner(int playerId,int pitIndex) {
		//Check the pit is own by this payer or not. 
		return (boardArrayList.get(pitIndex).getPlayerId() == playerId);
	}
	
	
	public boolean checkPitSeedIsOne(int pitIndex) {
		//Check the number of seeds is one or not. 
		return (boardArrayList.get(pitIndex).getNumberOfSeeds() == 1);
	}
	
	public boolean checkPitIsStore(int pitIndex) {
		return (boardArrayList.get(pitIndex).checkIsStore());
	}
	
	public boolean checkPitEmpty(int pitIndex) {
		return (boardArrayList.get(pitIndex).getNumberOfSeeds() == 0);
	}
	
	//check the player can continue playing or not. 
	public boolean checkContinue(int playerId,int pitIndex) {
		if (checkPitOwner(playerId, pitIndex)) {
			if (checkPitIsStore(pitIndex)) {
				return true;
			}
			else if (checkPitSeedIsOne(pitIndex)) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	//Check the pit number valued or not
	public boolean checkPitEffective(int playerId,int houseNumber) {
		int pitIndex = getIndexByHouseAndPlayer(playerId, houseNumber);
		if (checkPitEmpty(pitIndex)) {
			return false;
		} else {
			return true;
		}
	}
	
	//Check Player house empty
	public boolean checkHouseEmpty(int playerId) {
		for (int i = (playerId - 1) * (houseEachPlayer + storeEachPlayer) + 1; i < playerId * (houseEachPlayer + storeEachPlayer); i ++) {
			if (boardArrayList.get(i).getNumberOfSeeds() != 0) {
				return false;
			}
		}
		return true;
	}
	
	//Get score for player
	public int getScore(int playerId) {
		int playerScore = 0;
		for (int i = 0; i < boardArrayList.size(); i++) {
			if (boardArrayList.get(i).getPlayerId() == playerId) {
				playerScore += boardArrayList.get(i).getNumberOfSeeds();
			}
		}
		return playerScore;
	}
	
	
	//Get winner
	public void getWinner(IO io,int numberOfPlayer) {
		int winner = 0;
		int maxScore = -1;
		for (int id = 1; id < numberOfPlayer + 1; id ++) {
			int playerScore = getScore(id);
			io.println(String.format("	player %d:%d", id,playerScore));
			if (playerScore > maxScore) {
				maxScore = playerScore;
				winner = id;
			}
		}
		boolean tie = true;
		for (int id = 1; id < numberOfPlayer + 1; id ++) {
			if (getScore(id) != maxScore) {
				tie = false;
			}
		}
		if (tie) {
			io.println("A tie!");
		} else {
			io.println(String.format("Player %d wins!",winner));
		}
		
	
	}
	
	
	
}