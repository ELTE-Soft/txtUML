package txtuml.examples.garage.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage img;

	ImagePanel(String imgPath) {
		try {
		    img = ImageIO.read(new File(imgPath));
			this.setSize(img.getWidth(), img.getHeight());
		} catch (IOException e) {
			System.out.println("Error: Cannot load image: " + imgPath);
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		if(img == null) {
			return new Dimension(0,0);
		}
		return new Dimension(img.getWidth(), img.getHeight());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(img,0,0,null);
	}
}
