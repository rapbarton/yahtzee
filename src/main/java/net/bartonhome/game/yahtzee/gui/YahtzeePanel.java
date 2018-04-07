package net.bartonhome.game.yahtzee.gui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.bartonhome.game.yahtzee.service.Dice;

public class YahtzeePanel extends JPanel {
	private static final long serialVersionUID = -5725523842677271505L;
	static YahtzeePanel instance = null;
	private InfoPanel infoPanel;
	private DicePanel dicePanel;
	private ScoreSheetPanel scoreSheetPanel;
	
	public static YahtzeePanel getInstance() {
		if (null == instance) {
			if (SwingUtilities.isEventDispatchThread()) {
				instance = new YahtzeePanel();
			} else {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						public void run() {
							instance = new YahtzeePanel();
						}
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return instance;
	}
	
	private YahtzeePanel () {
		setLayout(new BorderLayout());
		addInfoPanel();
		addDicePanel();
		addScoreSheetPanel();
	}

	private void addInfoPanel() {
		setInfoPanel(new InfoPanel());
		add(getInfoPanel(), BorderLayout.NORTH);
	}

	private void addDicePanel() {
		setDicePanel(new DicePanel());
		add(getDicePanel(), BorderLayout.CENTER);
	}

	private void addScoreSheetPanel() {
		setScoreSheetPanel(new ScoreSheetPanel());
		add(getScoreSheetPanel(), BorderLayout.SOUTH);
	}

	public InfoPanel getInfoPanel() {
		return infoPanel;
	}

	public void setInfoPanel(InfoPanel infoPanel) {
		this.infoPanel = infoPanel;
	}

	public DicePanel getDicePanel() {
		return dicePanel;
	}

	public void setDicePanel(DicePanel dicePanel) {
		this.dicePanel = dicePanel;
	}

	public ScoreSheetPanel getScoreSheetPanel() {
		return scoreSheetPanel;
	}

	public void setScoreSheetPanel(ScoreSheetPanel scoreSheetPanel) {
		this.scoreSheetPanel = scoreSheetPanel;
	}

	public Dice[] getDice() {
		return getDicePanel().getDice();
	}
}
