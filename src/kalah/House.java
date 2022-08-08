package kalah;

public class House extends Pit {

	public House(int playerId, int seeds) {
		super(playerId, seeds);
		this.isStore = false;
	}
	
	//Take one seed from house. 
	public void removeOneSeed() {
		this.seeds -= 1;
	}
	
	//Take all the seed from house, return the number of seed before take it. 
	public int removeAllSeed() {
		int seeds = this.seeds;
		this.seeds = 0;
		return seeds;
	}
	
}
