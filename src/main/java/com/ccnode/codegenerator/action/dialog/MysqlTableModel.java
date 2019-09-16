package com.ccnode.codegenerator.action.dialog;

import com.google.common.collect.Lists;

import java.util.List;

public class MysqlTableModel extends MutiTableModel {
    private static final long serialVersionUID = 2901808435846952546L;
    private static final String[] HEAD_TITLE = {"选择", "表名", "状态"};

    public MysqlTableModel() {
        super(HEAD_TITLE);
        setCheckColumn(0);
    }

    public MysqlTableModel(Object[][] datas) throws Exception {
        this();
        refreshContents(datas);
    }


    static Object[][] getDatas(List<String> tables) {
        List<Object[]> ssList = Lists.newArrayList();
        for (String tbl : tables) {
            Object[] mm = new Object[3];
            String[] arr = tbl.split(" ", 2);
            mm[0] = Boolean.valueOf(false);
            mm[1] = arr[0];
            mm[2] = "";
            if (arr.length == 2) {
                mm[2] = arr[1];
            }
            ssList.add(mm);
        }
        return (Object[][]) ssList.toArray(new Object[0][]);
    }
}