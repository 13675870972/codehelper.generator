package com.ccnode.codegenerator.action;

import com.ccnode.codegenerator.action.enums.Information;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author: caoyc
 * @Date: 2019-09-01 10:56
 * @Description:配置文件生成
 */
public class GenerateXMLApplication extends AnAction {
    private static final Logger log = LoggerWrapper.getLogger(GenerateXMLApplication.class);

    @Override
    public void actionPerformed(AnActionEvent event) {
        //获取配置
        VirtualFile configurationFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        String path = configurationFile.getPath();
        if (!new File(path).isDirectory()) {
            Messages.showErrorDialog(Information.GEN_ERROR.getCode(), Information.GEN_ERROR.getMessage());
            return;
        }

        String configContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE generatorConfiguration\n" +
                "        PUBLIC \"-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN\"\n" +
                "        \"dtd/mybatis-generator-config_1_0.dtd\">\n" +
                "<generatorConfiguration>\n" +
                "    <context id=\"DB2Tables\"  targetRuntime=\"MyBatis3\">\n" +
                "        <jdbcConnection\n" +
                "                driverClass=\"com.mysql.jdbc.Driver\"\n" +
                "                connectionURL=\"jdbc:mysql://localhost:3306/database\"\n" +
                "                userId=\"root\"\n" +
                "                password=\"123456\">\n" +
                "        </jdbcConnection>\n" +
                "\n" +
                "        <javaModelGenerator targetPackage=\"com.\" targetProject=\"src/main/java\">\n" +
                "            <property name=\"enableSubPackages\" value=\"true\"/>\n" +
                "        </javaModelGenerator>\n" +
                "        <sqlMapGenerator targetPackage=\"mapper\" targetProject=\"src/main/resources\">\n" +
                "            <property name=\"enableSubPackages\" value=\"true\"/>\n" +
                "        </sqlMapGenerator>\n" +
                "        <javaClientGenerator type=\"XMLMAPPER\" targetPackage=\"com.\" targetProject=\"src/main/java\">\n" +
                "            <property name=\"enableSubPackages\" value=\"true\"/>\n" +
                "        </javaClientGenerator>\n" +
                "    </context>\n" +
                "</generatorConfiguration>";

        File targetFile = new File(path + "/mybatis-generate.xml");

        if (targetFile.exists()) {
            Messages.showErrorDialog(Information.EXIST_ERROR.getCode(), Information.EXIST_ERROR.getMessage());
            return;
        }

        try {
            writeFile(targetFile, configContent);
            VirtualFileManager.getInstance().refreshWithoutFileWatcher(false);
            Messages.showErrorDialog(Information.GEN_SUCCESS.getCode(), Information.GEN_SUCCESS.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(Information.GEN_ERROR.getCode(), Information.GEN_ERROR.getMessage());
        }

    }

    /**
     * 写入文件
     */
    public static void writeFile(File targetFile,String lines) {
        try {
            targetFile.createNewFile();
            try (FileWriter writer = new FileWriter(targetFile);
                 BufferedWriter out = new BufferedWriter(writer)
            ) {
                out.write(lines);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
