package action;

import action.dialog.Dialog;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiUtilBase;
import org.slf4j.Logger;

import java.util.Arrays;
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
        System.err.println("启动...");
        //获取配置
        Project project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);

        VirtualFile configurationFile = event.getData(PlatformDataKeys.VIRTUAL_FILE);



        //获取类的信息
        String basePath = project.getBasePath();
        System.out.println(String.format("basePath == %s", basePath));

        String classPath = new StringBuilder(basePath).append("/src/main/java/dao/RoleVODao.java").toString();

//        VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl("file://" + classPath);

        PsiJavaFile file = (PsiJavaFile) PsiUtilBase.getPsiFileInEditor(editor,project);

        PsiClass[] classes = file.getClasses();

        Arrays.asList(classes).forEach(e->{
            String name = e.getName();
//            System.err.println(String.format("filename == %s",name));

            PsiField[] allFields = e.getAllFields();

            Arrays.asList(allFields).forEach(f -> {
                System.err.println(String.format("FieldName == %s", f.getName()));
            });

            PsiMethod[] allMethods = e.getMethods();

            Arrays.asList(allMethods).forEach(m -> {
                System.err.println(String.format("MethodName == %s", m.getName()));
                PsiDocComment docComment = m.getDocComment();

                PsiDocTag[] tags = docComment.getTags();
                Arrays.asList(tags).forEach(t -> {
                    System.err.println(String.format("tags == %s", t.getName()));
                });

                PsiParameterList parameterList = m.getParameterList();
                System.err.println(parameterList.toString());

                String canonicalText = m.getReturnType().getCanonicalText();

                System.err.println(String.format("canonicalText == %s",canonicalText));

            });

        });


        try {

            System.err.println("画面弹出...");

            List<String> tables = Arrays.asList("aaa", "bbb");

            Dialog dialog = new Dialog(project, tables);

            if (dialog.showAndGet()) {
                String[] strings = dialog.getTables().toArray(new String[0]);
                System.err.println(String.format("checked tables == %s", strings));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


//        List<String> warnings = new ArrayList<String>();
//        ConfigurationParser cp = new ConfigurationParser(warnings);
//        Configuration config = null;
//        try {
//            config = cp.parseConfiguration(configurationFile.getInputStream());
//            System.err.println(config);
//        } catch (Exception e) {
//            log.error("{}",e);
//            e.printStackTrace();
//            Messages.showErrorDialog("该文件格式不支持.", "错误");
//        }
//
//        List<Context> contexts = config.getContexts();
//        System.err.println(contexts);
//
//
//        //根据配置连接数据库
//
//
//        //生成对话框界面
//
//        System.err.println("开始...");
//        GenerateMapperDialog dialog = new GenerateMapperDialog(project, null);
//        dialog.show();
//        System.err.println("结束...");
    }



}
