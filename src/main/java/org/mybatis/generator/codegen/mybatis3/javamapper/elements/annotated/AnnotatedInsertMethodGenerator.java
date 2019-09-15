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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.InsertMethodGenerator;
import org.mybatis.generator.config.GeneratedKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

/**
 * 
 * @author Jeff Butler
 */
public class AnnotatedInsertMethodGenerator extends
        InsertMethodGenerator {

    public AnnotatedInsertMethodGenerator(boolean isSimple) {
        super(isSimple);
    }

    @Override
    public void addMapperAnnotations(Interface interfaze, Method method) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Insert"));
        
        GeneratedKey gk = introspectedTable.getGeneratedKey();
        
        method.addAnnotation("@Insert({");
        StringBuilder insertClause = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();
        
        javaIndent(insertClause, 1);
        javaIndent(valuesClause, 1);

        insertClause.append("\"insert into ");
        insertClause.append(escapeStringForJava(introspectedTable
                .getFullyQualifiedTableNameAtRuntime()));
        insertClause.append(" (");

        valuesClause.append("\"values (");

        List<String> valuesClauses = new ArrayList<String>();
        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns()
                .iterator();
        boolean hasFields = false;
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.isIdentity()) {
                // cannot set values on identity fields
                continue;
            }

            insertClause.append(escapeStringForJava(getEscapedColumnName(introspectedColumn)));
            valuesClause.append(getParameterClause(introspectedColumn));
            hasFields = true;
            if (iter.hasNext()) {
                insertClause.append(", ");
                valuesClause.append(", ");
            }

            if (valuesClause.length() > 60) {
                if (!iter.hasNext()) {
                    insertClause.append(')');
                    valuesClause.append(')');
                }
                insertClause.append("\",");
                valuesClause.append('\"');
                if (iter.hasNext()) {
                    valuesClause.append(',');
                }
                
                method.addAnnotation(insertClause.toString());
                insertClause.setLength(0);
                javaIndent(insertClause, 1);
                insertClause.append('\"');
                
                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                javaIndent(valuesClause, 1);
                valuesClause.append('\"');
                hasFields = false;
            }
        }
        
        if (hasFields) {
            insertClause.append(")\",");
            method.addAnnotation(insertClause.toString());

            valuesClause.append(")\"");
            valuesClauses.add(valuesClause.toString());
        }

        for (String clause : valuesClauses) {
            method.addAnnotation(clause);
        }
        
        method.addAnnotation("})");

        if (gk != null) {
            addGeneratedKeyAnnotation(interfaze, method, gk);
        }
    }
}
