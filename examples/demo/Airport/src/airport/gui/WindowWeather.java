package airport.gui;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WindowWeather extends JPanel {
	private static final long serialVersionUID = -8450091623583045432L;

	private JLabel currentTemperature;
	private JLabel currentRain;
    private JLabel currentWind;
	
	public WindowWeather() {

		JLabel labelOld = new JLabel("Current",SwingConstants.CENTER);

		JLabel labelNew = new JLabel("New");

		JLabel labelTemperature= new JLabel("Temperature: ",SwingConstants.RIGHT);
		JLabel labelRain = new JLabel("Rain: ", SwingConstants.RIGHT);
	    JLabel labelWind = new JLabel("Wind: ", SwingConstants.RIGHT);
	    
	    currentTemperature= new JLabel("-",SwingConstants.CENTER);
		currentRain = new JLabel("-", SwingConstants.CENTER);
	    currentWind = new JLabel("-", SwingConstants.CENTER);
	    
	    // Give the default values
	    	currentTemperature.setText(Integer.toString(Main.getX_Weather().getTemperature()));
	    	currentRain.setText(Integer.toString(Main.getX_Weather().getRain()));
	    	currentWind.setText(Integer.toString(Main.getX_Weather().getWind()));
		    
	    
	    JButton updateWeather = new JButton("Update");
	    updateWeather.setFocusable(false);
	    
	    JTextField temperature = new JTextField();
	    JTextField rain = new JTextField();
	    JTextField wind = new JTextField();
	   
	    temperature.setHorizontalAlignment(SwingConstants.CENTER);
	    rain.setHorizontalAlignment(SwingConstants.CENTER);
	    wind.setHorizontalAlignment(SwingConstants.CENTER);
		
		setLayout(null);
		
	
		add(labelOld);
		add(labelNew);
		add(labelTemperature);
		add(labelRain);
	    add(labelWind);
	    add(updateWeather);
	    
	    add(temperature);
	    add(rain);
	    add(wind);
	    
	    add(currentTemperature);
	    add(currentRain);
	    add(currentWind);
	    
	    

	    labelNew.setBounds(150, 10, 60, 40);
	    labelOld.setBounds(225, 10, 60, 40);
	    labelTemperature.setBounds(30, 40, 80, 40);
	    labelRain.setBounds(30, 70, 80, 40);
	    labelWind.setBounds(30, 100, 80, 40);
	    
	    temperature.setBounds(130, 48, 60, 25);
	    rain.setBounds(130, 78, 60, 25);
	    wind.setBounds(130, 108, 60, 25);
	    
	    currentTemperature.setBounds(225, 48, 60, 25);
	    currentRain.setBounds(225, 78, 60, 25);
	    currentWind.setBounds(225, 108, 60, 25);
	    

	    updateWeather.setBounds(250, 150, 75, 25);
	
	    
	    updateWeather.addActionListener(new ActionListener() {
			 
			public void actionPerformed(ActionEvent e)
		    {
				//Execute when button is pressed
				boolean valid = true;
				
				valid = isNumeric(temperature.getText()) && 
						isNumeric(rain.getText()) && 
						isNumeric(wind.getText());
				
				if(valid) {
					int intTemperature = Integer.parseInt(temperature.getText());
					int intRain = Integer.parseInt(rain.getText());
					int intWind = Integer.parseInt(wind.getText());
					
					valid = intTemperature >= -50 && intTemperature <= 50 &&
							intRain >= 0 && intRain <= 10 &&
							intWind >= 0 && intWind <= 10;
				
				
					if(valid) {
				    	Main.getX_Weather().setWeather(intTemperature, intWind, intRain);
				    }
				}
				
				if(!valid) {
					System.out.println("Invalid values!");
				}

			    refresh();
		    }
		});  
	    
	}
	
	public static boolean isNumeric(String str)
	{
		int i = 0;
		int length = str.length();
	    for (char c : str.toCharArray())
	    {
	    	
	    	if(Character.isDigit(c) || i == 0 && length > 1 && c == '-') {
	    		// SKIP
	    	} else {
	    		return false;
	    	}
	        i++;
	    }
	    if(length == 0) return false;
	    return true;
	}
	
	public void refresh() {
		
		
		currentTemperature.setText(Integer.toString(Main.getX_Weather().getTemperature()));
    	currentRain.setText(Integer.toString(Main.getX_Weather().getRain()));
    	currentWind.setText(Integer.toString(Main.getX_Weather().getWind()));

	}
}


