import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TestTable {



    public static void main(String[] args) {
        MyTableModel model = new MyTableModel();
        MyRenderer renderer = new MyRenderer();

        JTable table = new JTable(new MyTableModel());
        table.setDefaultRenderer(String.class, renderer);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JFrame frame = new JFrame("TestTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}

class MyRenderer extends DefaultTableCellRenderer {
    public MyRenderer() { super(); }

    public void setValue(Object value) {
        setText("Lol?");
    }
}

class MyTableModel extends AbstractTableModel {
    private String[] columnNames = {"First Name",
            "Last Name",
            "Sport",
            "# of Years",
            "Vegetarian"};

    private Object[][] data = {
            {"Kathy\nKathy", "Smith",
                    "Snowboarding", Integer.valueOf(5), Boolean.valueOf(false)},
            {"John", "Doe",
                    "Rowing", Integer.valueOf(3), Boolean.valueOf(true)},
            {"Sue", "Black",
                    "Knitting", Integer.valueOf(2), Boolean.valueOf(false)},
            {"Jane", "White",
                    "Speed reading", Integer.valueOf(29), Boolean.valueOf(true)},
            {"Joe", "Brown",
                    "Pool", Integer.valueOf(10), Boolean.valueOf(false)}
    };

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 2) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
