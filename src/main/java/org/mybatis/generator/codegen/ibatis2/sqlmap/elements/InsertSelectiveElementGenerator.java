/*
 *  Copyright 2008 The Apache Software Foundation
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
package org.mybatis.generator.codegen.ibatis2.sqlmap.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;
import org.mybatis.generator.config.GeneratedKey;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class InsertSelectiveElementGenerator extends
        AbstractXmlElementGenerator {

    public InsertSelectiveElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getInsertSelectiveStatementId()));

        FullyQualifiedJavaType parameterType = introspectedTable.getRules()
                .calculateAllFieldsClass();

        answer.addAttribute(new Attribute("parameterClass",
                parameterType.getFullyQualifiedName()));

        context.getCommentGenerator().addComment(answer);

        GeneratedKey gk = introspectedTable.getGeneratedKey();

        if (gk != null && gk.isPlacedBeforeInsertInIbatis2()) {
            IntrospectedColumn introspectedColumn = introspectedTable
                    .getColumn(gk.getColumn());
            // if the column is null, then it's a configuration error. The
            // warning has already been reported
            if (introspectedColumn != null) {
                // pre-generated key
                answer.addElement(getSelectKey(introspectedColumn, gk));
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append("insert into ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement insertElement = new XmlElement("dynamic");
        insertElement.addAttribute(new Attribute("prepend", "("));
        answer.addElement(insertElement);

        answer.addElement(new TextElement("values"));

        XmlElement valuesElement = new XmlElement("dynamic");
        valuesElement.addAttribute(new Attribute("prepend", "("));
        answer.addElement(valuesElement);

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getAllColumns()) {
            if (introspectedColumn.isIdentity()) {
                // cannot set values on identity fields
                continue;
            }

            XmlElement insertNotNullElement = new XmlElement("isNotNull");
            insertNotNullElement.addAttribute(new Attribute("prepend", ","));
            insertNotNullElement.addAttribute(new Attribute(
                    "property", introspectedColumn.getJavaProperty()));
            insertNotNullElement.addElement(new TextElement(
                    Ibatis2FormattingUtilities
                            .getEscapedColumnName(introspectedColumn)));
            insertElement.addElement(insertNotNullElement);

            XmlElement valuesNotNullElement = new XmlElement("isNotNull");
            valuesNotNullElement.addAttribute(new Attribute("prepend", ","));
            valuesNotNullElement.addAttribute(new Attribute(
                    "property", introspectedColumn.getJavaProperty()));
            valuesNotNullElement.addElement(new TextElement(
                    Ibatis2FormattingUtilities
                            .getParameterClause(introspectedColumn)));
            valuesElement.addElement(valuesNotNullElement);
        }

        insertElement.addElement(new TextElement(")"));
        valuesElement.addElement(new TextElement(")"));

        if (gk != null && !gk.isPlacedBeforeInsertInIbatis2()) {
            IntrospectedColumn introspectedColumn = introspectedTable
                    .getColumn(gk.getColumn());
            // if the column is null, then it's a configuration error. The
            // warning has already been reported
            if (introspectedColumn != null) {
                // pre-generated key
                answer.addElement(getSelectKey(introspectedColumn, gk));
            }
        }

        if (context.getPlugins().sqlMapInsertSelectiveElementGenerated(
                answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
