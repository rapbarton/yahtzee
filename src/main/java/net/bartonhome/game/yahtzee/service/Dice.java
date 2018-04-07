package net.bartonhome.game.yahtzee.service;

public interface Dice {
	public void setValue (int value);
	public int getValue ();
	public int getSides();
	public boolean isHeld();
	public void setHold(boolean isHeld);
}
