package action.dialog;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Vector;

/**
 * @Author: caoyc
 * @Date: 2019-09-06 11:11
 * @Description:
 */
public class MutiTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -1264704523326656387L;
    private static final String ERROR_DATA_LENGTH = "数据不正确。";
    protected int checkColumn = -1;
    protected List<String> columnNames = new Vector();
    protected List<Object> contents = new Vector();


    public MutiTableModel() {
    }


    public MutiTableModel(String[] columnNames) {
        this();

        if (null == columnNames) {
            return;
        }

        for (String columnName : columnNames) {
            this.columnNames.add(columnName);
        }
    }


    public MutiTableModel(Object[][] datas, String[] columnNames) throws Exception {
        this(columnNames);
        refreshContents(datas);
    }


    public void refreshContents(Object[][] datas) throws Exception {
        this.contents.clear();

        if (null == datas) {
            return;
        }

        for (Object[] data : datas) {
            addRow(data);
        }
    }


    public void addRow(Object[] data) throws Exception {
        if (null == data) {
            return;
        }

        if (this.columnNames.size() != data.length) {
            throw new Exception("数据不正确。");
        }

        Vector<Object> content = new Vector<Object>(this.columnNames.size());

        for (int ii = 0; ii < this.columnNames.size(); ii++) {
            content.add(data[ii]);
        }

        this.contents.add(content);
    }


    public void removeRow(int row) {
        this.contents.remove(row);
    }


    public void removeRows(int row, int count) {
        for (int ii = 0; ii < count; ii++) {
            if (this.contents.size() > row) {
                this.contents.remove(row);
            }
        }
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == this.checkColumn) {
            return true;
        }

        return super.isCellEditable(row, col);
    }


    @Override
    public void setValueAt(Object value, int row, int col) {
        ((Vector) this.contents.get(row)).set(col, value);
        fireTableCellUpdated(row, col);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        Object value = getValueAt(0, column);

        if (value != null) {
            return value.getClass();
        }

        return getClass();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.size();
    }


    @Override
    public int getRowCount() {
        return this.contents.size();
    }


    @Override
    public Object getValueAt(int row, int col) {
        return ((Vector) this.contents.get(row)).get(col);
    }


    @Override
    public String getColumnName(int col) {
        return (String) this.columnNames.get(col);
    }


    public int getCheckColumn() {
        return this.checkColumn;
    }


    public void setCheckColumn(int checkColumn) {
        this.checkColumn = checkColumn;
    }
}
