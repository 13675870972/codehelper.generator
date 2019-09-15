/*
 *  Copyright 2010 The MyBatis Team
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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class ProviderInsertSelectiveMethodGenerator extends
        AbstractJavaProviderMethodGenerator {

    public ProviderInsertSelectiveMethodGenerator() {
        super();
    }

    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN");
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO");
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL");
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.VALUES");

        FullyQualifiedJavaType fqjt = introspectedTable.getRules()
            .calculateAllFieldsClass();
        importedTypes.add(fqjt);

        Method method = new Method(
                introspectedTable.getInsertSelectiveStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(fqjt, "record"));
        
        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        method.addBodyLine("BEGIN();");
        method.addBodyLine(String.format("INSERT_INTO(\"%s\");",
                escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime())));

        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            if (introspectedColumn.isIdentity()) {
                // cannot set values on identity fields
                continue;
            }
            
            method.addBodyLine("");
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()
                    && !introspectedColumn.isSequenceColumn()) {
                method.addBodyLine(String.format("if (record.%s() != null) {",
                    getGetterMethodName(introspectedColumn.getJavaProperty(),
                            introspectedColumn.getFullyQualifiedJavaType())));
            }
            method.addBodyLine(String.format("VALUES(\"%s\", \"%s\");",
                    escapeStringForJava(getEscapedColumnName(introspectedColumn)),
                    getParameterClause(introspectedColumn)));
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()
                    && !introspectedColumn.isSequenceColumn()) {
                method.addBodyLine("}");
            }
        }
        
        method.addBodyLine("");
        method.addBodyLine("return SQL();");
        
        if (context.getPlugins().providerInsertSelectiveMethodGenerated(method, topLevelClass,
                introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}
