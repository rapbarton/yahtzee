package net.bartonhome.game.yahtzee.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.bartonhome.game.yahtzee.service.Dice;
import net.bartonhome.game.yahtzee.service.DiceController;
import net.bartonhome.game.yahtzee.service.GameController;

public class DiceView extends JPanel implements Dice {
	private static final long serialVersionUID = -4927610210418684393L;
	private static final int SIDES = 6;
	private boolean isHeld;
	
	DiceController diceController;
	GameController gameController;
	
	int value = 1;
	int size = 60;
	int offset = 15;
	int m = 5;
	int a = 20;
	int dotSize = 10;

	public DiceView () {
		isHeld = false;
		setPreferredSize(new Dimension(size, size));
    setOpaque(false);
    //setBackground(Color.WHITE);
    addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				GameController.getInstance().diceClicked((Dice)e.getSource());
			}
		});
	}	
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.CYAN);
		g.fillRoundRect(m, m, size-(2*m), size-2*m, a, a);
		
		g.setColor(Color.BLACK);
		drawDotsBasedOnValue(g);
	}
	
	private void drawDotsBasedOnValue(Graphics g) {
		ArrayList<Point> points = getDotPointsForValue();
		for (Point point : points) {
			drawDotAt(g,point);
		}
	}
	
	private ArrayList<Point> getDotPointsForValue() {
		ArrayList<Point> points = new ArrayList<Point>();
		switch(value) {
		case 1:
			points.add(new Point(2,2));
			break;
		
		case 2:
			points.add(new Point(1,1));
			points.add(new Point(3,3));
			break;
		
		case 3:
			points.add(new Point(1,1));
			points.add(new Point(2,2));
			points.add(new Point(3,3));
			break;
		
		case 5:
			points.add(new Point(2,2));
		case 4:
			points.add(new Point(1,1));
			points.add(new Point(3,3));
			points.add(new Point(1,3));
			points.add(new Point(3,1));
			break;
		
		case 7:
			points.add(new Point(2,2));
		case 6:
			points.add(new Point(1,1));
			points.add(new Point(1,2));
			points.add(new Point(1,3));
			points.add(new Point(3,1));
			points.add(new Point(3,2));
			points.add(new Point(3,3));
			break;
		default:
		}
		return points;
	}

	private void drawDotAt(Graphics g, Point p) {
		int xx = size/2 - dotSize/2 + ((p.x-2) * offset);
		int yy = size/2 - dotSize/2 + ((p.y-2) * offset);
		g.fillOval(xx,yy,dotSize,dotSize);
	}

	public void setValue(int value) {
		this.value = value;
		this.repaint();
	}
	
	public int getSides() {
		return SIDES;
	}

	public int getValue() {
		return this.value;
	}

	public boolean isHeld() {
		return isHeld;
	}

	public void setHold(boolean isHeld) {
		this.isHeld = isHeld;
	}
}
