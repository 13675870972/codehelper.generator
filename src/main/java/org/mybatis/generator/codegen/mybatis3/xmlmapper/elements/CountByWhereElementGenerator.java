package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * @Author: caoyc
 * @Date: 2019-09-15 10:46
 * @Description:
 */
public class CountByWhereElementGenerator extends AbstractXmlElementGenerator{

    public CountByWhereElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "countByWhere"));
        answer.addAttribute(new Attribute("resultType", "java.lang.Integer"));
        answer.addAttribute(new Attribute("parameterType", "map"));

        this.context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select count(1) from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", "whereSQL"));
        answer.addElement(includeElement);


        parentElement.addElement(answer);
    }
}
