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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class SelectByExampleWithBLOBsElementGenerator extends
        AbstractXmlElementGenerator {

    public SelectByExampleWithBLOBsElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer
                .addAttribute(new Attribute(
                        "id", introspectedTable.getSelectByExampleWithBLOBsStatementId()));
        answer.addAttribute(new Attribute(
                "resultMap", introspectedTable.getResultMapWithBLOBsId()));
        answer.addAttribute(new Attribute(
                "parameterClass", introspectedTable.getExampleType()));

        context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select"));
        XmlElement isParameterPresent = new XmlElement("isParameterPresent");
        XmlElement isEqualElement = new XmlElement("isEqual");
        isEqualElement.addAttribute(new Attribute("property", "distinct"));
        isEqualElement.addAttribute(new Attribute("compareValue", "true"));
        isEqualElement.addElement(new TextElement("distinct"));
        isParameterPresent.addElement(isEqualElement);
        answer.addElement(isParameterPresent);

        StringBuilder sb = new StringBuilder();
        if (stringHasValue(introspectedTable
                .getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID,");
            answer.addElement(new TextElement(sb.toString()));
        }

        answer.addElement(getBaseColumnListElement());
        answer.addElement(new TextElement(","));
        answer.addElement(getBlobColumnListElement());

        sb.setLength(0);
        sb.append("from ");
        sb.append(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement isParameterPresenteElement = new XmlElement(
                "isParameterPresent");
        answer.addElement(isParameterPresenteElement);

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid",
                introspectedTable.getIbatis2SqlMapNamespace()
                        + "." + introspectedTable.getExampleWhereClauseId()));
        isParameterPresenteElement.addElement(includeElement);

        XmlElement isNotNullElement = new XmlElement("isNotNull");
        isNotNullElement
                .addAttribute(new Attribute("property", "orderByClause"));
        isNotNullElement
                .addElement(new TextElement("order by $orderByClause$"));
        isParameterPresenteElement.addElement(isNotNullElement);

        if (context.getPlugins()
                .sqlMapSelectByExampleWithBLOBsElementGenerated(answer,
                        introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
