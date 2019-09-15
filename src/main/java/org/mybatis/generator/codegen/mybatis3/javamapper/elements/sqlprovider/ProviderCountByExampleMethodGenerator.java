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

import org.mybatis.generator.api.dom.java.*;

import java.util.Set;
import java.util.TreeSet;

import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class ProviderCountByExampleMethodGenerator extends
        AbstractJavaProviderMethodGenerator {

    public ProviderCountByExampleMethodGenerator() {
        super();
    }

    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN");
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.FROM");
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SELECT");
        staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL");
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(fqjt);

        Method method = new Method(
                introspectedTable.getCountByExampleStatementId());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.addParameter(new Parameter(fqjt, "example"));
        
        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        method.addBodyLine("BEGIN();");
        method.addBodyLine("SELECT(\"count(*)\");");
        method.addBodyLine(String.format("FROM(\"%s\");",
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine("applyWhere(example, false);");
        method.addBodyLine("return SQL();");
        
        if (context.getPlugins().providerCountByExampleMethodGenerated(method, topLevelClass,
                introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}
