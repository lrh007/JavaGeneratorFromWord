package entiry;

import java.util.List;

/**
 * @Description: 类对象
 * @Author: liangruihao
 * @Date: 2021/4/30 11:10
 */
public class ClassObj {

    /**
     * 类名称
     */
    private String className;
    /**
     * 类描述
     */
    private String classDesc;

    /**
     * 字段列表
     */
    private List<FieldObj> fields;

    public ClassObj() {
    }

    public ClassObj(String className, String classDesc) {
        this.className = className;
        this.classDesc = classDesc;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public List<FieldObj> getFields() {
        return fields;
    }

    public void setFields(List<FieldObj> fields) {
        this.fields = fields;
    }
}
