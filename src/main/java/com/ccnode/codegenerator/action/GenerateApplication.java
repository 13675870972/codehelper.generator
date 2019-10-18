package com.ccnode.codegenerator.action;

import com.ccnode.codegenerator.action.dialog.Dialog;
import com.ccnode.codegenerator.action.enums.Information;
import com.ccnode.codegenerator.action.util.Tools;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.junit.Assert;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.config.*;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.db.ConnectionFactory;
import org.mybatis.generator.internal.util.ClassloaderUtility;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: caoyc
 * @Date: 2019-09-01 10:56
 * @Description:
 */
public class GenerateApplication extends AnAction {
    private static final Logger log = LoggerWrapper.getLogger(GenerateApplication.class);

    /**
     * 截止时间
     */
    /**
     * 项目使用权
     */

    @Override
    public void actionPerformed(AnActionEvent event) {
        //刷新文件
        Tools.refresh();
        //获取配置
        Project project = event.getData(PlatformDataKeys.PROJECT);
        VirtualFile configurationFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        String path = configurationFile.getPath().substring(0, configurationFile.getPath().lastIndexOf("/src"));
        String basePath = project.getBasePath();

        //解析配置
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(configurationFile.getInputStream());

            //设置连接
            List<Context> contexts = config.getContexts();
            Context context = contexts.get(0);
            JDBCConnectionConfiguration jdbcConnectionConfiguration = context.getJdbcConnectionConfiguration();

            //检测
            Assert.assertTrue(Tools.toExamine(context));

            ClassLoader classLoader = ClassloaderUtility.getCustomClassloader(config.getClassPathEntries());
            ObjectFactory.setExternalClassLoader(classLoader);

            //获取全部表的表名列表并展示对话框
            List<String> tables = new ArrayList<>();
            Connection connection = ConnectionFactory.getInstance().getConnection(jdbcConnectionConfiguration);
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(rs.getString(3));
            }

            //设置数据库所有待生成表格的配置
            tables.forEach(e -> {
                Tools.setTables(context, e);
            });

            //纠正targetProject
            Tools.formatTargetPath(config, path);

            Dialog dialog = new Dialog(project, tables);

            if (dialog.showAndGet()) {
                //根据选择的表格生成文件
                ShellRunner.run(event, config, new HashSet<>(dialog.getTables()));
                VirtualFileManager.getInstance().refreshWithoutFileWatcher(false);
                Messages.showErrorDialog(Information.SUCCESS.getCode(), Information.SUCCESS.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog(Information.CONFIG_ERROR.getCode(), Information.CONFIG_ERROR.getMessage());
        }

    }


}
