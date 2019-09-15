package action.util;

import action.auth.Auth;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
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
            for (String d : Auth.DOMIAN_TAGS) {
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
     * 有效期检测
     * @param deadLineStr
     * @return
     */
    public static boolean checkValidity(String deadLineStr) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");

        try {
            //网络时间
            URL url=new URL("http://www.baidu.com");
            URLConnection conn=url.openConnection();
            conn.connect();
            Date dateNow=new Date(conn.getDate());

            //截止时间
            Date deadLine = dateFormat.parse(deadLineStr);

            if (deadLine.getTime() > dateNow.getTime()) {
                return true;
            }else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 权限检测
     * @param context
     * @return
     */
    public static boolean checkPackage(Context context,List<String> packages) {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
        String modelTargetPackage = javaModelGeneratorConfiguration.getTargetPackage();

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
        String clientTargetPackage = javaClientGeneratorConfiguration.getTargetPackage();

        boolean flag = false;

        for (String e : packages) {
            if (modelTargetPackage.startsWith(e)) {
                flag = true;
                break;
            }
            flag = false;
        }

        for (String e : packages) {
            if (clientTargetPackage.startsWith(e)) {
                flag = true;
                break;
            }
            flag = false;
        }


        return flag;
    }


    public static void main(String[] args) throws Exception {
        if (checkValidity("2019-09-14")) {
            System.err.println("还能用");
        }else {
            System.err.println("已过期");
        }


    }

}
