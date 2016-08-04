package tester_GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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

import hu.elte.txtuml.api.model.execution.ModelExecutor;

import java.util.Timer;
import java.util.TimerTask;

import tester_GUI.Glue;
import tester_GUI.IControl;

public class GUITester {
	private MachinePanel machinePanel = new MachinePanel(this);
	private JPanel buttonPanel = new JPanel();
	private JFrame frame = new JFrame("Vending Machine");
	
	private JButton cola = new JButton("Highway Cola");
	private JButton orange = new JButton("Fantom Orange");
	private JButton aqua = new JButton("Fresh Aqua");
	private JButton stripe = new JButton("Stripe");
	private JButton returnCash = new JButton("RETURN");
	
	private JButton ten = new JButton("10");
	private JButton twenty = new JButton("20");
	private JButton fifty = new JButton("50");
	private JButton hundred = new JButton("100");
	private JButton twohundred = new JButton("200");
	
	private JPanel cashPanel = new JPanel();
	
	private JLabel monitor = new JLabel();
	
	private JTextField refiller = new JTextField();
	
	IControl control;
	Timer timer;
	
	private GUITester() {
		control = Glue.getInstance();
		
		

		buttonPanel.setOpaque(false);
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// ImageIcon colaImage = new ImageIcon(getClass().getResource("/images/Button_Cola.png"));
		
		monitor.setPreferredSize(new Dimension(150,100));
		monitor.setBackground(new Color (0, 0, 100));
		monitor.setHorizontalAlignment(0);
		monitor.setOpaque(true);
		monitor.setForeground(Color.GREEN);
		monitor.setFont(new Font("Courier New",0,14));
		monitor.setText(control.showMessage());
		monitor.setVisible(true);
		
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(15,0,0,0);
		buttonPanel.add(monitor,c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(20,10,0,0);
		buttonPanel.add(cola,c);
			cola.setBackground(Color.RED);
			cola.setOpaque(true);
			cola.setPreferredSize(new Dimension(150,50));
			
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(10,10,0,0);
		buttonPanel.add(orange,c);
			orange.setBackground(Color.ORANGE);
			cola.setOpaque(true);
			orange.setPreferredSize(new Dimension(150,50));
			
		c.gridx = 1;
		c.gridy = 4;
		buttonPanel.add(aqua,c);
			aqua.setBackground(Color.CYAN);
			orange.setOpaque(true);
			aqua.setPreferredSize(new Dimension(150,50));
			
		c.gridx = 1;
		c.gridy = 5;
		buttonPanel.add(stripe,c);
			stripe.setBackground(Color.GREEN);
			stripe.setOpaque(true);
			stripe.setPreferredSize(new Dimension(150,50));
			
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(10,10,0,0);
		buttonPanel.add(returnCash,c);
			returnCash.setBackground(Color.LIGHT_GRAY);
			returnCash.setOpaque(true);
			returnCash.setPreferredSize(new Dimension(90,30));
			returnCash.setMargin(new Insets(0,0,0,0));
		
		cashPanel.add(refiller);
			refiller.setPreferredSize(new Dimension(40,20));
			
		
		machinePanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(-50,160,0,20);
		buttonPanel.setVisible(true);
		
		machinePanel.add(buttonPanel,c);
		machinePanel.setVisible(true);
		
		
		cashPanel.setBackground(Color.LIGHT_GRAY);
		cashPanel.setVisible(true);
		cashPanel.setPreferredSize(new Dimension(frame.getPreferredSize().width, 50));
		cashPanel.setLayout(new FlowLayout());
		cashPanel.add(ten);
		cashPanel.add(twenty);
		cashPanel.add(fifty);
		cashPanel.add(hundred);
		cashPanel.add(twohundred);
		
		// TODO these are the coin inserter buttons. they're going to be real coins
		ten.setBackground(Color.YELLOW);
		ten.setOpaque(true);
		ten.setPreferredSize(new Dimension(70,25));
		ten.setMargin(new Insets(0,0,0,0));
		
		twenty.setBackground(Color.YELLOW);
		twenty.setOpaque(true);
		twenty.setPreferredSize(new Dimension(70,25));
		twenty.setMargin(new Insets(0,0,0,0));		
		
		fifty.setBackground(Color.YELLOW);
		fifty.setOpaque(true);
		fifty.setPreferredSize(new Dimension(70,25));
		fifty.setMargin(new Insets(0,0,0,0));

		hundred.setBackground(Color.YELLOW);
		hundred.setOpaque(true);
		hundred.setPreferredSize(new Dimension(70,25));
		hundred.setMargin(new Insets(0,0,0,0));
		
		twohundred.setBackground(Color.YELLOW);
		twohundred.setOpaque(true);
		twohundred.setPreferredSize(new Dimension(70,25));
		twohundred.setMargin(new Insets(0,0,0,0));
	
		
		
		frame.add(cashPanel, BorderLayout.NORTH);
		frame.add(machinePanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		
		
		
		// BUTTON ACTIONS

		cola.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (refiller.getText().equals(""))
					control.pressCola();
				else {
					control.refillCola(Integer.parseInt(refiller.getText()));
					refiller.setText("");
				}
			}
		});
		
		orange.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (refiller.getText().equals(""))
					control.pressFantom();
				else {
					control.refillFantom(Integer.parseInt(refiller.getText()));
					refiller.setText("");
				}
			}
		});
		
		aqua.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (refiller.getText().equals(""))
					control.pressAqua();
				else {
					control.refillAqua(Integer.parseInt(refiller.getText()));
					refiller.setText("");
				}
			}
		});
		
		stripe.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (refiller.getText().equals(""))
					control.pressStripe();
				else {
					control.refillStripe(Integer.parseInt(refiller.getText()));
					refiller.setText("");
				}
			}
		});
		
		returnCash.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				control.cashReturn();
				playReturnSound();
			}
		});
		
		ten.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				playCoinSound();
				control.insertTen();
			}
		});
		
		twenty.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				playCoinSound();
				control.insertTwenty();
			}
		});
		
		fifty.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				playCoinSound();
				control.insertFifty();
			}
		});
		
		hundred.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				playCoinSound();
				control.insertHundred();
			}
		});
		
		twohundred.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				playCoinSound();
				control.insertTwoHundred();		
			}
		});
		
		// SETTING UP TIMER TO UPDATE THE MONITOR
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				monitor.setText(control.showMessage());
			}
		}, 100, 100);
		
	}
	
	void playCoinSound() {
		String soundName = "src/sounds/coin.wav";    
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
	
	void playReturnSound() {
		String soundName = "src/sounds/return.wav";    
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
	
	public static void main(String[] args) {
		ModelExecutor.create().setTraceLogging(false).launch( () -> new GUITester() );
	}


}

class MachinePanel extends ImagePanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage machineImg;
	private GUITester parent;

	MachinePanel(GUITester p) {
		super("src/images/VendingMachine.png");
		parent = p;
		try {
		    machineImg = ImageIO.read(new File("src/images/VendingMachine.png"));
		} catch (IOException e) {
			System.out.println("Error: Cannot load some image.");
		}
	}
}
