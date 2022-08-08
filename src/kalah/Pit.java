package kalah;

public abstract class Pit {
	
	//Number of seeds in this pit
	protected int seeds; 
	private int playerId;
	protected boolean isStore; 
	
	public Pit(int playerId, int seeds) {
		this.playerId = playerId;
		this.seeds = seeds;
	}
	
	//return the number of seed
	public int getNumberOfSeeds() {
		return this.seeds;
	}
	
	
	//Add a seed to pit
	public void addSeed() {
		this.seeds += 1;
	}
	
	//return the playerId(owner) of the pit. 
	public int getPlayerId() {
		return this.playerId;
	}
	
	//If this pit is a store, return true. 
	public boolean checkIsStore() {
		return this.isStore;
	}
	
}
