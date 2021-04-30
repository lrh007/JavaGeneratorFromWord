package entiry;

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

    public FieldObj() {
    }

    public FieldObj(String fieldName, String fieldDesc) {
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
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
}
