package net.bartonhome.game.yahtzee.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.SwingUtilities;

import net.bartonhome.game.yahtzee.ScoreConstants;
import net.bartonhome.game.yahtzee.gui.DiceView;
import net.bartonhome.game.yahtzee.gui.YahtzeePanel;

public class GameController implements ScoreConstants {
	private static final int SCORE_FULL_HOUSE = 25;
	private static final int SCORE_SMALL_STRAIGHT = 30;
	private static final int SCORE_LONG_STRAIGHT = 40;
	private static final int SCORE_YAHTZEE = 50;
	static private GameController instance = null;
	private DiceController diceController;
	private YahtzeePanel yahtzeeView;
	private DiceView[] diceViews;
	private int goThrow = 1;
	private Score gameScore;
	private int highScore = 0;
	private PlaySound sound = new PlaySound();
	private int lastGoThrow = 1;
	
	public static GameController getInstance() {
		if (null == instance) instance = new GameController();
		return instance;
	}
	
	private GameController() {
		diceController = DiceController.getInstance();
		yahtzeeView = YahtzeePanel.getInstance();
		highScore = readHighScore();
		yahtzeeView.getInfoPanel().setHighScore(highScore);
		startNewGame();
	}

	public void startNewGame() {
		goThrow = 1;
		gameScore = new Score();
		yahtzeeView.getDicePanel().setAllDice(1);
		yahtzeeView.getDicePanel().setFirstThrow(true);
		yahtzeeView.getScoreSheetPanel().clearScores();
		yahtzeeView.getDicePanel().setThrowButtonEnable(true);
	}

	public YahtzeePanel getGui() {
		return yahtzeeView;
	}
	
	public void throwDice() {
		int step = 1;
		doThrowStep(step);
		sound.rollDice();
	}
	
	private void doThrowStep(final int stepNumber) {
		if (stepNumber >= 10) {
			doThrowComplete();
		} else {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Dice[] dice = yahtzeeView.getDice();
					for (Dice die : dice) {
						if (!die.isHeld()) {
							diceController.throwDice(die);
						}
					}
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
					}
					doThrowStep(stepNumber + 1);
				}
			});
		} 
	}

	private void doThrowComplete() {
		yahtzeeView.getDicePanel().setFirstThrow(false);
		if (goThrow == 3) {
			yahtzeeView.getDicePanel().setThrowButtonEnable(false);
		} else {
			goThrow++;
		}
		yahtzeeView.getScoreSheetPanel().enableUseButtons(gameScore);	
		if (isYahtzee(getCurrentDiceScores())) {
			sound.woohoo();
		}
	}

