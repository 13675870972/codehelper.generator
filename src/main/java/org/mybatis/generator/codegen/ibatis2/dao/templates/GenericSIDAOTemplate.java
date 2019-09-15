/*
 *  Copyright 2006 The Apache Software Foundation
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
package org.mybatis.generator.codegen.ibatis2.dao.templates;

import org.mybatis.generator.api.dom.java.*;

/**
 * @author Jeff Butler
 */
public class GenericSIDAOTemplate extends AbstractDAOTemplate {

    private FullyQualifiedJavaType sqlMapClientType = new FullyQualifiedJavaType(
            "com.ibatis.sqlmap.client.SqlMapClient");

    /**
     *  
     */
    public GenericSIDAOTemplate() {
        super();
    }

    @Override
    protected void configureCheckedExceptions() {
        addCheckedException(new FullyQualifiedJavaType("java.sql.SQLException"));
    }

    @Override
    protected void configureConstructorTemplate() {
        Method method = new Method();
        method.setConstructor(true);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addBodyLine("super();");
        setConstructorTemplate(method);
    }

    @Override
    protected void configureDeleteMethodTemplate() {
        setDeleteMethodTemplate("sqlMapClient.delete(\"{0}.{1}\", {2});");
    }

    @Override
    protected void configureFields() {
        Field field = new Field();
        field.setVisibility(JavaVisibility.PRIVATE);
        field.setType(sqlMapClientType);
        field.setName("sqlMapClient");
        addField(field);
    }

    @Override
    protected void configureImplementationImports() {
        addImplementationImport(sqlMapClientType);
    }

    @Override
    protected void configureInsertMethodTemplate() {
        setInsertMethodTemplate("sqlMapClient.insert(\"{0}.{1}\", {2});");
    }

    @Override
    protected void configureMethods() {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("setSqlMapClient");
        method.addParameter(new Parameter(sqlMapClientType, "sqlMapClient"));
        method.addBodyLine("this.sqlMapClient = sqlMapClient;");
        addMethod(method);

        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("getSqlMapClient");
        method.setReturnType(sqlMapClientType);
        method.addBodyLine("return sqlMapClient;");
        addMethod(method);
    }

    @Override
    protected void configureQueryForListMethodTemplate() {
        setQueryForListMethodTemplate("sqlMapClient.queryForList(\"{0}.{1}\", {2});");
    }

    @Override
    protected void configureQueryForObjectMethodTemplate() {
        setQueryForObjectMethodTemplate("sqlMapClient.queryForObject(\"{0}.{1}\", {2});");
    }

    @Override
    protected void configureUpdateMethodTemplate() {
        setUpdateMethodTemplate("sqlMapClient.update(\"{0}.{1}\", {2});");
    }
}
