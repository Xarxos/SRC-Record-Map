import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class RecordTableModel extends AbstractTableModel {
    private ArrayList<Run> runs = new ArrayList<>();
    private int rowCount;
    private int columnCount = 3;

    @Override
    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Run run = runs.get(rowIndex);
        Object value = null;

        switch(columnIndex) {
            case 0:
                value = run.getTimingMethod();
                break;
            case 1:
                value = run.getTimeString();
                break;
            case 2:
                value = run.getRunner().getName();
        }

        return value;
    }

    public Class<?> getColumnClass(int columnIndex) {
        Class<?> columnClass = null;

        switch(columnIndex) {
            case 0:
                columnClass = String.class;
                break;
            case 1:
                columnClass = String[].class;
                break;
            case 2:
                columnClass = String.class;
        }

        return columnClass;
    }

    @Override
    public String getColumnName(int columnIndex) {
        String columnName = null;

        switch(columnIndex) {
            case 0:
                columnName = "Timing Method";
                break;
            case 1:
                columnName = "World Record";
                break;
            case 2:
                columnName = "Runner";
        }

        return columnName;
    }

    public void addRun(Run run) {
        runs.add(run);
    }

    public void clearRuns() {
        runs.clear();
    }

    public ArrayList<Run> getRuns() {
        return runs;
    }
}
