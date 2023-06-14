package vip.yeee.memo.integrate.custom.generate.core.model;

import cn.hutool.core.util.StrUtil;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 3.4.5
 */
public class TableColumnBuilder {

    /**
     * 创建 TableClass
     */
    public static TableClass build(IntrospectedTable introspectedTable) {
        TableClass tableClass = new TableClass();
        tableClass.setIntrospectedTable(introspectedTable);

        FullyQualifiedTable fullyQualifiedTable = introspectedTable.getFullyQualifiedTable();
        tableClass.setTableName(fullyQualifiedTable.getIntrospectedTableName());

        String tablePrefix = introspectedTable.getContext().getProperty("tablePrefix");
        String[] arr = tableClass.getTableName().replaceFirst(StrUtil.isNotBlank(tablePrefix) ? tablePrefix : "", "").split("_");
        tableClass.setSimpleTableName(StrUtil.toCamelCase(Arrays.stream(arr).skip(1).collect(Collectors.joining("_"))));

        tableClass.setPagesPath(arr[0] + "/" + tableClass.getSimpleTableName());

        tableClass.setRemarks(StrUtil.emptyToDefault(introspectedTable.getRemarks(), "     "));

        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        tableClass.setType(type);
        tableClass.setVariableName(Introspector.decapitalize(type.getShortName()));
        tableClass.setLowerCaseName(type.getShortName().toLowerCase());
        tableClass.setShortClassName(type.getShortName());
        tableClass.setFullClassName(type.getFullyQualifiedName());
        tableClass.setPackageName(type.getPackageName());

        List<ColumnField> pkFields = new ArrayList<ColumnField>();
        List<ColumnField> baseFields = new ArrayList<ColumnField>();
        List<ColumnField> blobFields = new ArrayList<ColumnField>();
        List<ColumnField> allFields = new ArrayList<ColumnField>();
        for (IntrospectedColumn column : introspectedTable.getPrimaryKeyColumns()) {
            ColumnField field = build(column);
            field.setTableClass(tableClass);
            pkFields.add(field);
            allFields.add(field);
        }
        for (IntrospectedColumn column : introspectedTable.getBaseColumns()) {
            ColumnField field = build(column);
            field.setTableClass(tableClass);
            baseFields.add(field);
            allFields.add(field);
        }
        for (IntrospectedColumn column : introspectedTable.getBLOBColumns()) {
            ColumnField field = build(column);
            field.setTableClass(tableClass);
            blobFields.add(field);
            allFields.add(field);
        }
        tableClass.setPkFields(pkFields);
        tableClass.setBaseFields(baseFields);
        tableClass.setBlobFields(blobFields);
        tableClass.setAllFields(allFields);

        return tableClass;
    }

    /**
     * 创建 ColumnField
     */
    public static ColumnField build(IntrospectedColumn column) {
        ColumnField field = new ColumnField();
        field.setColumnName(column.getActualColumnName());
        field.setJdbcType(column.getJdbcTypeName());
        field.setFieldName(column.getJavaProperty());
        field.setRemarks(column.getRemarks());
        FullyQualifiedJavaType type = column.getFullyQualifiedJavaType();
        field.setType(type);
        field.setTypePackage(type.getPackageName());
        field.setShortTypeName(type.getShortName());
        field.setFullTypeName(type.getFullyQualifiedName());
        field.setIdentity(column.isIdentity());
        field.setNullable(column.isNullable());
        field.setSequenceColumn(column.isSequenceColumn());
        field.setBlobColumn(column.isBLOBColumn());
        field.setStringColumn(column.isStringColumn());
        field.setJdbcCharacterColumn(column.isJdbcCharacterColumn());
        field.setJdbcDateColumn(column.isJDBCDateColumn());
        field.setJdbcTimeColumn(column.isJDBCTimeColumn());
        field.setLength(column.getLength());
        field.setScale(column.getScale());
        return field;
    }


}
