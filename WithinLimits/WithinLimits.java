import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class WithinLimits {
	private static int difficulty = 1;
	private static int lowerLimit = 1;
	private static int upperLimit = 30;
	// using arrays to utilize references and to be able to change these values inside listeners
	private static int[] numR = new int[1];
	private static int[] numTotal = new int[1];
	private static int[] score = new int[1];
	private BufferedImage ruleSet;
	private static JLabel picLabel,myTotalLabel, myTotal,scoreCount,myRandomNum,difficultyLevel,upperLimitLabel,lowerLimitLabel;
	private static JButton gameReset, addBtn, multBtn, subBtn, leaderboardBtn, instructions,difficultyButton;
	private static JPanel panel;
	private static JTextField playerName;
	private static boolean gameInProgress = false;
	//private static ArrayList<Player> highScores = new ArrayList<>();
	
	public WithinLimits() throws IOException {
		WithinLimitsController.leaderboardLoader();
		// rules image loading
		URL url = new URL("https://github.com/aliAljaffer/projects/raw/main/WithinLimits/rules.png");
		ruleSet = ImageIO.read(url);
		picLabel = new JLabel(new ImageIcon(ruleSet));
		
		JFrame frame = new JFrame("Within Limits");
		
		// assigning primary values
		numR[0] = WithinLimitsController.getRandomInt(upperLimit);
		numTotal[0] = (int)((upperLimit+lowerLimit)/2);
		score[0] = 0;
		
		// the main vertical panel
		JPanel vPnl = new JPanel();
		vPnl.setLayout(new BoxLayout(vPnl, BoxLayout.Y_AXIS));
		// top panel to hold the buttons
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// show leaderboard
		leaderboardBtn = new JButton("Leaderboard");
		WithinLimitsController.listenAndUpdate(leaderboardBtn);
		
		// show instructions
		instructions = new JButton("Instructions");
		WithinLimitsController.listenAndUpdate(instructions);
		
		// a button to reset the game to a fresh state
		gameReset = new JButton("Reset Game");
		WithinLimitsController.listenAndUpdate(gameReset);
		// Difficulty cycling buttons. Each difficulty has a different lower and upper bound. Also resets the game.
		difficultyButton = new JButton("Cycle Difficulty");
		WithinLimitsController.listenAndUpdate(difficultyButton);
		
		topPanel.add(leaderboardBtn);
		topPanel.add(instructions);
		topPanel.add(gameReset);
		topPanel.add(difficultyButton);
		vPnl.add(topPanel);
		
		JPanel textFieldPnl = new JPanel(new BorderLayout());
		playerName = new JTextField("Enter your name");
		playerName.setHorizontalAlignment(JLabel.CENTER);
		playerName.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e) {
				if(playerName.getText().equals("Enter your name")) {
					playerName.setText("");
				}
			}
		});
		textFieldPnl.add(playerName,BorderLayout.CENTER);
		vPnl.add(textFieldPnl);
		
		JPanel horizontalBox = new JPanel(new GridLayout(1,2));
		upperLimitLabel = new JLabel("Upper limit: " + upperLimit);
		lowerLimitLabel = new JLabel("Lower limit: " + lowerLimit);
		difficultyLevel = new JLabel("Difficulty: Easy");
		upperLimitLabel.setHorizontalAlignment(JLabel.CENTER);
		lowerLimitLabel.setHorizontalAlignment(JLabel.CENTER);
		difficultyLevel.setHorizontalAlignment(JLabel.CENTER);
		horizontalBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		upperLimitLabel.setForeground(Color.red);
		lowerLimitLabel.setForeground(Color.red);
		difficultyLevel.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		horizontalBox.add(lowerLimitLabel);
		horizontalBox.add(difficultyLevel);
		horizontalBox.add(upperLimitLabel);
		vPnl.add(horizontalBox);
		vPnl.add(new JLabel(" "));
		
		JPanel horizontalBox2 = new JPanel();
		horizontalBox2.setLayout(new BoxLayout(horizontalBox2, BoxLayout.X_AXIS));
		myTotalLabel = new JLabel("Total: ");
		myTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		myTotalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		myTotalLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		myTotal = new JLabel(""+numTotal[0]);
		myTotal.setFont(new Font("Tahoma", Font.PLAIN, 30));
		myTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
		horizontalBox2.add(myTotalLabel);
		horizontalBox2.add(myTotal);
		vPnl.add(horizontalBox2);
		vPnl.add(new JLabel(" "));
		
		// colorful border separator :-)
		panel = new JPanel(new GridLayout(1,0));
		panel.setBorder(new MatteBorder(5, 0, 5, 0, (Color) new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)))); // random color border!
		panel.add(new JLabel(" "));
		vPnl.add(panel);
		vPnl.add(new JLabel(" "));
		
		myRandomNum = new JLabel(numR[0]+"");
		myRandomNum.setHorizontalAlignment(SwingConstants.LEFT);
		myRandomNum.setFont(new Font("Tahoma", Font.PLAIN, 30));
		JLabel randomLabel = new JLabel("Random number: ");
		randomLabel.setFont(new Font("Tahoma", Font.PLAIN, 30));
		randomLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JPanel rndmPanel = new JPanel();
		rndmPanel.setLayout(new BoxLayout(rndmPanel, BoxLayout.X_AXIS));
		rndmPanel.add(randomLabel);
		rndmPanel.add(myRandomNum);
		vPnl.add(rndmPanel);
		vPnl.add(new JLabel(" "));
		
		// panel to hold the line of buttons
		JPanel buttonsBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		
		addBtn = new JButton("Add it");
		// if clicked, add to total and change it's label.
		WithinLimitsController.listenAndUpdate(addBtn);
		
		multBtn = new JButton("Multiply it");
		WithinLimitsController.listenAndUpdate(multBtn);
		
		subBtn = new JButton("Subtract it");
		WithinLimitsController.listenAndUpdate(subBtn);
		
		buttonsBox.add(addBtn);
		buttonsBox.add(multBtn);
		buttonsBox.add(subBtn);
		vPnl.add(buttonsBox);
		
		// lower panel to include the score
		JPanel scorePanel = new JPanel();
		scoreCount = new JLabel("Score: " + score[0]);
		scoreCount.setHorizontalAlignment(SwingConstants.LEFT);
		scoreCount.setFont(new Font("SansSerif", Font.BOLD, 18));
		scoreCount.setForeground(Color.blue);
		scorePanel.add(scoreCount);
		for(int i=0;i<200;i++) {
			scorePanel.add(new JLabel(" ")); // pushing the score to the left. didnt know how else to do it so this is a bandaid fix
		}
		vPnl.add(scorePanel);
		
		frame.getContentPane().add(vPnl);
		frame.setSize(600,380);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
	}

	public static void main(String[] args) throws IOException {
		new WithinLimits();
	}
	
	// getters and setters for needed fields
	public static int getDifficulty() {
		return WithinLimits.difficulty;
	}

	public static void setDifficulty(int difficulty) {
		if(difficulty>4) { // we dont want to go over 4
			WithinLimits.difficulty = 1;
		} else WithinLimits.difficulty = difficulty;
	}

	public static int getLowerLimit() {
		return lowerLimit;
	}

	public static void setLowerLimit(int lowerLimit) {
		WithinLimits.lowerLimit = lowerLimit;
	}

	public static int getUpperLimit() {
		return upperLimit;
	}

	public static void setUpperLimit(int upperLimit) {
		WithinLimits.upperLimit = upperLimit;
	}

	public static int[] getNumR() {
		return numR;
	}

	public static int[] getNumTotal() {
		return numTotal;
	}

	public static int[] getScore() {
		return score;
	}

	public static JLabel getMyTotalLabel() {
		return myTotalLabel;
	}

	public static JLabel getMyTotal() {
		return myTotal;
	}

	public static JLabel getScoreCount() {
		return scoreCount;
	}

	public static JLabel getMyRandomNum() {
		return myRandomNum;
	}

	public static JLabel getDifficultyLevel() {
		return difficultyLevel;
	}
	
	public static JLabel getUpperLimitLabel() {
		return upperLimitLabel;
	}

	public static JLabel getLowerLimitLabel() {
		return lowerLimitLabel;
	}

	public static JButton getGameReset() {
		return gameReset;
	}
	
	public static JPanel getPanel() {
		return panel;
	}

	public static JTextField getPlayerName() {
		return playerName;
	}

	public static boolean isGameInProgress() {
		return gameInProgress;
	}

	public static void setGameInProgress(boolean gameInProgress) {
		WithinLimits.gameInProgress = gameInProgress;
	}

	public static JButton getAddBtn() {
		return addBtn;
	}

	public static JButton getMultBtn() {
		return multBtn;
	}

	public static JButton getSubBtn() {
		return subBtn;
	}

	public static JButton getLeaderboardBtn() {
		return leaderboardBtn;
	}

	public static JButton getInstructions() {
		return instructions;
	}

	public static JButton getDifficultyButton() {
		return difficultyButton;
	}

	public static JLabel getPicLabel() {
		return picLabel;
	}

}
