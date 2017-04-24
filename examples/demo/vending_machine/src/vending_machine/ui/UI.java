package vending_machine.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import vending_machine.glue.Model;
import vending_machine.glue.ViewImpl;

public class UI implements Runnable {

	private static final String ASSETS_FOLDER = "src/vending_machine/ui";
	private static final String PANEL_IMAGE = ASSETS_FOLDER + "/images/VendingMachine.png";
	private static final String COIN_SOUND = ASSETS_FOLDER + "/sounds/coin.wav";
	private static final String RETURN_SOUND = ASSETS_FOLDER + "/sounds/return.wav";
	
	private ImagePanel machinePanel = new ImagePanel(PANEL_IMAGE);
	private JPanel buttonPanel = new JPanel();
	private JFrame frame = new JFrame("Vending Machine");

	private JButton[] drinkButtons = new JButton[] { createDrinkButton(Model.COLA_NAME, Color.RED),
			createDrinkButton(Model.PHANTOM_NAME, Color.ORANGE), createDrinkButton(Model.AQUA_NAME, Color.CYAN),
			createDrinkButton(Model.STRIPE_NAME, Color.GREEN) };

	private JButton returnCash = new JButton("RETURN");

	private JButton[] coinButtons = new JButton[] { createCoinButton(10), createCoinButton(20), createCoinButton(50),
			createCoinButton(100), createCoinButton(200) };

	private JPanel cashPanel = new JPanel();

	private JLabel monitor = new JLabel();

	private JTextField refiller = new JTextField();

	private final Model model;

	private UI() {
		ViewImpl.getInstance().setUI(this);
		model = new Model();

		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		monitor.setPreferredSize(new Dimension(150, 100));
		monitor.setBackground(new Color(0, 0, 100));
		monitor.setHorizontalAlignment(0);
		monitor.setOpaque(true);
		monitor.setForeground(Color.GREEN);
		monitor.setFont(new Font("Courier New", 0, 14));
		monitor.setText("");

		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(15, 0, 0, 0);
		buttonPanel.add(monitor, c);

		c.gridx = 1;
		int y = 1;
		c.insets = new Insets(10, 10, 0, 0);
		for (JButton b : drinkButtons) {
			c.gridy = y;
			buttonPanel.add(b, c);
			++y;
		}

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(10, 10, 0, 0);
		buttonPanel.add(returnCash, c);
		returnCash.setBackground(Color.LIGHT_GRAY);
		returnCash.setOpaque(true);
		returnCash.setPreferredSize(new Dimension(90, 30));
		returnCash.setMargin(new Insets(0, 0, 0, 0));

		cashPanel.add(refiller);
		refiller.setPreferredSize(new Dimension(40, 20));

		machinePanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(-50, 160, 0, 20);

		machinePanel.add(buttonPanel, c);

		cashPanel.setBackground(Color.LIGHT_GRAY);
		cashPanel.setVisible(true);
		cashPanel.setPreferredSize(new Dimension(frame.getPreferredSize().width, 50));
		cashPanel.setLayout(new FlowLayout());

		// TODO these are the coin inserter buttons. they're going to be real
		// coins

		for (JButton b : coinButtons) {
			cashPanel.add(b);
		}

		returnCash.addActionListener(e -> {
			model.cashReturn();
			playReturnSound();
		});

		frame.add(cashPanel, BorderLayout.NORTH);
		frame.add(machinePanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
	}

	@Override
	public void run() {
		frame.setVisible(true);
	}

	private JButton createDrinkButton(String drinkName, Color color) {
		JButton drinkButton = new JButton(drinkName);
		drinkButton.setBackground(Color.YELLOW);
		drinkButton.setOpaque(true);
		drinkButton.setPreferredSize(new Dimension(70, 25));
		drinkButton.setMargin(new Insets(0, 0, 0, 0));

		drinkButton.setBackground(color);
		drinkButton.setOpaque(true);
		drinkButton.setPreferredSize(new Dimension(150, 50));

		drinkButton.addActionListener(e -> {
			if (refiller.getText().equals(""))
				model.chooseDrink(drinkName);
			else {
				model.refillDrink(drinkName, Integer.parseInt(refiller.getText()));
				refiller.setText("");
			}
		});

		return drinkButton;
	}

	private JButton createCoinButton(int value) {
		JButton coinButton = new JButton(String.valueOf(value));
		coinButton.setBackground(Color.YELLOW);
		coinButton.setOpaque(true);
		coinButton.setPreferredSize(new Dimension(70, 25));
		coinButton.setMargin(new Insets(0, 0, 0, 0));

		coinButton.addActionListener(e -> {
			playCoinSound();
			model.insertCoin(value);
		});

		return coinButton;
	}

	private void playCoinSound() {
		playSound(COIN_SOUND);
	}

	private void playReturnSound() {
		playSound(RETURN_SOUND);
	}

	private void playSound(String soundName) {
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Thread-safe.
	 */
	public void showMessage(String message) {
		SwingUtilities.invokeLater(() -> {
			monitor.setText(message);
		});
	}

	public static void main(String[] args) {
		new UI().run();
	}

}
