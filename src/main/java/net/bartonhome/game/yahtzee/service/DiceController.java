package net.bartonhome.game.yahtzee.service;

import java.util.concurrent.ThreadLocalRandom;

public class DiceController {
	static private DiceController instance = null;
	
	public static DiceController getInstance() {
		if (null == instance) instance = new DiceController();
		return instance;
	}
	
	public void throwDice(Dice dice) {
		int rand = ThreadLocalRandom.current().nextInt(1,7);
		dice.setValue(rand);
	}
}
