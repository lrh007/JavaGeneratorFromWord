package entity;

/**
 * @Description: 字段类
 * @Author: liangruihao
 * @Date: 2021/4/30 11:11
 */
public class FieldObj {
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段描述
     */
    private String fieldDesc;
    /**
     * 字段类型，默认String
     */
    private String fieldType;

    public FieldObj() {
    }

    public FieldObj(String fieldName, String fieldDesc) {
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
    }

    public FieldObj(String fieldName, String fieldDesc, String fieldType) {
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
