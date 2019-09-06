package dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * @Author: caoyc
 * @Date: 2019-09-01 10:47
 * @Description:
 */
public class GenerateMapperDialog extends DialogWrapper {

    public GenerateMapperDialog(@Nullable Project project, List<String> tables) {
        super(project, true);
        setTitle("数据库表列表 -- Mybatis Mapper生成器 by haoxz11 Email:haoxz11@163.com");
        setOKButtonText("确定");
        setCancelButtonText("取消");

    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return null;
    }
}
