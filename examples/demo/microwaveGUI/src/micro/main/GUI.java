package micro.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import micro.model.DoorClosed;
import micro.model.DoorOpened;
import micro.model.MicrowaveOven;
import micro.model.Start;
import micro.model.Stop;

public class GUI implements GUIInterface {
	static MicrowaveOven oven;
	static JFrame frame;
	static JPanel mainPanel;
	static JPanel ovenPanel;
	static JPanel buttonPanel;
	static JButton openButton;
	static JButton closeButton;
	static JButton startButton;
	static JButton stopButton;
	static JButton testButton;
	static BufferedImage ovenClosed;
	static BufferedImage ovenOpen;
	static BufferedImage ovenCook;
	static BufferedImage ovenLamp;
	static JLabel ovenClosedLabel;
	static JLabel ovenOpenLabel;
	static JLabel ovenCookLabel;
	static JLabel ovenLampLabel;
	static Timer timer;

	static void init() {
		oven = Action.create(MicrowaveOven.class);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		ovenPanel = new JPanel();
		buttonPanel = new JPanel();
		try {
			ovenClosed = ImageIO.read(new File("src/micro/images/ovenclosed.png"));
			ovenOpen = ImageIO.read(new File("src/micro/images/ovenopen.png"));
			ovenCook= ImageIO.read(new File("src/micro/images/ovencook.png"));
			ovenLamp = ImageIO.read(new File("src/micro/images/ovenlamp.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		ovenClosedLabel = new JLabel(new ImageIcon(ovenClosed));
		ovenOpenLabel = new JLabel(new ImageIcon(ovenOpen));
		ovenCookLabel = new JLabel(new ImageIcon(ovenCook));
		ovenLampLabel = new JLabel(new ImageIcon(ovenLamp));
		ovenPanel.setBackground(new Color(255,255,255));
		ovenPanel.add(ovenClosedLabel);
		ovenPanel.add(ovenOpenLabel);
		ovenPanel.add(ovenCookLabel);
		ovenPanel.add(ovenLampLabel);
		ovenOpenLabel.setVisible(false);
		ovenCookLabel.setVisible(false);
		ovenLampLabel.setVisible(false);
		
		openButton = new JButton("Open");
		closeButton = new JButton("Close");
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		closeButton.setVisible(false);
		openButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ovenClosedLabel.setVisible(false);
				ovenCookLabel.setVisible(false);
				ovenLampLabel.setVisible(false);
				ovenOpenLabel.setVisible(true);
				openButton.setVisible(false);
				closeButton.setVisible(true);
				startButton.setEnabled(false);
				stopButton.setEnabled(false);
				Action.send(new DoorOpened(), oven);
			}
		});
		closeButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ovenOpenLabel.setVisible(false);
				ovenCookLabel.setVisible(false);
				ovenClosedLabel.setVisible(true);
				closeButton.setVisible(false);
				openButton.setVisible(true);
				startButton.setEnabled(true);
				stopButton.setEnabled(true);
				Action.send(new DoorClosed(), oven);
			}
		});

		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Action.send(new Start(), oven);
			}
		});
		stopButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Action.send(new Stop(), oven);
			}
		});
		buttonPanel.add(openButton);
		buttonPanel.add(closeButton);
		buttonPanel.add(startButton);
		buttonPanel.add(stopButton);
		mainPanel.add(ovenPanel);
		mainPanel.add(buttonPanel);
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
		Action.start(oven);
	}

	@Override
	public void lampOn() {
		ovenClosedLabel.setVisible(false);
		ovenLampLabel.setVisible(true);
	}

	@Override
	public void lampOff() {
		ovenLampLabel.setVisible(false);
		ovenClosedLabel.setVisible(true);
	}

	@Override
	public void magnetronOn() {
		ovenLampLabel.setVisible(false);
		ovenCookLabel.setVisible(true);
	}

	@Override
	public void magnetronOff() {
		ovenLampLabel.setVisible(true);
		ovenCookLabel.setVisible(false);
	}

	public static void main(String[] args) {
		ModelExecutor.create().setTraceLogging(true).start(GUI::init);
	}
}