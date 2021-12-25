import java.awt.Color;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;

public class WithinLimitsController {
	private static Player currentPlayer;
	private static Scanner in;
	private static ArrayList<Player> highScores = new ArrayList<>();
	private static JButton temp;

	public WithinLimitsController() {
	}

	public static void updateLabel(JLabel l, String text) {
		l.setText(text);
	}

	public static void difficultyChanger(int lower, int upper) {
		WithinLimits.setDifficulty(WithinLimits.getDifficulty()+1);
		WithinLimits.setUpperLimit(upper);
		WithinLimits.setLowerLimit(lower);
	}
	// this method will take in a button from the interface and assign it a function
	public static void listenAndUpdate(JButton b) {
		b.addActionListener(e -> {
			// ARITHMETIC OPERATIONS BUTTONS
			temp = (JButton)e.getSource(); // we set our action source to a temp button
			if(temp.equals(WithinLimits.getSubBtn())){ // if the source was the sub button, we do the sub operation
				updateLabel(WithinLimits.getMyTotal(),"" + (WithinLimits.getNumTotal()[0]-WithinLimits.getNumR()[0])); // update the total label to reflect the change by the sub btn
				WithinLimits.getNumTotal()[0]-=WithinLimits.getNumR()[0];
				if(WithinLimits.getDifficulty()==4) { // if we're on super hard difficulty, we hide the total
					WithinLimits.getMyTotal().setVisible(false);
				} else WithinLimits.getMyTotal().setVisible(true);
				WithinLimits.setGameInProgress(true); // we set the game in progress, because we don't want players switching difficulty mid-game because each difficulty has a score addition and upper and lower limit tied to it
				try {
					scoreUpdater(); // update the appropriate elements in the gui
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if(temp.equals(WithinLimits.getAddBtn())) { // if the source was the add button, we do the adding operation
				updateLabel(WithinLimits.getMyTotal(),"" + (WithinLimits.getNumTotal()[0]+WithinLimits.getNumR()[0]));
				WithinLimits.getNumTotal()[0]+=WithinLimits.getNumR()[0];
				if(WithinLimits.getDifficulty()==4) {
					WithinLimits.getMyTotal().setVisible(false);
				} else WithinLimits.getMyTotal().setVisible(true);
				WithinLimits.setGameInProgress(true);
				try {
					scoreUpdater();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if(temp.equals(WithinLimits.getMultBtn())) { // if the source was the multiply button, we do the multiplication operation
				updateLabel(WithinLimits.getMyTotal(),"" + (WithinLimits.getNumTotal()[0]*WithinLimits.getNumR()[0]));
				WithinLimits.getNumTotal()[0]*=WithinLimits.getNumR()[0];
				if(WithinLimits.getDifficulty()==4) {
					WithinLimits.getMyTotal().setVisible(false);
				} else WithinLimits.getMyTotal().setVisible(true);
				WithinLimits.setGameInProgress(true);
				try {
					scoreUpdater();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// OTHER UTILITY BUTTONS
			} else if(temp.equals(WithinLimits.getDifficultyButton())) {
				if(!WithinLimits.isGameInProgress()) { // before changing the difficulty, we first make sure that the game is NOT in progress.
					if(WithinLimits.getDifficulty() == 1) { // each difficulty has its own values for upper,lower bounds
						difficultyChanger(5,20);
						updateLabel(WithinLimits.getDifficultyLevel(), "Difficulty: Medium");
					} else if(WithinLimits.getDifficulty() == 2) {
						difficultyChanger(5,15);
						updateLabel(WithinLimits.getDifficultyLevel(), "Difficulty: Hard");
					} else if(WithinLimits.getDifficulty() == 3) {
						difficultyChanger(1,35);
						updateLabel(WithinLimits.getDifficultyLevel(), "Difficulty: Super hard");
					} else if(WithinLimits.getDifficulty() == 4) {
						difficultyChanger(1,30);
						updateLabel(WithinLimits.getDifficultyLevel(), "Difficulty: Easy");
					}
					// we update the gui to display the bounds
					updateLabel(WithinLimits.getUpperLimitLabel(),"Upper limit: " + WithinLimits.getUpperLimit());
					updateLabel(WithinLimits.getLowerLimitLabel(),"Lower limit: " + WithinLimits.getLowerLimit());
					WithinLimits.getGameReset().doClick(); // and then we reset the game to its original state (scores zeroed, random number renewed, game progress check set to false, etc)
					WithinLimits.getGameReset().doClick();
				} else JOptionPane.showMessageDialog(null, new JLabel("Error: Can't change difficulty while the game is in progress. Either reset the game or continue playing.")); // we display a message to indicate that there's a game in progress
			} else if(temp.equals(WithinLimits.getGameReset())) { // our game resetter
				WithinLimits.getMyTotal().setVisible(true);
				WithinLimits.setGameInProgress(false);
				WithinLimits.getNumTotal()[0] = (int)((WithinLimits.getUpperLimit()+WithinLimits.getLowerLimit())/2);
				WithinLimits.getNumR()[0] = getRandomInt(WithinLimits.getUpperLimit());
				WithinLimits.getScore()[0] = 0;
				updateLabel(WithinLimits.getMyTotal(),""+WithinLimits.getNumTotal()[0]);
				WithinLimits.getMyTotal().setForeground(Color.black);
				updateLabel(WithinLimits.getScoreCount(),"Score: "+WithinLimits.getScore()[0]);
				updateLabel(WithinLimits.getMyRandomNum(),""+WithinLimits.getNumR()[0]);
				WithinLimits.getPanel().setBorder(new MatteBorder(5, 0, 5, 0, (Color) new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256)))); // random color border! every time the game is reset
			} else if(temp.equals(WithinLimits.getLeaderboardBtn())) {
				try {
					leaderboardLoader(); // load the leaderboard, in the loader method I setup a way such that if there's no leaderboard.txt present, the game will create one and add random players in
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, leaderboardView()); // display the leaderboard in a mini window
			} else if(temp.equals(WithinLimits.getInstructions())) {
				JOptionPane.showMessageDialog(WithinLimits.getInstructions(), WithinLimits.getPicLabel());
			}
		});
	}

	public static boolean withinLimits(int num) { // validity of number checking
		return (num>=WithinLimits.getLowerLimit()) && num<=WithinLimits.getUpperLimit();
	}
	// game over popup that resets the game after being dismissed
	public static void gameOverPopup() throws IOException {
		String diff = "";
		switch(WithinLimits.getDifficulty()){
		case 1: diff="easy";
		break;
		case 2: diff="medium";
		break;
		case 3: diff="hard";
		break;
		case 4: diff="super hard";
		break;
		}
		currentPlayer = new Player(WithinLimits.getPlayerName().getText(), WithinLimits.getScore()[0]); // create a new player using the name in the text field and the score achieved before the game ended
		highScores.add(currentPlayer); // add the player
		leaderboardWriter(); // save the leaderboard (sorting happens inside the method)
		JOptionPane.showMessageDialog(null, new JLabel("<html>You were out of bounds! Game is finished.<br/> Your score was: " + WithinLimits.getScore()[0]+ " on " + diff +" difficulty.<br/>Click OK to reset the game.</html>")); // using html for line break
		WithinLimits.getGameReset().doClick(); // reset the game
	}

	// random int generator [0, upper/2)
	public static int getRandomInt(int upper) {
		int randomNum = 1 + (int)(Math.random()*(upper/2));
		int currentTotal = WithinLimits.getNumTotal()[0];
		while(!withinLimits(randomNum+currentTotal) && !withinLimits(currentTotal-randomNum)) { 
			// if the random number generated is unusable for BOTH adding and subtracting, then roll a new number.
			// this will prevent the player from being stuck in a situation where they're forced to lose since both options lead to an out of bounds result
			randomNum = 1 + (int)(Math.random()*(upper/2));
		}
		return randomNum;
	}
	// score updating method that's called after an arithmetic button finished its operation
	public static void scoreUpdater() throws IOException {
		int scoreToAdd = 0;
		if(withinLimits(WithinLimits.getNumTotal()[0])) { // if within limits, then the operation was a success and therefore add to the score
			if(temp.equals(WithinLimits.getMultBtn())) { // if it was a multiplication process, add 3 instead
				scoreToAdd = 3;
			} else scoreToAdd = 1;
			if(WithinLimits.getDifficulty() == 4) { // if its difficulty 4(super hard) add double the amount
				scoreToAdd*=2;
				WithinLimits.getScore()[0]+=scoreToAdd;
			} else WithinLimits.getScore()[0]+=scoreToAdd;
		} else gameOverPopup(); // else display the game over dialogue
		if(WithinLimits.getNumTotal()[0]<=WithinLimits.getLowerLimit()+3 || WithinLimits.getNumTotal()[0]>=WithinLimits.getUpperLimit()-3) { // number changes to red when the total gets really close to one of the limits
			WithinLimits.getMyTotal().setForeground(Color.red);
		} else WithinLimits.getMyTotal().setForeground(Color.black);
		WithinLimits.getNumR()[0] = getRandomInt(WithinLimits.getUpperLimit()); // generate a new number
		updateLabel(WithinLimits.getMyRandomNum(),WithinLimits.getNumR()[0]+""); // update with the new number
		updateLabel(WithinLimits.getScoreCount(),"Score: "+ WithinLimits.getScore()[0]); // update the score
	}

	public static void leaderboardLoader() throws IOException {
		String[] names = {"Jenny", "James", "Mike", "Fay", "Ali", "Samir", "Sukaina", "Reem", "Sarah", "Jane", "Don", "Jim", "Steve", "Jane", "Nora", "Scarlett"};
		File f = new File("leaderboards.txt");
		if(highScores.isEmpty()) {
			try {
				in = new Scanner(f);
				while(in.hasNextLine()) {
					String line = in.nextLine();
					String name = line.substring(0,line.lastIndexOf('-')).trim();
					int score = Integer.parseInt(line.substring(line.lastIndexOf('-')+1).trim());
					highScores.add(new Player(name, score));
				}
			} catch(FileNotFoundException e) {
				PrintWriter pw = new PrintWriter(f);
				for(int i=0;i<10;i++) {
					pw.println(new Player(names[(int)(Math.random()*names.length)], (int)(Math.random()*55)));
				}
				pw.close();
			}
		}
	}

	public static JPanel leaderboardView() {
		JPanel pnl = new JPanel();
		pnl.setLayout(new BoxLayout(pnl,BoxLayout.Y_AXIS));
		Collections.sort(highScores);
		int topScores = highScores.size()>=10 ? 10 : highScores.size();
		for(int i=0; i<topScores;i++) {
			Player p = highScores.get(i);
			pnl.add(new JLabel((i+1)+".   "+p.getName()+"     "+p.getScore()));
		}
		return pnl;
	}

	public static void leaderboardWriter() throws IOException {
		PrintWriter pw = new PrintWriter(new File("leaderboards.txt"));
		Collections.sort(highScores);
		for(Player p: highScores) {
			pw.println(p);
		}
		pw.close();
	}
}
