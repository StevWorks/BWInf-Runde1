public class Spieler {
	int spielernummer;
	int score;
	int siege;

	public Spieler(int nummer) {
		spielernummer = nummer;
		score = 0; // TODO Wert Random Zahl zuweisen
		siege = 0;
	}
	
	public Spieler(int nummer, int score) {
		spielernummer = nummer;
		this.score = score;
		siege = 0;
	}

	public Spieler(int nummer, int score, int siege) {
		spielernummer = nummer;
		this.score = score;
		this.siege = siege;
	}
	
	public int getSpielernummer() {
		return spielernummer;
	}

	public void setSpielernummer(int nummer) {
		spielernummer = nummer;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int wert) {
		score = wert;
	}

	public int getSiege() {
		return siege;
	}

	public void setSiege(int wert) {
		siege = wert;
	}

	public void erhoeheSiege() {
		siege++;
	}

	@Override
	public String toString() {
		return "Spieler [spielernummer=" + spielernummer + "]";
	}
	

}
