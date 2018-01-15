package microwave.main;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import microwave.model.ClockTicked;
import microwave.model.View;

public class UIWithSmartClock extends UI {

	private JSlider slider;

	private ModelWithSmartClock model;

	@Override
	public void run() {
		model = new ModelWithSmartClock();
		register(model.start().get(), View.id());

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(createMainPanel(), BorderLayout.CENTER);
		frame.add(createSlider(), BorderLayout.LINE_END);
		frame.pack();

		frame.setVisible(true);
	}

	@Override
	protected JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		JButton openOrCloseButton = new JButton("Open/Close");

		openOrCloseButton.addActionListener(e -> model.openOrCloseDoor());

		buttonPanel.add(openOrCloseButton);
		return buttonPanel;
	}

	private JSlider createSlider() {
		slider = new JSlider(SwingConstants.VERTICAL, 0, 30, 0);
		slider.addChangeListener(e -> {
			if (!slider.getValueIsAdjusting()) {
				model.changeTime(slider.getValue());
			}
		});

		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
		slider.createStandardLabels(10);

		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		return slider;
	}

	// additional signal handler
	
	@SignalHandler
	public void tick(ClockTicked sig) {
		slider.setValueIsAdjusting(true);
		slider.setValue(Math.max(0, slider.getValue() - 1));
	}

	public static void main(String[] args) {
		new UIWithSmartClock().run();
	}

}