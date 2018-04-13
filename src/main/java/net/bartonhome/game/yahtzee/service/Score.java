package net.bartonhome.game.yahtzee.service;

import java.util.HashMap;

import net.bartonhome.game.yahtzee.ScoreConstants;

public class Score implements ScoreConstants {
	private HashMap<String, Integer> namedScores;
	int yahtzeeBonus;
	String lastScoreName;
	private boolean lastScoreWasYahtzeeBonus;
	
	public Score () {
		namedScores = new HashMap<String, Integer>();
		yahtzeeBonus = 0;
		lastScoreName = "";
		lastScoreWasYahtzeeBonus = false;
	}
	
	public boolean isComplete(){
		return namedScores.size() == 13;
	}
	
	public boolean isUsed (String name) {
		return namedScores.containsKey(name);
	}

	public void use(String name, int score, boolean isYahtzee) {
		if (isUsed(name)) throw new GameException("Already used " + name);
		if (isYahtzee && isUsed(USE_YAHTZEE)) {
			yahtzeeBonus += 100;
			lastScoreWasYahtzeeBonus = true;
		}
		namedScores.put(name, score);
		lastScoreName = name;
	}
	
	public void undoLastUse () {
		if (lastScoreName.isEmpty()) return;
		if (lastScoreWasYahtzeeBonus) yahtzeeBonus -= 100;
		namedScores.remove(lastScoreName);
		lastScoreName = "";
	}
	
	public int get(String name) {
		if (isUsed(name)) {
			return namedScores.get(name);
		} else {
			return 0;
		}
	}
	
	public int getUpperSubtotal() {
		return get(USE_ONES) 
				 + get(USE_TWOS)
				 + get(USE_THREES)
				 + get(USE_FOURS)
				 + get(USE_FIVES)
				 + get(USE_SIXES);
	}

	public int getUpperBonus() {
		return getUpperSubtotal() >= 63 ? 35 : 0;
	}

	public int getUpperTotal() {
		return getUpperSubtotal() + getUpperBonus();
	}
	
	public int getLowerSubtotal () {
		return get(USE_THREE_KIND) 
				 + get(USE_FOUR_KIND)
				 + get(USE_FULL_HOUSE)
				 + get(USE_SMALL_STRAIGHT)
				 + get(USE_LONG_STRAIGHT)
				 + get(USE_CHANCE)
				 + get(USE_YAHTZEE);
	}
		
	public int getYahtzeeBonus () {
		return yahtzeeBonus;
	}

	public int getLowerTotal() {
		return getLowerSubtotal() + getYahtzeeBonus();
	}
	
	public int getGrandTotal() {
		return getUpperTotal() + getLowerTotal();
	}
	
}
