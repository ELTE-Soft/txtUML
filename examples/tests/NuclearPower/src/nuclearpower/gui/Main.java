package nuclearpower.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main extends JFrame{
	
	private static final String ASSETS_FOLDER = "/nuclearpower/gui/assets/";
	
	private JLabel powerPlantLabel = new JLabel("Power plant: Off");
	private JLabel solarPanelLabel = new JLabel("Solar panel: Off");
	private JLabel accumulatorLabel = new JLabel("Accumulator: Off");
	private JLabel houseLabel = new JLabel("House: Off");
	
	private JLabel powerPlantImagePanel = new JLabel();
	private JLabel solarPanelImagePanel = new JLabel();
	private JLabel houseImagePanel = new JLabel();
	private JLabel accumulatorImagePanel = new JLabel();
	
	private JButton solarPanelButton = new JButton("Change weather");
	private JButton houseButton = new JButton("Swicth");
	private JButton accumulatorButton = new JButton("AccBtn");

	public static void main(String[] args) {
		new Main().setVisible(true);
	}
	
	public Main(){
		super("Nuclear Power Station model");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    // set the jframe size and location
	    setPreferredSize(new Dimension(1200, 800));

	    createImages();
	    createLayout();
	    pack();
	    setLocationRelativeTo(null);
	}

	private void createImages() {
		ImageIcon powerPlantImage = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "powerstation_off.png"));
		powerPlantImagePanel.setIcon(new ImageIcon(powerPlantImage.getImage().getScaledInstance(300, 275, Image.SCALE_SMOOTH)));
		ImageIcon solarPanelImage = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "solarpanel_rainy.png"));
		solarPanelImagePanel.setIcon(new ImageIcon(solarPanelImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
		ImageIcon houseImage = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "house_off.png"));
		houseImagePanel.setIcon(new ImageIcon(houseImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
		ImageIcon accumulatorImage = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "accu_empty.png"));
		accumulatorImagePanel.setIcon(new ImageIcon(accumulatorImage.getImage().getScaledInstance(150, 300, Image.SCALE_SMOOTH)));

		Font font = new Font("Serif", Font.BOLD, 24);
		houseLabel.setFont(font);
		accumulatorLabel.setFont(font);
		solarPanelLabel.setFont(font);
		powerPlantLabel.setFont(font);
	}

	private void createLayout() {
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		//layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				   layout.createSequentialGroup()
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    		  .addComponent(houseLabel)
				    		  .addComponent(houseImagePanel)
				    		  .addComponent(houseButton))
				      .addGap(100)
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    		  .addComponent(solarPanelLabel)
				    		  .addComponent(solarPanelImagePanel)
				    		  .addComponent(solarPanelButton)
				    		  .addComponent(accumulatorLabel)
				    		  .addComponent(accumulatorImagePanel)
				    		  .addComponent(accumulatorButton))
				      .addGap(100)
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    		  .addComponent(powerPlantLabel)
				    		  .addComponent(powerPlantImagePanel))
				);
		layout.setVerticalGroup(
		   layout.createParallelGroup()
		   		.addGroup(layout.createSequentialGroup()
	   				.addComponent(houseLabel)
			        .addComponent(houseImagePanel)
			        .addComponent(houseButton))
		   		.addGap(100)
		      .addGroup(layout.createSequentialGroup()
	    		  	.addComponent(solarPanelLabel)
	    		  	.addComponent(solarPanelImagePanel)
	    		  	.addComponent(solarPanelButton)
	    		  	.addComponent(accumulatorLabel)
	    		  	.addComponent(accumulatorImagePanel)
	    		  	.addComponent(accumulatorButton))
		      .addGap(100)
		      .addGroup(layout.createSequentialGroup()  
		    		  .addComponent(powerPlantLabel)
		    		  .addComponent(powerPlantImagePanel))
		);
	}

}
