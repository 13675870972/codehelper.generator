package action.util;

import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.util.StringUtility;

import java.io.File;
import java.util.List;

/**
 * @Author: caoyc
 * @Date: 2019-09-13 12:52
 * @Description:
 */
public class Tools {
    public static final String[] DOMIAN_TAGS = {"com.souche"};

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

            //权限控制，暂时省略
            boolean c = true;
            for (String d : DOMIAN_TAGS) {
                if (clientConfig.getTargetPackage().startsWith(d)) {
                    c = false;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
    }

}
