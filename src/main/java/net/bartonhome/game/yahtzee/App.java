package net.bartonhome.game.yahtzee;

import net.bartonhome.game.yahtzee.gui.BasicFrame;
import net.bartonhome.game.yahtzee.service.GameController;

/**
 * Run Yahtzee
 *
 */
public class App 
{
	public static void main( String[] args ) {
		GameController gameController = GameController.getInstance();
    BasicFrame.launch("Yahtzee", gameController.getGui());
  }
}
