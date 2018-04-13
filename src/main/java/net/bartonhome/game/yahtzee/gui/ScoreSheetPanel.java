package net.bartonhome.game.yahtzee.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.bartonhome.game.yahtzee.ScoreConstants;
import net.bartonhome.game.yahtzee.service.GameController;
import net.bartonhome.game.yahtzee.service.Score;

public class ScoreSheetPanel extends JPanel implements ScoreConstants {
	
	public static final String USE_ONES = "Ones";
	public static final String USE_TWOS = "Twos";
	public static final String USE_THREES = "Threes";
	public static final String USE_FOURS = "Fours";
	public static final String USE_FIVES = "Fives";
	public static final String USE_SIXES = "Sixes";
	public static final String USE_THREE_KIND = "Three of a kind";
	public static final String USE_FOUR_KIND = "Four of a kind";
	public static final String USE_FULL_HOUSE = "Full house";
	public static final String USE_SMALL_STRAIGHT = "Small straight";
	public static final String USE_LONG_STRAIGHT = "Long straight";
	public static final String USE_YAHTZEE = "Yahtzee";
	public static final String USE_CHANCE = "Chance";
	
	public static final String[] NAMES = new String[] {
		USE_ONES, USE_TWOS, USE_THREES, USE_FOURS, USE_FIVES, USE_SIXES,
		USE_THREE_KIND, USE_FOUR_KIND, USE_FULL_HOUSE, 
		USE_SMALL_STRAIGHT, USE_LONG_STRAIGHT, USE_YAHTZEE, USE_CHANCE};
	
//	JTextField scoreOnes = new ScoreField();
//	JTextField scoreTwos = new ScoreField();
//	JTextField scoreThrees = new ScoreField();
//	JTextField scoreFours = new ScoreField();
//	JTextField scoreFives = new ScoreField();
//	JTextField scoreSixes = new ScoreField();
	JTextField scoreSectionOneSubTotal = new ScoreField();
	JTextField scoreSectionOneBonus = new ScoreField();
	JTextField scoreSectionOneTotal = new ScoreField();
//	JTextField scoreThreeOfAKind = new ScoreField();
//	JTextField scoreFourOfAKind = new ScoreField();
//	JTextField scoreFullHouse = new ScoreField();
//	JTextField scoreSmlStraight = new ScoreField();
//	JTextField scoreLngStraight = new ScoreField();
//	JTextField scoreYahtzee = new ScoreField();
//	JTextField scoreChance = new ScoreField();
	JTextField scoreYahtzeeBonus = new ScoreField();
	JTextField scoreSectionTwoTotal = new ScoreField();
	JTextField scoreTotal = new ScoreField();
	
	JButton useOnes = new JButton("Use");
	JButton useTwos = new JButton("Use");
	JButton useThrees = new JButton("Use");
	JButton useFours = new JButton("Use");
	JButton useFives = new JButton("Use");
	JButton useSixes = new JButton("Use");
	JButton useThreeOfAKind = new JButton("Use");
	JButton useFourOfAKind = new JButton("Use");
	JButton useFullHouse = new JButton("Use");
	JButton useSmlStraight = new JButton("Use");
	JButton useLngStraight = new JButton("Use");
	JButton useYahtzee = new JButton("Use");
	JButton useChance = new JButton("Use");
	
	HashMap<String, ScoreField> scoresForUseButtons;
	private HashMap<String, JButton> useButtons;
	
	public ScoreSheetPanel() {
		initComponents();
		initView();
	}
	
	private void initComponents() {
		useButtons = new HashMap<String, JButton>();
		useOnes = initButton(USE_ONES);
		useTwos = initButton(USE_TWOS);
		useThrees = initButton(USE_THREES);
		useFours = initButton(USE_FOURS);
		useFives = initButton(USE_FIVES);
		useSixes = initButton(USE_SIXES);
		useThreeOfAKind = initButton(USE_THREE_KIND);
		useFourOfAKind = initButton(USE_FOUR_KIND);
		useFullHouse = initButton(USE_FULL_HOUSE);
		useSmlStraight = initButton(USE_SMALL_STRAIGHT);
		useLngStraight = initButton(USE_LONG_STRAIGHT);
		useYahtzee = initButton(USE_YAHTZEE);
		useChance = initButton(USE_CHANCE);
		scoresForUseButtons = new HashMap<String, ScoreField>();
	}

