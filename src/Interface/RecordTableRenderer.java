import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class RecordTableRenderer extends JTextArea implements TableCellRenderer {



    public RecordTableRenderer() {
        super();
        setLineWrap(true);
        setWrapStyleWord(true);
        setEditable(false);
    }

    public void setValue(Object value) {
        setText("Lol?");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String str = "";

        for(int i = 0; i < ((String[])value).length; i++) {
            if(((String[])value)[i] != null) {
                str += ((String[])value)[i] + "\n";
            }
        }

        setText(str);
        //table.setRowHeight(this.getPreferredSize().height);
        return this;
    }
}
