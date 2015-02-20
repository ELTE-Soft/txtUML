package txtuml.examples.garage.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import txtuml.examples.garage.control.model.Glue;
import txtuml.examples.garage.interfaces.IControl;
import txtuml.examples.garage.interfaces.IControlled;

public class UI implements IControlled {
	private GaragePanel garagePanel = new GaragePanel(this);
	private RemotePanel remotePanel = new RemotePanel(this);
	private JPanel spacePanel = new JPanel();
	private JPanel labelPanel = new JPanel();
	private JLabel statusLabel = new JLabel("Alarmed");
	private JProgressBar progressBar = new JProgressBar(0,100); 
	private KeypadPanel keypadPanel = new KeypadPanel(this);
	private JPanel controlPanel = new JPanel();
	private JPanel alarmPanel = new JPanel();
	private JPanel mainPanel = new JPanel();
	private JFrame frame = new JFrame("Garage");
	IControl control;
	
	private UI() {
		control = Glue.getInstance();
		((Glue)control).setControlled(this);
		alarmPanel.setLayout(new BoxLayout(alarmPanel, BoxLayout.Y_AXIS));
		controlPanel.setLayout(new BorderLayout());
		controlPanel.add(remotePanel,BorderLayout.NORTH);
		controlPanel.setBackground(Color.WHITE);
		labelPanel.setBackground(Color.RED);
		labelPanel.setPreferredSize(new Dimension(remotePanel.getWidth(),30));
		labelPanel.setMaximumSize(new Dimension(remotePanel.getWidth(),30));
		labelPanel.setLayout(new BorderLayout());
		labelPanel.add(statusLabel,BorderLayout.CENTER);
		alarmPanel.add(labelPanel);
		alarmPanel.add(progressBar);
		alarmPanel.add(keypadPanel);
		controlPanel.add(alarmPanel, BorderLayout.SOUTH);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(garagePanel);
		mainPanel.add(controlPanel);

		int spaceHeight = garagePanel.getHeight()
						- remotePanel.getHeight()
						- labelPanel.getHeight()
						- progressBar.getHeight()
						- keypadPanel.getHeight(); 
		spacePanel.setPreferredSize(new Dimension(remotePanel.getWidth(),spaceHeight));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new UI();
	}
	
	public void stopDoor() {
		garagePanel.direction = GaragePanel.Direction.STOPPED;
	}
	
	public void startDoorDown() {
		garagePanel.direction = GaragePanel.Direction.DOWN;
		if(!garagePanel.motor.isAlive()) {
			garagePanel.motor = garagePanel.new Motor(garagePanel);
			garagePanel.motor.start();
		}
	}
	
	public void startDoorUp() {
		garagePanel.direction = GaragePanel.Direction.UP;
		if(!garagePanel.motor.isAlive()) {
			garagePanel.motor = garagePanel.new Motor(garagePanel);
			garagePanel.motor.start();
		}
	}
	
	public void startSiren() {
		garagePanel.sirenActive = true;
		if(!garagePanel.siren.isAlive()) {
			garagePanel.siren = garagePanel.new Siren(garagePanel);
			garagePanel.siren.start();
		}
	}

	public void stopSiren() {
		garagePanel.sirenActive = false;
	}

	public void codeExpected() {
		labelPanel.setBackground(Color.ORANGE);
		statusLabel.setText("Enter code");
	}
	
	public void oldCodeExpected() {
		labelPanel.setBackground(Color.ORANGE);
		statusLabel.setText("Enter old code");
	}
	
	public void newCodeExpected() {
		labelPanel.setBackground(Color.ORANGE);
		statusLabel.setText("Enter new code");
	}
	
	public void progress(int percent) {
		progressBar.setValue(percent);
	}
	
	public void alarmOff() {
		progressBar.setValue(progressBar.getMinimum());
		labelPanel.setBackground(Color.GREEN);
		statusLabel.setText("Alarm off");
	}
	
	public void alarmOn() {
		labelPanel.setBackground(Color.RED);
		statusLabel.setText("Alarm on");		
	}
	
}

