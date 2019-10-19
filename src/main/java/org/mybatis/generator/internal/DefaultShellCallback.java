/*
 *  Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.internal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONWriter;
import com.ccnode.codegenerator.util.JSONUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiUtilBase;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.ShellException;
import org.slf4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author Jeff Butler
 */
public class DefaultShellCallback implements ShellCallback {
    private static final Logger log = LoggerWrapper.getLogger(DefaultShellCallback.class);
    private boolean overwrite;

    /**
     *
     */
    public DefaultShellCallback(boolean overwrite) {
        super();
        this.overwrite = overwrite;
    }

    @Override
    public File getDirectory(String targetProject, String targetPackage)
            throws ShellException {
        // targetProject is interpreted as a directory that must exist
        //
        // targetPackage is interpreted as a sub directory, but in package
        // format (with dots instead of slashes). The sub directory will be
        // created
        // if it does not already exist

        File project = new File(targetProject);
        if (!project.isDirectory()) {
            throw new ShellException(getString("Warning.9",
                    targetProject));
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, ".");
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException(getString("Warning.10",
                        directory.getAbsolutePath()));
            }
        }

        return directory;
    }

    @Override
    public void refreshProject(String project) {
        // nothing to do in the default shell callback
    }

    @Override
    public boolean isMergeSupported() {
        return true;
    }

    @Override
    public boolean isOverwriteEnabled() {
        return overwrite;
    }

    /**
     * 组合新文件+老文件=最终文件
     *
     * @param event
     * @param newFile
     * @param existingFileFullPath the fully qualified path name of the existing Java file
     * @param javadocTags          the JavaDoc tags that denotes which methods and fields in the
     *                             old file to delete (if the Java element has any of these tags,
     *                             the element is eligible for merge)
     * @param fileEncoding         the file encoding for reading existing Java files.  Can be null,
     *                             in which case the platform default encoding will be used.
     * @return
     * @throws ShellException
     */
    @Override
    public String mergeJavaFile(AnActionEvent event, GeneratedJavaFile newFile, String existingFileFullPath, String[] javadocTags, String fileEncoding) throws ShellException {
        Project project = event.getData(PlatformDataKeys.PROJECT);

        String source = null;

        VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl("file://" + existingFileFullPath);

        if (vf.exists()) {

            //老文件
            PsiJavaFile file = (PsiJavaFile) PsiUtilBase.getPsiFile(project, vf);
            PsiClass psiClass = file.getClasses()[0];

            GeneratedJavaFile mergerFile = null;

            //定义最终文件骨架
            CompilationUnit compilationUnit = null;
            if (newFile.getCompilationUnit() instanceof TopLevelClass) {
                compilationUnit = new TopLevelClass(newFile.getCompilationUnit().getType());
            } else if (newFile.getCompilationUnit() instanceof Interface) {
                compilationUnit = new Interface(newFile.getCompilationUnit().getType());
            }
            CompilationUnit finalCompilationUnit = compilationUnit;

            //获取新文件中的导入行
            newFile.getImportedTypes().stream().forEach(s -> finalCompilationUnit.addImportedType(new FullyQualifiedJavaType(s.getFullyQualifiedName())));

            //获取老文件中比新文件多的导入行
            Arrays.stream(file.getImportList().getImportStatements())
                    .filter(is ->
                            !newFile.getImportedTypes().contains(new FullyQualifiedJavaType(is.getQualifiedName())))
                    .forEach(is ->
                            finalCompilationUnit.addImportedType(new FullyQualifiedJavaType(is.getQualifiedName())));

            //获取新文件中的注释
            newFile.getCompilationUnit().getFileCommentLines().forEach(s -> finalCompilationUnit.addFileCommentLine(s));

            if (finalCompilationUnit instanceof TopLevelClass) {

                //如果是实体类
                TopLevelClass topLevelClass = (TopLevelClass) finalCompilationUnit;
                TopLevelClass newFileClass = (TopLevelClass) newFile.getCompilationUnit();
                topLevelClass.setVisibility(JavaVisibility.PUBLIC);
                topLevelClass.setSuperClass(newFileClass.getSuperClass());
                newFileClass.getAnnotations().stream().forEach(s -> topLevelClass.addAnnotation(s));
                newFileClass.getFields().stream().forEach(s -> topLevelClass.addField(s));
                PsiField[] fields = psiClass.getFields();
                Arrays.stream(fields).filter(is -> {
                    PsiDocComment docComment = is.getDocComment();
                    String text = docComment.getText();
                    PsiDocTag[] tags = docComment.getTags();
                    Arrays.asList(tags).forEach(e -> {
                    });

                    if (is.getDocComment() != null
                            && is.getDocComment().getTags() != null
                            && is.getDocComment().getTags().length > 0
                            && isMyTag(is.getDocComment().getTags())
                    ) {
                        return false;
                    }

                    return true;
                }).forEach(is -> {
                    Field field = new Field();
                    field.setVisibility(JavaVisibility.PRIVATE);
                    field.setType(new FullyQualifiedJavaType(is.getType().getCanonicalText()));
                    if (is.getDocComment() != null) {
                        field.addJavaDocLine(is.getDocComment().getText());
                    }

                    field.setName(is.getName());
                    topLevelClass.addField(field);
                });
                newFileClass.getMethods().stream().forEach(s -> topLevelClass.addMethod(s));
            } else if (finalCompilationUnit instanceof Interface) {

                //如果是接口，则整合新文件中的所有方法
                Interface finalInterface = (Interface) finalCompilationUnit;
                Interface newFileClass = (Interface) newFile.getCompilationUnit();
                newFileClass.getMethods().stream().forEach(s -> finalInterface.addMethod(s));
                finalInterface.setVisibility(JavaVisibility.PUBLIC);
            }
            if (newFile.getFileName().endsWith("Dao.java") || newFile.getFileName().endsWith("Service.java")) {
                //如果是接口，则整合老文件中多出的方法
                try {
                    Arrays.stream(psiClass.getMethods()).filter(pm -> {
                        if (pm.getDocComment() != null
                                && pm.getDocComment().getTags() != null
                                && pm.getDocComment().getTags().length > 0
                                && isMyTag(pm.getDocComment().getTags())
                        ) {
                            return false;
                        }
                        return true;
                    }).forEach(pm -> {
                        Method method = new Method();
                        method.setVisibility(JavaVisibility.PUBLIC);
                        if (pm.getReturnType() != null) {
                            method.setReturnType(new FullyQualifiedJavaType(pm.getReturnType().getCanonicalText()));
                        } else {
                            method.setReturnType(new FullyQualifiedJavaType("void"));
                        }

                        Arrays.stream(pm.getParameterList().getParameters()).forEach(e -> {
                            String canonicalText = e.getType().getCanonicalText();
                            PsiElement[] children = e.getChildren();

                            Parameter parameter = new Parameter(new FullyQualifiedJavaType(e.getType().getCanonicalText()), e.getName());
                            Arrays.asList(e.getAnnotations()).forEach(annotation -> {
                                parameter.addAnnotation(annotation.getText());

                            });
                            method.addParameter(parameter);
                        });


                        Arrays.stream(pm.getThrowsList().getReferencedTypes()).forEach(e -> {
                            String className = e.getClassName();
                            FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(e.getClassName());
                            method.addException(fullyQualifiedJavaType);
                        });

                        method.setName(pm.getName());
                        if (pm.getDocComment() != null) {
                            method.addJavaDocLine(pm.getDocComment().getText());
                        }
//                        Arrays.stream(pm.getChildren()).forEach(());


                        if (pm.getBody() != null && pm.getBody().getChildren() != null) {
//                            Arrays.stream(pm.getBody().getChildren()).filter(()).forEach(());
                        }


                        if (newFile.getCompilationUnit() instanceof TopLevelClass) {
                            ((TopLevelClass) finalCompilationUnit).addMethod(method);
                        } else if (newFile.getCompilationUnit() instanceof Interface) {
                            ((Interface) finalCompilationUnit).addMethod(method);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mergerFile = new GeneratedJavaFile(finalCompilationUnit, newFile.getTargetProject());

            source = mergerFile.getFormattedContent();
        } else {
            source = newFile.getFormattedContent();
        }

        return source;

    }

    /**
     * 判断注解
     *
     * @param tags
     * @return
     */
    private boolean isMyTag(PsiDocTag[] tags) {
        if (tags == null || tags.length == 0) {
            return false;
        }

        for (int i = 0; i < tags.length; i++) {
            PsiDocTag e = tags[i];
            if (e.getName() != null && MergeConstants.NEW_ELEMENT_TAG.contains(e.getName())) {
                return true;
            }
        }
        return false;
    }
}