//	public DiceView[] getDiceViews() {
//		return diceViews;
//	}
//
//	public void setDiceViews(DiceView[] diceViews) {
//		this.diceViews = diceViews;
//	}

	public void diceClicked(Dice source) {
		yahtzeeView.getDicePanel().clickHoldFor(source);		
	}

	public Score undoLastUse() {
		gameScore.undoLastUse();
		goThrow = lastGoThrow;
		yahtzeeView.getDicePanel().setThrowButtonEnable(lastGoThrow != 3);
		yahtzeeView.getScoreSheetPanel().enableUseButtons(gameScore);
		yahtzeeView.getDicePanel().setFirstThrow(false);
		return gameScore;
	}
	
	public Score use(String name) {
		int[] diceScore = getCurrentDiceScores();
		int points = getScoreForName(name,diceScore);
		gameScore.use(name, points, isYahtzee(diceScore));
		boolean ended = gameScore.isComplete();
		lastGoThrow  = goThrow;
		goThrow = 1;
		yahtzeeView.getDicePanel().setThrowButtonEnable(!ended);
		yahtzeeView.getScoreSheetPanel().disableUseButtons(ended?"":name);
		yahtzeeView.getDicePanel().setFirstThrow(true);
		if (ended) {
			endGame();
			if (gameScore.getGrandTotal() > highScore) {
				highScore = gameScore.getGrandTotal();
				yahtzeeView.getInfoPanel().setHighScore(highScore);
			}
		}
		return gameScore;
	}

	private void endGame() {
		if (gameScore.getGrandTotal() > highScore) {
			highScore = gameScore.getGrandTotal();
			yahtzeeView.getInfoPanel().setHighScore(highScore);
			sound.highScore();
			recordHighScore();
		}		
	}

	private void recordHighScore() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
		String scoreLine = sdf.format(new Date()) + ":" + highScore + "\n";
		File file = new File("YahtzeeHighScore");		
		try {
			FileWriter fw = new FileWriter(file);
			fw.append(scoreLine);
			fw.close();
		} catch (IOException e) {
		}
	}

	private int readHighScore() {
		int score = 0;
		File file = new File("YahtzeeHighScore");		
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while (null != (line = br.readLine())) {
				String[] bits = line.split(":");
				if (bits.length > 1) {
					try {
						int i = Integer.parseInt(bits[1]);
						score = i;
					} catch (NumberFormatException e) {
					}
				}
			}			
			fr.close();
		} catch (IOException e) {
		}
		return score;
	}

	private int[] getCurrentDiceScores() {
		Dice[] dice = yahtzeeView.getDice();
		int scores[] = new int[dice.length];
		for (int i = 0; i < dice.length; i++)  {
			scores[i] = dice[i].getValue();
		}
		return scores;
	}
	
	public int getScoreForName(String name, int[] score) {
		boolean isJoker = gameScore.isUsed(USE_YAHTZEE);
		switch (name) {
		case USE_ONES: return qtyOfNumbersInScore(1, score);
		case USE_TWOS: return qtyOfNumbersInScore(2, score)*2;
		case USE_THREES: return qtyOfNumbersInScore(3, score)*3;
		case USE_FOURS: return qtyOfNumbersInScore(4, score)*4;
		case USE_FIVES: return qtyOfNumbersInScore(5, score)*5;
		case USE_SIXES: return qtyOfNumbersInScore(6, score)*6;
		case USE_THREE_KIND: return isThreeOfKind(score)?sumOf(score):0;
		case USE_FOUR_KIND: return isFourOfKind(score)?sumOf(score):0;
		case USE_FULL_HOUSE: return isFullHouse(score)?SCORE_FULL_HOUSE:0;
		case USE_SMALL_STRAIGHT: return isSmallStraight(score)||(isYahtzee(score)&&isJoker)?SCORE_SMALL_STRAIGHT:0;
		case USE_LONG_STRAIGHT: return isLongStraight(score)||(isYahtzee(score)&&isJoker)?SCORE_LONG_STRAIGHT:0;
		case USE_YAHTZEE: return isYahtzee(score)?SCORE_YAHTZEE:0;
		case USE_CHANCE: return sumOf(score);
		}
		return 0;
	}
	
	private boolean isFourOfKind(int[] score) {
		for (int n = 1; n <= 6; n++) {
			if (qtyOfNumbersInScore(n, score) >= 4) return true;
		}
		return false;
	}

	private boolean isThreeOfKind(int[] score) {
		for (int n = 1; n <= 6; n++) {
			if (qtyOfNumbersInScore(n, score) >= 3) return true;
		}
		return false;
	}

	private int sumOf(int[] score) {
		int sum = 0;
		for (int i = 0; i < score.length; i++) {
			sum += score[i];
		}
		return sum;
	}

	private boolean isYahtzee(int[] score) {
		int n = score[0];
		for (int i = 1; i < score.length; i++) {
			if (n != score[i]) return false;
		}
		return true;
	}

	private boolean isLongStraight(int[] score) {
		boolean has1 = qtyOfNumbersInScore(1, score) > 0;
		boolean has2 = qtyOfNumbersInScore(2, score) > 0;
		boolean has3 = qtyOfNumbersInScore(3, score) > 0;
		boolean has4 = qtyOfNumbersInScore(4, score) > 0;
		boolean has5 = qtyOfNumbersInScore(5, score) > 0;
		boolean has6 = qtyOfNumbersInScore(6, score) > 0;
		
		if (has1 && has2 && has3 && has4 && has5) return true;
		if (has2 && has3 && has4 && has5 && has6) return true;
		
		return false;
	}

	private boolean isSmallStraight(int[] score) {
		boolean has1 = qtyOfNumbersInScore(1, score) > 0;
		boolean has2 = qtyOfNumbersInScore(2, score) > 0;
		boolean has3 = qtyOfNumbersInScore(3, score) > 0;
		boolean has4 = qtyOfNumbersInScore(4, score) > 0;
		boolean has5 = qtyOfNumbersInScore(5, score) > 0;
		boolean has6 = qtyOfNumbersInScore(6, score) > 0;
		
		if (has1 && has2 && has3 && has4) return true;
		if (has2 && has3 && has4 && has5) return true;
		if (has3 && has4 && has5 && has6) return true;
		
		return false;
	}

	private boolean isFullHouse(int[] score) {
		int a = 0;
		int b = 0;
		int countA = 0;
		int countB = 0;
		for (int s : score) {
			if (a == 0) {
				a = s;
				countA = 1;
			} else if (a == s) {
				countA++;
				continue;
			} else if (b == 0) {
				b = s;
				countB = 1;
			} else if (b == s) {
				countB++;
				continue;
			} else {
				return false;
			}
		}
		return countA == 2 || countA == 3 || countA == 5;
	}

	public int qtyOfNumbersInScore(int number, int[] score) {
		int qty = 0;
		for (int value : score) {
			if (number == value) qty++;
		}
		return qty;
	}

}
