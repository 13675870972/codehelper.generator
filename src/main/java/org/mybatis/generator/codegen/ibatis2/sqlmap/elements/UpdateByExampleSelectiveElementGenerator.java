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
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.ibatis2.Ibatis2FormattingUtilities;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class UpdateByExampleSelectiveElementGenerator extends
        AbstractXmlElementGenerator {

    public UpdateByExampleSelectiveElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");

        answer
                .addAttribute(new Attribute(
                        "id", introspectedTable.getUpdateByExampleSelectiveStatementId()));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update ");
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("dynamic");
        dynamicElement.addAttribute(new Attribute("prepend", "set"));
        answer.addElement(dynamicElement);

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getAllColumns()) {
            XmlElement isNotNullElement = new XmlElement("isNotNull");
            isNotNullElement.addAttribute(new Attribute("prepend", ","));
            isNotNullElement.addAttribute(new Attribute(
                    "property", introspectedColumn.getJavaProperty("record.")));
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(Ibatis2FormattingUtilities
                    .getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(Ibatis2FormattingUtilities.getParameterClause(
                    introspectedColumn, "record."));

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        XmlElement isParameterPresentElement = new XmlElement(
                "isParameterPresent");
        answer.addElement(isParameterPresentElement);

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid",
                introspectedTable.getIbatis2SqlMapNamespace()
                        + "." + introspectedTable.getExampleWhereClauseId()));
        isParameterPresentElement.addElement(includeElement);

        if (context.getPlugins()
                .sqlMapUpdateByExampleSelectiveElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
