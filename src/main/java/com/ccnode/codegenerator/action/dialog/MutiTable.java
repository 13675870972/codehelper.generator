package com.ccnode.codegenerator.action.dialog;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: caoyc
 * @Date: 2019-09-06 11:00
 * @Description:
 */
public class MutiTable extends JTable implements MouseListener {

    private static final long serialVersionUID = 584842405181279389L;

    private final int DEFAULT_CHECKHEADERCOLUMN = -1;

    private final int DEFAULT_PREFERREDWIDTH = 40;

    private final int DEFAULT_MAXWIDTH = 40;

    private final int DEFAULT_MINWIDTH = 40;

    private int checkHeaderColumn = -1;

    private final HeaderCheckBoxRenderer checkHeader = new HeaderCheckBoxRenderer();

    private TableCellRenderer oldCheckHeader = null;

    private boolean oldEnable = false;

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == getTableHeader()) {
            boolean isSelected = !this.checkHeader.isSelected();
            this.checkHeader.setSelected(isSelected);
            getTableHeader().repaint();
            checkColumnCells(isSelected);
        } else {
            int row = rowAtPoint(e.getPoint());
            boolean isSelected = !((Boolean)getModel().getValueAt(row, this.checkHeaderColumn)).booleanValue();
            getModel().setValueAt(Boolean.valueOf(isSelected), row, this.checkHeaderColumn);
            setRowSelectionInterval(row, row);
            checkColumnHeader();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public int getCheckHeaderColumn() { return this.checkHeaderColumn; }

    public void setCheckHeaderColumn(int checkHeaderColumn) {
        if (isCheckHeader()) {
            TableColumn tableColumn = getColumnModel().getColumn(this.checkHeaderColumn);

            if (null != this.oldCheckHeader) {
                tableColumn.setHeaderRenderer(this.oldCheckHeader);
                setEnabled(this.oldEnable);
            }

            getTableHeader().removeMouseListener(this);
            removeMouseListener(this);
        }

        this.checkHeaderColumn = checkHeaderColumn;

        if (!isCheckHeader()) {
            this.checkHeaderColumn = -1;

            return;
        }
        TableColumn tableColumn = getColumnModel().getColumn(this.checkHeaderColumn);
        tableColumn.setPreferredWidth(40);
        tableColumn.setMaxWidth(40);
        tableColumn.setMinWidth(40);
        this.oldCheckHeader = tableColumn.getHeaderRenderer();
        tableColumn.setHeaderRenderer(this.checkHeader);
        getTableHeader().addMouseListener(this);
        addMouseListener(this);
        this.oldEnable = isEnabled();

        if (this.oldEnable) {
            setEnabled(false);
        }

        checkColumnHeader();
    }

    public void checkColumnCells(boolean isCheck) {
        if (!isCheckHeader()) {
            return;
        }

        for (int ii = 0; ii < getRowCount(); ii++) {
            getModel().setValueAt(Boolean.valueOf(isCheck), ii, this.checkHeaderColumn);
        }
    }

    public void checkColumnHeader() {
        if (hasCheckedRow()) {
            if (this.checkHeader.isSelected()) {
                return;
            }

            this.checkHeader.setSelected(true);
            getTableHeader().repaint();
        } else {
            if (!this.checkHeader.isSelected()) {
                return;
            }

            this.checkHeader.setSelected(false);
            getTableHeader().repaint();
        }
    }

    public boolean isCheckHeader() { return (this.checkHeaderColumn >= 0 && this.checkHeaderColumn < getColumnCount()); }

    public boolean hasCheckedRow() {
        if (!isCheckHeader()) {
            return false;
        }

        for (int ii = 0; ii < getRowCount(); ii++) {
            boolean isCheck = ((Boolean)getModel().getValueAt(ii, this.checkHeaderColumn)).booleanValue();

            if (isCheck) {
                return true;
            }
        }

        return false;
    }

    public List<Integer> getAllCheckedRows() {
        List<Integer> rows = new ArrayList<Integer>();

        if (!isCheckHeader()) {
            return rows;
        }

        for (int ii = 0; ii < getRowCount(); ii++) {
            boolean isCheck = ((Boolean)getModel().getValueAt(ii, this.checkHeaderColumn)).booleanValue();

            if (isCheck) {
                rows.add(Integer.valueOf(ii));
            }
        }

        return rows;
    }

    public List<Object> getAllCheckedColumn(int col) {
        List<Object> rows = new ArrayList<Object>();

        if (!isCheckHeader()) {
            return rows;
        }

        for (int ii = 0; ii < getRowCount(); ii++) {
            boolean isCheck = ((Boolean)getModel().getValueAt(ii, this.checkHeaderColumn)).booleanValue();

            if (isCheck) {
                rows.add(getModel().getValueAt(ii, col));
            }
        }

        return rows;
    }

    public void refreshContents(Object[][] datas) throws Exception {
        MutiTableModel mutiTableModel = (MutiTableModel)getModel();
        mutiTableModel.refreshContents(datas);
        checkColumnHeader();
        updateUI();
    }

    public void addRow(Object[] data) throws Exception {
        MutiTableModel mutiTableModel = (MutiTableModel)getModel();
        mutiTableModel.addRow(data);
        checkColumnHeader();
        updateUI();
    }

    public void removeRow(int row) {
        MutiTableModel mutiTableModel = (MutiTableModel)getModel();
        mutiTableModel.removeRow(row);
        checkColumnHeader();
        updateUI();
    }

    public void removeRows(int row, int count) {
        MutiTableModel mutiTableModel = (MutiTableModel)getModel();
        mutiTableModel.removeRows(row, count);
        checkColumnHeader();
        updateUI();
    }
}