class GaragePanel extends ImagePanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage doorImg;
	private int doorLift = 0;
	private final int doorX = 147;
	private final int doorY = 178;
	Motor motor = new Motor(this);
	public enum Direction { STOPPED, UP, DOWN };
	public Direction direction;
	private UI parent;
	Siren siren = new Siren(this);
	boolean sirenActive = false;
	private boolean sirenLamp = false;
	private BufferedImage sirenImg1;
	private BufferedImage sirenImg2;
	private final int sirenX = 103;
	private final int sirenY = 101;

	GaragePanel(UI p) {
		super("src/txtuml/examples/garage/images/garage.jpg");
		parent = p;
		try {
		    doorImg = ImageIO.read(new File("src/txtuml/examples/garage/images/door.jpg"));
		    sirenImg1 = ImageIO.read(new File("src/txtuml/examples/garage/images/siren1.jpg"));
		    sirenImg2 = ImageIO.read(new File("src/txtuml/examples/garage/images/siren2.jpg"));
		} catch (IOException e) {
			System.out.println("Error: Cannot load some image.");
		}
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				Rectangle doorRect = new Rectangle(doorX, doorY, doorImg.getWidth(), doorImg.getHeight());
				if( doorRect.contains(me.getPoint())) {
					parent.control.motionSensorActivated();
					parent.control.alarmSensorActivated();
					setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				} else {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
	}
		
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage lifted = doorImg.getSubimage(0, doorLift, doorImg.getWidth(), doorImg.getHeight()-doorLift);
		g.drawImage(lifted,doorX,doorY,null);
		if(sirenLamp) {
			g.drawImage(sirenImg2, sirenX, sirenY, null);
		} else {
			g.drawImage(sirenImg1, sirenX, sirenY, null);
		}
	}
	
	class Motor extends Thread {
		private GaragePanel parent;
		
		public Motor(GaragePanel p) {
			parent = p;
		}
		
		public void run() {
			try {
				while((direction == Direction.UP && doorLift + 10 < doorImg.getHeight())
				   || (direction == Direction.DOWN && doorLift > 0)) {
					if(direction == Direction.UP)
						++doorLift;
					else if(direction == Direction.DOWN)
						--doorLift;
					parent.repaint();
					parent.parent.control.alarmSensorActivated();
				    Thread.sleep(100);
				}
				if(direction == Direction.UP) {
					parent.parent.control.doorReachedTop();
				} else if(direction == Direction.DOWN) {
					parent.parent.control.doorReachedBottom();
				}
			} catch(InterruptedException ex) {
			}
		}
	}
	
	class Siren extends Thread {
		private GaragePanel parent;
		private String sirenFile = "src/txtuml/examples/garage/sounds/siren.wav";
		private boolean soundOn = false;
		private Clip clip;
		
		public Siren(GaragePanel p) {
			parent = p;
		}
		
		public void run() {
			try {
				while(sirenActive) {
					sirenLamp = !sirenLamp;
					parent.repaint();
					for(int i=0; i<50; ++i) {
						if(!soundOn) {
							if(clip != null) {
			                	clip.stop();
			                	clip.close();
							}
							playSirenSound();
						}
					    Thread.sleep(10);
					}
				}
				sirenLamp = false;
				parent.repaint();
			} catch(InterruptedException ex) {
			}
		}
		
		private void playSirenSound() {
			try {
		    	File soundFile = new File(sirenFile);
		    	AudioInputStream soundIn = AudioSystem.getAudioInputStream(soundFile);
		    	AudioFormat format = soundIn.getFormat();
		    	DataLine.Info info = new DataLine.Info(Clip.class, format);
		    	clip = (Clip)AudioSystem.getLine(info);
		        clip.addLineListener(new LineListener()
		        {
		            @Override
		            public void update(LineEvent event)
		            {
		                if (event.getType() == LineEvent.Type.STOP) {
		                	soundOn = false;
		                }
		            }
		        });
		    	clip.open(soundIn);
		    	clip.start();
		    	soundOn = true;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

	}
}

class RemotePanel extends ImagePanel {
	private static final long serialVersionUID = 1L;
	private UI parent;
	private final Point buttonCenter = new Point(119,62);
	private final int buttonSize = 20;

	RemotePanel(UI p) {
		super("src/txtuml/examples/garage/images/remote.jpg");
		parent = p;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if(buttonCenter.distance(me.getPoint()) < buttonSize) {
					parent.control.remoteControlButtonPressed();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				if(buttonCenter.distance(me.getPoint()) < buttonSize) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
	}
}

class KeypadPanel extends ImagePanel {
	private static final long serialVersionUID = 1L;
	private UI parent;
	private Rectangle[] buttons = new Rectangle[10];
	private Rectangle buttonStar;
	private Rectangle buttonHash;

	KeypadPanel(UI p) {
		super("src/txtuml/examples/garage/images/keypad.jpg");
		parent = p;
		buttons[0] = new Rectangle(76,170,49,27);
		buttons[1] = new Rectangle(8,28,49,27);
		buttons[2] = new Rectangle(76,28,49,27);
		buttons[3] = new Rectangle(143,28,49,27);
		buttons[4] = new Rectangle(8,75,49,27);
		buttons[5] = new Rectangle(76,75,49,27);
		buttons[6] = new Rectangle(143,75,49,27);
		buttons[7] = new Rectangle(8,122,49,27);
		buttons[8] = new Rectangle(76,122,49,27);
		buttons[9] = new Rectangle(143,122,49,27);
		buttonStar = new Rectangle(8,170,49,27);
		buttonHash = new Rectangle(143,170,49,27);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				for(int i=0; i<10; ++i) {
					if(buttons[i].contains(me.getPoint())) {
						parent.control.keyPress(i);
					}
				}
				if(buttonStar.contains(me.getPoint())) {
					parent.control.starPressed();
				}
				if(buttonHash.contains(me.getPoint())) {
					parent.control.hashPressed();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				boolean onButton = false;
				for(int i=0; i<10; ++i) {
					if(buttons[i].contains(me.getPoint())) {
						onButton = true;
					}
				}
				if(buttonStar.contains(me.getPoint())) {
					onButton = true;
				}
				if(buttonHash.contains(me.getPoint())) {
					onButton = true;
				}
				if(onButton) {
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				} else {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}
			}
		});
	}
}
