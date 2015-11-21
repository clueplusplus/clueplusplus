package gameboard;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Displays the check list for the user. 
 */
public class ChecklistGUI {
	private final JPanel gui = new JPanel();
	private JPanel checklist;
	private final List<String> characters = Arrays.asList("Colonel Mustard", "Miss Scarlet", "Mr. Green",
			"Mrs. Peacock", "Mrs. White", "Professor Plum");
	private final List<String> rooms = Arrays.asList("Ballroom", "Billard Room", "Conservatory", "Dining Room",
			"Hall", "Kitchen", "Library", "Lounge", "Study");
	private final List<String> weapons = Arrays.asList("Candlestick", "Knife", "Lead Pipe", "Revolver", "Rope", "Wrench");

	/**
	 * Creates an instance of the check list.
	 */
	public ChecklistGUI() {
		initializeGui();
	}

	/**
	 * Initializes the Check list.
	 */
	private void initializeGui() {
		// Describes the columns of the table.
		final Class<?>[] columnClass = new Class[] {
				String.class, Boolean.class
		};
		// Gets character data.
		JTable charTable = prepareCharacters(columnClass);
		// Gets weapon data.
		JTable weapTable = prepareWeapons(columnClass);
		// Gets room data.
		JTable roomTable = prepareRooms(columnClass);
		// Create the checklist.
		JPanel checklistPanel = createChecklist(charTable, weapTable, roomTable);
		// Add the panel to the GUI.
		checklist.add(new JScrollPane(checklistPanel));
	}

	/**
	 * Prepares the character checklist.
	 * 
	 * @param columnClass The column classes.
	 * @return The table of characters.
	 */
	private JTable prepareCharacters(final Class<?>[] columnClass) {
		Object[][] charData = createModelData(characters);
		String[] charColumns = new String[] {
			"Characters", ""
		};
		// Creates the table for characters.
		DefaultTableModel charModel = getDefaultModel(columnClass, charData, charColumns);
		JTable charTable = new JTable(charModel);
		charTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		charTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		return charTable;
	}

	/**
	 * Prepares the weapon checklist.
	 * 
	 * @param columnClass The column classes.
	 * @return The table of weapons.
	 */
	private JTable prepareWeapons(final Class<?>[] columnClass) {
		Object[][] weapData = createModelData(weapons);
		String[] weapColumns = new String[] {
			"Weapons", ""
		};
		// Creates the table for weapons.
		DefaultTableModel weapModel = getDefaultModel(columnClass, weapData, weapColumns);
		JTable weapTable = new JTable(weapModel);
		weapTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		weapTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		return weapTable;
	}

	/**
	 * Prepares the room checklist.
	 * 
	 * @param columnClass The column classes.
	 * @return The table of rooms.
	 */
	private JTable prepareRooms(final Class<?>[] columnClass) {
		Object[][] roomData = createModelData(rooms);
		String[] roomColumns = new String[] {
			"Rooms", ""
		};
		// Creates the table for rooms.
		DefaultTableModel roomModel = getDefaultModel(columnClass, roomData, roomColumns);
		JTable roomTable = new JTable(roomModel);
		roomTable.getColumnModel().getColumn(0).setPreferredWidth(120);
		roomTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		return roomTable;
	}

	/**
	 * Creates the DefaultTableModel.
	 * 
	 * @param columnClass The column classes.
	 * @param data The data to fill the table with.
	 * @param columns The column headers.
	 * @return The populate table model.
	 */
	private DefaultTableModel getDefaultModel(final Class<?>[] columnClass, Object[][] data, String[] columns) {
		return new DefaultTableModel(data, columns) {
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
	}
	
	 /**
     * Sets the scope of the check list.
     */
    private void setChecklistScope() {
        JPanel boardScope = new JPanel(new GridBagLayout());
        boardScope.add(checklist);
        gui.add(boardScope);
    }

    /**
     * Creates the physic checklist.
     * 
     * @param charTable The character table.
     * @param weapTable The weapon table.
     * @param roomTable The room table.
     * @return The panel with all tables combined.
     */
	private JPanel createChecklist(JTable charTable, JTable weapTable, JTable roomTable) {
		JPanel panel = new JPanel();
		panel.add(new JLabel("Characters"));
		panel.add(charTable);
		panel.add(new JLabel("Weapons"));
		panel.add(weapTable);
		panel.add(new JLabel("Rooms"));
		panel.add(roomTable);
		checklist = new JPanel();
		setChecklistScope();
		return panel;
	}

	/**
	 * Populates the table model with data.
	 * 
	 * @param itemList The list of items to populate the table model.
	 * @return The two dimension object array (String, checkbox).
	 */
	private Object[][] createModelData(List<String> itemList) {
		Object[][] data = new Object[itemList.size()][2];
		int count = 0;
		for (String item : itemList) {
			data[count][0] = item;
			data[count][1] = false;
			count++;
		}
		return data;
	}

	/**
	 * @return The GUI.
	 */
	public Component getGui() {
		return gui;
	}
}