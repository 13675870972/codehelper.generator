package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * @Author: caoyc
 * @Date: 2019-09-15 10:35
 * @Description:
 */
public class CountByWhereMethodGenerator extends AbstractJavaMapperMethodGenerator {
    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FullyQualifiedJavaType.getNewHashMapInstance());

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getIntInstance();

        method.setReturnType(returnType);

        method.setName("countByWhere");

        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewHashMapInstance();
        paramType.addTypeArgument(FullyQualifiedJavaType.getStringInstance());
        paramType.addTypeArgument(FullyQualifiedJavaType.getObjectInstance());

        method.addParameter(new Parameter(paramType, "searchMap"));

        addMapperAnnotations(interfaze, method);

        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {}
}
