public class Player implements Comparable<Player> {
	private int score;
	private String name;
	public Player(String name)  {
		setName(name);
		setScore(score);
	}
	
	public Player(String name, int score)  {
		setName(name);
		setScore(score);
	}

	@Override
	public int compareTo(Player o) {
		return o.getScore()-this.getScore();
	}
	
	@Override
	public String toString() {
		return getName() + "\t-\t" + getScore();
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public String getName() {
		return name;
	}
	// if the player hasn't entered a name yet, assign it "No Name"
	public void setName(String name) {
		if(name.equals("Enter your name") || name.trim().isEmpty()) {
			this.name = "No Name";
		} else this.name = name.trim();
	}
	
	public int getScore() {
		return score;
	}
}
