package vip.yeee.memo.integrate.custom.generate.service.model;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.io.Serializable;

/**
 * @author liuzh
 * @since 3.4.5
 */
public class ColumnField implements Serializable {

    private static final long serialVersionUID = -435113788623615260L;
    private TableClass             tableClass;
    private String                 columnName;
    private String                 jdbcType;
    private String                 fieldName;
    private String                 remarks;
    private FullyQualifiedJavaType type;
    private String                 typePackage;
    private String                 shortTypeName;
    private String                 fullTypeName;
    private boolean                identity;
    private boolean                nullable;
    private boolean                blobColumn;
    private boolean                stringColumn;
    private boolean                jdbcCharacterColumn;
    private boolean                jdbcDateColumn;
    private boolean                jdbcTimeColumn;
    private boolean                sequenceColumn;
    private int                    length;
    private int                    scale;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
        //this.fieldName = this.columnName;
    }

    public String getFullTypeName() {
        return fullTypeName;
    }

    public void setFullTypeName(String fullTypeName) {
        this.fullTypeName = fullTypeName;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getShortTypeName() {
        return shortTypeName;
    }

    public void setShortTypeName(String shortTypeName) {
        this.shortTypeName = shortTypeName;
    }

    public TableClass getTableClass() {
        return tableClass;
    }

    public void setTableClass(TableClass tableClass) {
        this.tableClass = tableClass;
    }

    public FullyQualifiedJavaType getType() {
        return type;
    }

    public void setType(FullyQualifiedJavaType type) {
        this.type = type;
    }

    public String getTypePackage() {
        return typePackage;
    }

    public void setTypePackage(String typePackage) {
        this.typePackage = typePackage;
    }

    public boolean isBlobColumn() {
        return blobColumn;
    }

    public void setBlobColumn(boolean blobColumn) {
        this.blobColumn = blobColumn;
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public boolean isJdbcCharacterColumn() {
        return jdbcCharacterColumn;
    }

    public void setJdbcCharacterColumn(boolean jdbcCharacterColumn) {
        this.jdbcCharacterColumn = jdbcCharacterColumn;
    }

    public boolean isJdbcDateColumn() {
        return jdbcDateColumn;
    }

    public void setJdbcDateColumn(boolean jdbcDateColumn) {
        this.jdbcDateColumn = jdbcDateColumn;
    }

    public boolean isJdbcTimeColumn() {
        return jdbcTimeColumn;
    }

    public void setJdbcTimeColumn(boolean jdbcTimeColumn) {
        this.jdbcTimeColumn = jdbcTimeColumn;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isSequenceColumn() {
        return sequenceColumn;
    }

    public void setSequenceColumn(boolean sequenceColumn) {
        this.sequenceColumn = sequenceColumn;
    }

    public boolean isStringColumn() {
        return stringColumn;
    }

    public void setStringColumn(boolean stringColumn) {
        this.stringColumn = stringColumn;
    }
}
