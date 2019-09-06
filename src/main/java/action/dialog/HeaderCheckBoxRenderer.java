package action.dialog;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @Author: caoyc
 * @Date: 2019-09-06 11:01
 * @Description:
 */
public class HeaderCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
    private static final long serialVersionUID = -3224639986882887200L;

    public HeaderCheckBoxRenderer() {
        setBorderPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}