	private JButton initButton(String name) {
		JButton button = new JButton("Use");
		useButtons.put(name, button);
		button.setName(name);
		button.setPreferredSize(new Dimension(60,20));
		button.setBorder(BorderFactory.createEmptyBorder());
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Score gameScore;
				JButton button = ((JButton)e.getSource());
				if (button.getText().equals("No!")) {
					gameScore = GameController.getInstance().undoLastUse();
					button.setText("Use");
				} else {
					String name = button.getName();
					gameScore = GameController.getInstance().use(name);
				}
				updateFromGameScore(gameScore);
			}
		});
		return button;
	}

	protected void updateFromGameScore(Score gameScore) {
		for (String name : NAMES) {
			if (gameScore.isUsed(name)) {
				scoresForUseButtons.get(name).setText(""+gameScore.get(name));
			} else {
				scoresForUseButtons.get(name).setText("-");
			}
		}
		scoreSectionOneSubTotal.setText(""+gameScore.getUpperSubtotal());
		scoreSectionOneBonus.setText(""+gameScore.getUpperBonus());
		scoreSectionOneTotal.setText(""+gameScore.getUpperTotal());
		scoreYahtzeeBonus.setText(""+gameScore.getYahtzeeBonus());
		scoreSectionTwoTotal.setText(""+gameScore.getLowerTotal());
		scoreTotal.setText(""+gameScore.getGrandTotal());
	}

	public void clearScores() {
		for (String name : NAMES) {
			scoresForUseButtons.get(name).setText("-");
		}
		scoreSectionOneSubTotal.setText("0");
		scoreSectionOneBonus.setText("0");
		scoreSectionOneTotal.setText("0");
		scoreYahtzeeBonus.setText("0");
		scoreSectionTwoTotal.setText("0");
		scoreTotal.setText("0");
	}
	
	public void enableUseButtons (Score gameScore) {
		for (String name : NAMES) {
			boolean isUsed = gameScore.isUsed(name);
			useButtons.get(name).setEnabled(!isUsed);
			useButtons.get(name).setText("Use");
		}
	}

	public void disableUseButtons (String nameToMakeUndo) {
		for (String name : NAMES) {
			if (name.equals(nameToMakeUndo)) {
				useButtons.get(name).setText("No!");
			} else {
				useButtons.get(name).setEnabled(false);
			}
		}
	}

	private void initView() {
		JPanel left = new JPanel(new RiverLayout());
		JPanel right = new JPanel(new RiverLayout());
		left.setBorder(BorderFactory.createEtchedBorder());
		right.setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridBagLayout());
		GridBagConstraints gbcLeft = 
				new GridBagConstraints(0, 0, 1, 1, 0.5, 1.0, 
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
						new Insets(0,0,0,0), 0, 0);
		GridBagConstraints gbcRight = 
				new GridBagConstraints(1, 0, 1, 1, 0.5, 1.0, 
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, 
						new Insets(0,0,0,0), 0, 0);
		add(left, gbcLeft);	
		add(right, gbcRight);	
//		add ("", new JLabel("Upper Section"));
		
		addUseRow(left, useOnes);
		addUseRow(left, useTwos);
		addUseRow(left, useThrees);
		addUseRow(left, useFours);
		addUseRow(left, useFives);
		addUseRow(left, useSixes);
		left.add ("br", new JLabel("Subtotal"));
		left.add ("tab", scoreSectionOneSubTotal);
		left.add ("br", new JLabel("Bonus"));
		left.add ("tab", scoreSectionOneBonus);
		left.add ("br", new JLabel("Lower total"));
		left.add ("tab", scoreSectionOneTotal);
		addUseRow(right, useThreeOfAKind);
		addUseRow(right, useFourOfAKind);
		addUseRow(right, useFullHouse);
		addUseRow(right, useSmlStraight);
		addUseRow(right, useLngStraight);
		addUseRow(right, useYahtzee);
		addUseRow(right, useChance);
		right.add ("br", new JLabel("Yahtzee Bonus"));
		right.add ("tab", scoreYahtzeeBonus);
		right.add ("br", new JLabel("Subtotal"));
		right.add ("tab", scoreSectionTwoTotal);
		right.add ("br", new JLabel("Total"));
		right.add ("tab", scoreTotal);
	}

	private void addUseRow(JPanel panel, JButton useButton) {
		ScoreField scoreField = new ScoreField();
		scoresForUseButtons.put(useButton.getName(), scoreField );
		panel.add ("br", new JLabel(useButton.getName()));
		panel.add ("tab", scoreField);
		panel.add ("tab", useButton);
		
	}

}
