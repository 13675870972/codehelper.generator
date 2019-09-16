package com.ccnode.codegenerator.action.dialog;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * @Author: caoyc
 * @Date: 2019-09-06 10:36
 * @Description:
 */
public class Dialog extends DialogWrapper {

    private Project myProject;
    private Map<String, String> fieldTypeMap;
    private MutiTable propTable;
    private JScrollPane jScrollPane;
    private List<String> tables;

    public Dialog(Project project, List<String> tables) throws Exception {
        super(project, true);
        this.myProject = project;

        setTitle("数据库表列表");
        setOKButtonText("确定");
        setCancelButtonText("取消");

        Object[][] propData = MysqlTableModel.getDatas(tables);
        this.propTable = new MutiTable();
        this.propTable.setModel(new MysqlTableModel(propData));
        this.propTable.setCheckHeaderColumn(0);
        this.propTable.getTableHeader().setReorderingAllowed(false);
        this.propTable.getTableHeader().setResizingAllowed(false);
        this.propTable.setRowHeight(25);
        this.jScrollPane = new JScrollPane(this.propTable);

        tables = Lists.newArrayList();
        init();
    }


    @Override
    protected void doOKAction() {
        if (!this.propTable.hasCheckedRow()) {
            Messages.showErrorDialog("请选择要生成的表!", "错误");
            return;
        }
        this.tables = Lists.newArrayList(this.propTable.getAllCheckedColumn(1).toArray(new String[0]));
        super.doOKAction();
    }


    @Override
    @Nullable
    protected JComponent createCenterPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridBagLayout());
        GridBagConstraints bag = new GridBagConstraints();
        int mygridy = 0;
        bag.anchor = 18;
        bag.fill = 2;
        bag.gridy = mygridy++;
        bag.gridx = 0;
        bag.gridwidth = 10;

        jPanel.add(this.jScrollPane, bag);

        return jPanel;
    }


    public List<String> getTables() { return this.tables; }
}


