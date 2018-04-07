package net.bartonhome.game.yahtzee.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.bartonhome.game.yahtzee.service.Dice;
import net.bartonhome.game.yahtzee.service.GameController;

public class DicePanel extends JPanel {
	private static final long serialVersionUID = -8356195211381251003L;
	private DiceView[] diceViews;
	private JToggleButton[] toggles;
	private JButton throwButton;
	
	public DicePanel() {
		initView();
	}
	
	private void initView() {
		setLayout(new RiverLayout());
		
		diceViews = new DiceView[5];
		String ws = "br";
		for (int dice = 0; dice < 5; dice++) {
			diceViews[dice] = new DiceView();
			add (ws, diceViews[dice]);
			ws = "tab";
		}
		throwButton = new JButton("Throw");
		add ("tab hfill", throwButton);
		
		toggles = new JToggleButton[5];
		ws = "br";
		for (int dice = 0; dice < 5; dice++) {
			final Dice die = diceViews[dice];
			final JToggleButton button  = new JToggleButton("hold"); 
			toggles[dice] = button;
			toggles[dice].setPreferredSize(new Dimension(60,20));
			toggles[dice].setBorder(BorderFactory.createEmptyBorder());
			toggles[dice].addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					boolean selected = ((JToggleButton)e.getSource()).isSelected();
					die.setHold(selected);
				}
			});
			((DiceView)die).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					button.doClick();
				}
			});
			add (ws, toggles[dice]);
			ws = "tab";
		}
		
		throwButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameController.getInstance().throwDice();				
			}
		});
	}

	public Dice[] getDice() {
		return diceViews;
	}

	public void setThrowButtonEnable(boolean enable) {
		throwButton.setEnabled(enable);
	}

	public void setFirstThrow(boolean isFirstThrow) {
		if (isFirstThrow) {
			for (JToggleButton tButton : toggles) {
				tButton.setSelected(false);
				tButton.setEnabled(false);
			}
		} else {
			for (JToggleButton tButton : toggles) {
				tButton.setEnabled(true);
			}		
		}
	}

	public void clickHoldFor(Dice dice) {
		dice.setHold(!dice.isHeld());
		
	}
	
}
