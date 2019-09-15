package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * @Author: caoyc
 * @Date: 2019-09-15 10:35
 * @Description:
 */
public class SelectByWhereMethodGenerator extends AbstractJavaMapperMethodGenerator {

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        importedTypes.add(FullyQualifiedJavaType.getNewHashMapInstance());
//        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds"));

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());

        importedTypes.add(listType);

        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);

        method.setName("findByWhere");

        FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewHashMapInstance();
        paramType.addTypeArgument(FullyQualifiedJavaType.getStringInstance());
        paramType.addTypeArgument(FullyQualifiedJavaType.getObjectInstance());

        method.addParameter(new Parameter(paramType, "searchMap"));
        method.addParameter(new Parameter(new FullyQualifiedJavaType("org.apache.ibatis.session.RowBounds"), "rowBounds"));


        addMapperAnnotations(interfaze, method);

        interfaze.addImportedTypes(importedTypes);
        //分页直接使用pageHelper
//        interfaze.addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(returnType);

        method.setName("findByWhere");

        method.addParameter(new Parameter(paramType, "searchMap"));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        interfaze.addMethod(method);
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {}

}
