package microwave.main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hu.elte.txtuml.api.stdlib.world.SwingWorldObjectListener;
import microwave.model.DoorClosed;
import microwave.model.DoorOpened;
import microwave.model.OvenIsOff;
import microwave.model.OvenIsOn;
import microwave.model.View;

public class UI extends SwingWorldObjectListener implements Runnable {

	protected JLabel ovenClosedImage;
	protected JLabel ovenOpenImage;
	protected JLabel ovenCookingImage;

	protected JLabel currentImage;

	private Model model;

	@Override
	public void run() {
		model = new Model();
		register(model.start().get(), View.id());

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(createMainPanel());
		frame.pack();

		frame.setVisible(true);
	}

	/**
	 * Creates the main panel that will contain the images and the buttons.
	 * @return
	 */
	protected JPanel createMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(createOvenPanel());
		mainPanel.add(createButtonPanel());

		return mainPanel;
	}
	
	/**
	 * Creates the oven panel. This will contain the images.
	 */
	protected JPanel createOvenPanel() {
		JPanel ovenPanel = new JPanel();

		ovenClosedImage = new JLabel(getImageIcon("ovenclosed.png"));
		ovenOpenImage = new JLabel(getImageIcon("ovenopen.png"));
		ovenCookingImage = new JLabel(getImageIcon("ovencook.png"));

		ovenPanel.setBackground(new Color(255, 255, 255));
		ovenPanel.add(ovenClosedImage);
		ovenPanel.add(ovenOpenImage);
		ovenPanel.add(ovenCookingImage);

		currentImage = ovenClosedImage;
		ovenOpenImage.setVisible(false);
		ovenCookingImage.setVisible(false);

		return ovenPanel;
	}

	/**
	 * Creates the panel that will contain the buttons;
	 */
	protected JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		JButton openOrCloseButton = new JButton("Open/Close");
		JButton startButton = new JButton("Start");
		JButton stopButton = new JButton("Stop");

		openOrCloseButton.addActionListener(e -> model.openOrCloseDoor());
		startButton.addActionListener(e -> model.startClock());
		stopButton.addActionListener(e -> model.stopClock());

		buttonPanel.add(openOrCloseButton);
		buttonPanel.add(startButton);
		buttonPanel.add(stopButton);

		return buttonPanel;
	}

	private ImageIcon getImageIcon(String filename) {
		try {
			return new ImageIcon(ImageIO.read(new File("src/microwave/images/" + filename)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected void changeImage(JLabel to) {
		currentImage.setVisible(false);
		to.setVisible(true);
		currentImage = to;
	}

	// Signal handlers

	@SignalHandler
	public void doorOpened(DoorOpened sig) {
		changeImage(ovenOpenImage);
	}

	@SignalHandler
	public void doorClosed(DoorClosed sig) {
		changeImage(ovenClosedImage);
	}

	@SignalHandler
	public void ovenIsOn(OvenIsOn sig) {
		changeImage(ovenCookingImage);
	}

	@SignalHandler
	public void ovenIsOff(OvenIsOff sig) {
		// It is possible that the oven is switched off
		// because of the opened door
		if (currentImage == ovenCookingImage) {
			changeImage(ovenClosedImage);
		}
	}

	public static void main(String[] args) {
		new UI().run();
	}

}
