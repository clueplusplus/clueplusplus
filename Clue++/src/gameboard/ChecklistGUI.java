package gameboard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ChecklistGUI {
	private final JPanel gui = new JPanel();
	private JPanel checklist;
	private final List<String> characters = Arrays.asList("Colonel Mustard", "Miss Scarlet", "Mr. Green",
			"Mrs. Peacock", "Mrs. White", "Professor Plum");
	private final List<String> weapons = Arrays.asList("Ballroom", "Billard Room", "Conservatory", "Dining Room",
			"Hall", "Kitchen", "Library", "Lounge", "Study");
	private final List<String> rooms = Arrays.asList("Candlestick", "Knife", "Lead Pipe", "Revolver", "Rope", "Wrench");

	public ChecklistGUI() {
		initializeGui();
	}

	private void initializeGui() {
		final Class[] columnClass = new Class[] {
				String.class, Boolean.class
		};
		Object[][] data = createModelData();
		String[] columns = new String[] {
			"Card Name", "Status"
		};
		DefaultTableModel model = new DefaultTableModel(data, columns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 1) {
					return true;
				}
				return false;
			}
			
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}
		};
		JTable table = new JTable(model);
		createChecklist();
		setChecklistScope(new Color(204, 119, 34));
		checklist.add(new JScrollPane(table), BorderLayout.WEST);
	}
	
	 /**
     * Sets the scope of the game board with the color.
     * 
     * @param backgroundColor The color to set.
     */
    private void setChecklistScope(Color backgroundColor) {
        JPanel boardScope = new JPanel(new GridBagLayout());
        boardScope.setBackground(backgroundColor);
        boardScope.add(checklist);
        gui.add(boardScope);
    }

	private void createChecklist() {
		checklist = new JPanel(new GridLayout(1, 10)); 
	}

	private Object[][] createModelData() {
		Object[][] data = new Object[21][2];
		int count = 0;
		for (String character : characters) {
			data[count][0] = character;
			data[count][1] = false;
			count++;
		}
		for (String weapon : weapons) {
			data[count][0] = weapon;
			data[count][1] = false;
			count++;
		}
		for (String room : rooms) {
			data[count][0] = room;
			data[count][1] = false;
			count++;
		}
		return data;
	}

	public Component getGui() {
		return gui;
	}
}