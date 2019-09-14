package action;

import action.dialog.Dialog;
import action.util.Tools;
import com.ccnode.codegenerator.util.JSONUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiUtilBase;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.db.ConnectionFactory;
import org.mybatis.generator.internal.util.ClassloaderUtility;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @Author: caoyc
 * @Date: 2019-09-01 10:56
 * @Description:
 */
public class GenerateApplication extends AnAction {
    private static final Logger log = LoggerWrapper.getLogger(GenerateApplication.class);

    @Override
    public void actionPerformed(AnActionEvent event) {
        //获取配置
        Project project = event.getData(PlatformDataKeys.PROJECT);
        VirtualFile configurationFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);
        String path = configurationFile.getPath().substring(0, configurationFile.getPath().lastIndexOf("/src"));

        //获取类的信息
        String basePath = project.getBasePath();

        //解析配置
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(configurationFile.getInputStream());
        } catch (Exception e) {
            log.error("{}", e);
            e.printStackTrace();
            Messages.showErrorDialog("配置有误.", "错误");
        }

        //设置连接
        List<Context> contexts = config.getContexts();
        Context context = contexts.get(0);
        JDBCConnectionConfiguration jdbcConnectionConfiguration = context.getJdbcConnectionConfiguration();
        ClassLoader classLoader = ClassloaderUtility.getCustomClassloader(config.getClassPathEntries());
        ObjectFactory.setExternalClassLoader(classLoader);


        //获取全部表的表名列表并展示对话框
        List<String> tables = new ArrayList<>();
        try {
            Connection connection = ConnectionFactory.getInstance().getConnection(jdbcConnectionConfiguration);
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tables.add(rs.getString(3));
            }

            //设置数据库所有待生成表格的配置
            tables.forEach(e->{
                TableConfiguration tc = new TableConfiguration(context);
                context.addTableConfiguration(tc);
                tc.setTableName(e);
            });

            //纠正targetProject
            Tools.formatTargetPath(config, path);

            Dialog dialog = new Dialog(project, tables);

            if (dialog.showAndGet()) {
                //根据选择的表格生成文件
                ShellRunner.run(event, config, new HashSet<>(dialog.getTables()));
                Messages.showErrorDialog("恭喜你生成数据库表成功.", "成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Messages.showErrorDialog("配置有误.", "错误");
        }

    }


}
