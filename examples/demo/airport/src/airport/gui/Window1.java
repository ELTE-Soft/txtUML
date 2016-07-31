package airport.gui;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Window1 extends JPanel {
	private static final long serialVersionUID = -8450091623583045432L;

	public Window1() {
		JButton button = new JButton("Click");
		//Add action listener to button
		button.addActionListener(new ActionListener() {
		 
			public void actionPerformed(ActionEvent e)
		    {
				//Execute when button is pressed
				System.out.println("You clicked the button");
				button.setText("Uj");
		    }
		});    
		 
		 
		
		
		setLayout(null);
	    add(button);
	    button.setBounds(30, 150, 50, 20);
	    button.setBounds(10, 10, 100, 200);
	}
}


