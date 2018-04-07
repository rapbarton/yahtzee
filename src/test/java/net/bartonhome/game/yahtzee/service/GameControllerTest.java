package net.bartonhome.game.yahtzee.service;

import net.bartonhome.game.yahtzee.ScoreConstants;

import org.junit.Test;

import junit.framework.TestCase;

public class GameControllerTest extends TestCase {
	@Test
	public void testGetScoreForName() {
		GameController uut = GameController.getInstance();
		
		assertEquals(5, uut.getScoreForName(ScoreConstants.USE_ONES, new int[]{1,1,1,1,1}));
		assertEquals(0, uut.getScoreForName(ScoreConstants.USE_ONES, new int[]{2,3,4,5,6}));
		assertEquals(2, uut.getScoreForName(ScoreConstants.USE_ONES, new int[]{1,3,4,5,1}));
		assertEquals(4, uut.getScoreForName(ScoreConstants.USE_TWOS, new int[]{1,2,4,5,2}));
		
		assertEquals(25, uut.getScoreForName(ScoreConstants.USE_FULL_HOUSE, new int[]{2,2,3,3,3}));
		assertEquals(0,  uut.getScoreForName(ScoreConstants.USE_FULL_HOUSE, new int[]{1,2,3,3,3}));
		assertEquals(25, uut.getScoreForName(ScoreConstants.USE_FULL_HOUSE, new int[]{3,3,3,3,3}));
		
		
	}
	
	@Test
	public void testQtyOfNumbersInScore() {
		GameController uut = GameController.getInstance();
		assertEquals(3, uut.qtyOfNumbersInScore(1, new int[]{2,1,1,1,4}));
	}
}
