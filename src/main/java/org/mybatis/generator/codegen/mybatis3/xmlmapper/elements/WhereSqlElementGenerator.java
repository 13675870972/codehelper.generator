package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * @Author: caoyc
 * @Date: 2019-09-15 10:41
 * @Description:
 */
public class WhereSqlElementGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "whereSQL"));

        this.context.getCommentGenerator().addComment(answer);
        XmlElement whereElement = new XmlElement("where");

        for (IntrospectedColumn introspectedColumn : this.introspectedTable.getAllColumns()) {
            String column = introspectedColumn.getActualColumnName().toLowerCase();
            String columnType = "";
            if (column.equals("time_create") || column.equals("time_modified")) {
                columnType = "time";
            } else if (column.equals("create_time") || column.equals("modify_time")) {
                columnType = "time";
            }


            if (!"time".equals(columnType)) {
                XmlElement ifElement = new XmlElement("if");
                StringBuilder sb = new StringBuilder();
                sb.append(introspectedColumn.getJavaProperty());
                sb.append(" != null");
                ifElement.addAttribute(new Attribute("test", sb.toString()));
                sb = new StringBuilder();
                sb.append("<![CDATA[ AND ");
                sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
                sb.append(" = ");
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
                sb.append(" ]]> ");
                ifElement.addElement(new TextElement(sb.toString()));
                whereElement.addElement(ifElement);
            }
        }

        answer.addElement(whereElement);

        if (this.context.getPlugins().sqlMapResultMapWithoutBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}
