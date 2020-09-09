/*
 *  Copyright 2009 The Apache Software Foundation
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
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.GeneratedKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class InsertBatchElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");

        answer.addAttribute(new Attribute(
                "id", "insertBatch"));

        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());

        answer.addAttribute(new Attribute("parameterType",
                FullyQualifiedJavaType.getNewListInstance().getFullyQualifiedName()));

        context.getCommentGenerator().addComment(answer);

        StringBuilder insertClause = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();

        insertClause.append("insert into ");
        insertClause.append(introspectedTable
                .getFullyQualifiedTableNameAtRuntime());
        insertClause.append(" (");
        valuesClause.append(" (");

        List<String> valuesClauses = new ArrayList<String>();
        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns()
                .iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.isIdentity()) {
                // cannot set values on identity fields
                continue;
            }

            insertClause.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            valuesClause.append(MyBatis3FormattingUtilities
                    .getForEachParameterClause(introspectedColumn));
            if (iter.hasNext()) {
                insertClause.append(", ");
                valuesClause.append(", ");
            }

            if (valuesClause.length() > 80) {
                answer.addElement(new TextElement(insertClause.toString()));
                insertClause.setLength(0);
                OutputUtilities.xmlIndent(insertClause, 1);

                valuesClauses.add(valuesClause.toString());
                valuesClause.setLength(0);
                OutputUtilities.xmlIndent(valuesClause, 1);
            }
        }

        valuesClause.append(")");
        insertClause.append(") values");
        answer.addElement(new TextElement(insertClause.toString()));

        valuesClauses.add(valuesClause.toString());

        XmlElement foreach = new XmlElement("foreach");
        foreach.addAttribute(new Attribute("collection", "list"));
        foreach.addAttribute(new Attribute("index", "index"));
        foreach.addAttribute(new Attribute("item", "item"));
        foreach.addAttribute(new Attribute("separator", ","));
        for (String clause : valuesClauses) {
            foreach.addElement(new TextElement(clause));
        }
        answer.addElement(foreach);

        if (context.getPlugins().sqlMapInsertElementGenerated(answer,
                introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
