import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class LabelTableRenderer extends JLabel implements TableCellRenderer {
    public LabelTableRenderer() {
        super();
        setVerticalTextPosition(JLabel.NORTH);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(((JLabel)value).getText());
        setIcon(((JLabel)value).getIcon());

        return this;
    }
}
