package airport.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WindowPlanes extends JPanel {
	private static final long serialVersionUID = -8450091623583045432L;

	private ArrayList<Integer> planeIds;
	private ArrayList<JLabel> labelPlaneIds;
	private ArrayList<JLabel> labelPlaneStatus;
	private ArrayList<JLabel> labelPlaneXCoor;
	private ArrayList<JLabel> labelPlaneYCoor;
	private ArrayList<JLabel> labelTowerStatus;

	public WindowPlanes() {
		JLabel newPlaneLabel = new JLabel("New plane:");
		JLabel planesLabel = new JLabel("Planes:");
		JLabel planeAngleLabel = new JLabel("Angle:");
		JLabel newX = new JLabel("X: ");
		JLabel newY = new JLabel("Y: ");
		JTextField newXField = new JTextField("0");
		JTextField newYField = new JTextField("0");
		JTextField newAngleField = new JTextField("0");
		JButton addPlane = new JButton("Add");

		setLayout(null);

		add(newPlaneLabel);
		add(newX);
		add(newY);
		add(newXField);
		add(newYField);
		add(addPlane);
		add(planesLabel);
		add(planeAngleLabel);
		add(newAngleField);

		newPlaneLabel.setBounds(30, 10, 150, 25);
		newX.setBounds(74, 40, 20, 25);
		newY.setBounds(74, 70, 20, 25);
		newXField.setBounds(92, 42, 60, 25);
		newYField.setBounds(92, 72, 60, 25);
		planeAngleLabel.setBounds(50, 102, 75, 25);
		newAngleField.setBounds(92, 102, 60, 25);

		addPlane.setBounds(50, 134, 75, 25);

		planesLabel.setBounds(30, 179, 75, 25);

		addPlane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.addNewPlane(Integer.parseInt(newXField.getText()), Integer.parseInt(newYField.getText()),
						Integer.parseInt(newAngleField.getText()));
				refresh();
			}
		});

		JLabel LId = new JLabel("ID");
		JLabel LStatus = new JLabel("Status");
		JLabel LX = new JLabel("X");
		JLabel LY = new JLabel("Y");
		JLabel LTower = new JLabel("Tower");

		add(LId);
		add(LStatus);
		add(LX);
		add(LY);
		add(LTower);

		LId.setBounds(80, 209, 75, 25);
		LStatus.setBounds(195, 209, 75, 25);
		LX.setBounds(320, 209, 75, 25);
		LY.setBounds(380, 209, 75, 25);
		LTower.setBounds(440, 209, 75, 25);

		labelPlaneIds = new ArrayList<JLabel>();
		labelPlaneStatus = new ArrayList<JLabel>();
		labelPlaneXCoor = new ArrayList<JLabel>();
		labelPlaneYCoor = new ArrayList<JLabel>();
		labelTowerStatus = new ArrayList<JLabel>();

		planeIds = new ArrayList<Integer>();
	}

	public void refresh() {
		// Clear plane clones
		for (int i = 0; i < labelPlaneIds.size(); i++) {
			remove(labelPlaneIds.get(i));
			remove(labelPlaneStatus.get(i));
			remove(labelPlaneXCoor.get(i));
			remove(labelPlaneYCoor.get(i));
			remove(labelTowerStatus.get(i));
		}

		labelPlaneIds.clear();
		labelPlaneStatus.clear();
		labelPlaneXCoor.clear();
		labelPlaneYCoor.clear();
		labelTowerStatus.clear();

		// Get plane ids
		planeIds.clear();
		int planeIdsSize = Main.getPlaneIdsSize();
		for (int i = 0; i < planeIdsSize; i++) {
			int curId = Main.getPlaneId(i);
			planeIds.add(curId);
			labelPlaneIds.add(new JLabel(Integer.toString(curId)));
			labelPlaneStatus.add(new JLabel(status(Main.getPlane(planeIds.get(i)).getStatus())));
			labelPlaneXCoor.add(new JLabel(Integer.toString((int) (Main.getPlane(planeIds.get(i)).getX()))));
			labelPlaneYCoor.add(new JLabel(Integer.toString((int) (Main.getPlane(planeIds.get(i)).getY()))));
			labelTowerStatus.add(new JLabel(Integer.toString(Main.getPlane(planeIds.get(i)).getTowerId())));

		}

		// Paint them
		for (int i = 0; i < planeIds.size(); i++) {
			labelPlaneIds.get(i).setForeground(Color.BLACK);

			add(labelPlaneIds.get(i));
			;
			labelPlaneIds.get(i).setBounds(80, 232 + i * 30, 75, 25);

			add(labelPlaneStatus.get(i));
			labelPlaneStatus.get(i).setBounds(195, 232 + i * 30, 75, 25);

			add(labelPlaneXCoor.get(i));
			labelPlaneXCoor.get(i).setBounds(320, 232 + i * 30, 75, 25);

			add(labelPlaneYCoor.get(i));
			labelPlaneYCoor.get(i).setBounds(380, 232 + i * 30, 75, 25);

			add(labelTowerStatus.get(i));
			labelTowerStatus.get(i).setBounds(450, 232 + i * 30, 75, 25);

		}

		revalidate();
		repaint();
	}

	public static String status(String str) {
		return str;
	}

}
