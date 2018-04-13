package net.bartonhome.game.yahtzee.gui;

import java.awt.Color;

import javax.swing.JTextField;

public class ScoreField extends JTextField {
	private static final int DEFAULT_WIDTH = 5;
	public ScoreField (String initialValue) {
		super(initialValue, DEFAULT_WIDTH);
		setEditable(false);
		setBackground(Color.WHITE);
	}
	public ScoreField() {
		this("-");
	}
}
