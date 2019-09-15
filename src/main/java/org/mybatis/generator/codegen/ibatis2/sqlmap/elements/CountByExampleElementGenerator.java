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

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class CountByExampleElementGenerator extends AbstractXmlElementGenerator {

    public CountByExampleElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getCountByExampleStatementId()));
        answer.addAttribute(new Attribute(
                "parameterClass", introspectedTable.getExampleType()));
        answer.addAttribute(new Attribute("resultClass", "java.lang.Integer"));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select count(*) from ");
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeElement = new XmlElement("include");
        sb.setLength(0);
        sb.append(introspectedTable.getIbatis2SqlMapNamespace());
        sb.append('.');
        sb.append(introspectedTable.getExampleWhereClauseId());
        includeElement.addAttribute(new Attribute("refid",
                sb.toString()));

        answer.addElement(includeElement);

        if (context.getPlugins().sqlMapCountByExampleElementGenerated(
                answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
