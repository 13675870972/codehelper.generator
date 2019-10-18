package com.ccnode.codegenerator.action.util;

import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: caoyc
 * @Date: 2019-09-13 12:52
 * @Description:
 */
public class Tools {

    /**
     * 因IDE问题需要转换路径
     *
     * @param linkPath
     * @param relativePath
     * @return
     * @throws Exception
     */
    public static String formatRelativePath(String linkPath, String relativePath) throws Exception {
        if (linkPath == null || relativePath == null || "".equals(relativePath.trim())) {
            return linkPath;
        }
        if (linkPath.substring(linkPath.length() - 1, linkPath.length()).equals(File.separator)) {
            linkPath = linkPath.substring(0, linkPath.length() - 1);
        }
        if (!relativePath.startsWith("/")) {
            relativePath = "/" + relativePath;
        }

        relativePath = relativePath.replace("\\", "/");
        int index = relativePath.indexOf("../");
        while (index >= 0) {
            int _index = linkPath.lastIndexOf(File.separator);
            if (_index < 0) {
                throw new Exception("The relativePath is incorrect base on linkPath. ");
            }
            linkPath = linkPath.substring(0, _index);
            relativePath = relativePath.substring(index + 2);
            index = relativePath.indexOf("../");
        }
        String realPath = (linkPath + relativePath).replace("/", File.separator);
        File file = new File(realPath);
        if (!file.exists()) {
            boolean r = file.mkdir();
            if (!r) {
                r = file.mkdirs();
            }
        }
        return realPath;
    }

    public static void parseTable(Context context, String tableName) {
        if (StringUtility.stringHasValue(tableName)) {
            TableConfiguration tc = new TableConfiguration(context);
            context.addTableConfiguration(tc);
            tc.setTableName(tableName);
        }
    }

    /**
     * 纠正：targetProject
     * @param config
     * @param path
     * @throws Exception
     */
    public static void formatTargetPath(Configuration config, String path) throws Exception {

        List<Context> contexts = config.getContexts();

        if (contexts != null && contexts.size() > 0 && contexts.get(0) != null) {
            Context context = contexts.get(0);

            JavaClientGeneratorConfiguration clientConfig = context.getJavaClientGeneratorConfiguration();
            String clientPath = Tools.formatRelativePath(path, clientConfig.getTargetProject());
            clientConfig.setTargetProject(clientPath);

            JavaModelGeneratorConfiguration modelConfig = context.getJavaModelGeneratorConfiguration();
            String modelPath = Tools.formatRelativePath(path, modelConfig.getTargetProject());
            modelConfig.setTargetProject(modelPath);

            SqlMapGeneratorConfiguration sqlConfig = context.getSqlMapGeneratorConfiguration();
            String sqlPath = Tools.formatRelativePath(path, sqlConfig.getTargetProject());
            sqlConfig.setTargetProject(sqlPath);

            boolean flag = true;
            for (String d : ShellRunner.GENERATOR_PACKAGES) {
                if (clientConfig.getTargetPackage().startsWith(d)) {
                    flag = false;
                }
            }

            if (flag) {
                context.clearTableConfiguration();
            }

        }
    }

    /**
     * 设置数据库所有待生成表格的配置
     * @param context
     * @param tableName
     */
    public static void setTables(Context context, String tableName) {
        TableConfiguration tc = new TableConfiguration(context);
        context.addTableConfiguration(tc);
        tc.setTableName(tableName);
    }


    /**
     * 权限检测
     * @param context
     * @return
     */
    public static boolean checkPackage(Context context) {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
        String modelTargetPackage = javaModelGeneratorConfiguration.getTargetPackage();

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
        String clientTargetPackage = javaClientGeneratorConfiguration.getTargetPackage();

        boolean flag = false;

        for (String e : ShellRunner.CONFIG_PACKAGES) {
            if (modelTargetPackage.startsWith(e)) {
                flag = true;
                break;
            }else {
                return flag;
            }
        }

        for (String e : ShellRunner.CONFIG_PACKAGES) {
            if (clientTargetPackage.startsWith(e)) {
                flag = true;
                break;
            }
            flag = false;
        }


        return flag;
    }


    /**
     * 校验
     * @param context
     * @return
     */
    public static boolean toExamine(Context context) {
        return checkPackage(context) && checkValidity();
    }

    /**
     * 有效期检测
     * @return
     */
    public static boolean checkValidity() {
        SimpleDateFormat dateFormat=new SimpleDateFormat(ShellRunner.DEAD_LINE[2]);

        try {
            //网络时间
            URL url=new URL(ShellRunner.DEAD_LINE[1]);
            URLConnection conn=url.openConnection();
            conn.connect();
            Date dateNow=new Date(conn.getDate());

            //截止时间
            Date deadLine = dateFormat.parse(ShellRunner.DEAD_LINE[0]);

            if (deadLine.getTime() > dateNow.getTime()) {
                return true;
            }else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100; i++) {
            System.err.println(generator());
        }
    }

    public static void refresh() {
        FileDocumentManager.getInstance().saveAllDocuments();
        VirtualFileManager.getInstance().refreshWithoutFileWatcher(false);
    }

    public static String generator(){
        //用字符数组的方式随机
        String randomCode = "";
        String model = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        char[] m = model.toCharArray();

        for (int j=0;j<6 ;j++ )
        {
            char c = m[(int)(Math.random()*52)];
            randomCode = randomCode + c;
        }
        return randomCode;
    }

}