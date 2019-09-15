package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * @Author: caoyc
 * @Date: 2019-09-15 10:44
 * @Description:
 */
public class SelectByWhereElementGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "findByWhere"));
        answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId()));
        answer.addAttribute(new Attribute("parameterType", "map"));

        this.context.getCommentGenerator().addComment(answer);

        answer.addElement(new TextElement("select"));
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "FIELDS != null"));
        ifElement.addElement(new TextElement("${FIELDS}"));
        answer.addElement(ifElement);
        XmlElement ifElement2 = new XmlElement("if");
        ifElement2.addAttribute(new Attribute("test", "FIELDS == null"));
        ifElement2.addElement(new TextElement("*"));
        answer.addElement(ifElement2);

        StringBuilder sb = new StringBuilder();
        sb.append("from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", "whereSQL"));
        answer.addElement(includeElement);

        ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "ORDERLIST != null"));
        ifElement.addElement(new TextElement(" ORDER BY ${ORDERLIST}"));
        answer.addElement(ifElement);
        parentElement.addElement(answer);
    }

}
