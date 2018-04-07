package net.bartonhome.game.yahtzee.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.bartonhome.game.yahtzee.service.GameController;

public class InfoPanel extends JPanel {
	JTextField jtfHighScore;
	JButton startNewGame;
	
	public InfoPanel() {
		initView();
	}

	private void initView() {
		setLayout(new RiverLayout());
		startNewGame = new JButton("Start New Game");
		add ("", startNewGame);
		add ("tab", new JLabel("     "));
		add ("tab", new JLabel("High Score"));
		jtfHighScore = new JTextField("0", 10);
		add ("tab hfill", jtfHighScore);
		
		startNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameController.getInstance().startNewGame();
			}
		});
	}

	public void setHighScore(int highScore) {
		jtfHighScore.setText(""+highScore);
		
	}
	
	
}
