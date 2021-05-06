package entity;

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
    /**
     * 是否是请求类，true是，false 不是 （请求类内容比较多）
     */
    private boolean reqClass;

    /**
     * 实现类
     */
    private String responseClass;

    public ClassObj() {
    }

    public ClassObj(String className, String classDesc) {
        this.className = className;
        this.classDesc = classDesc;
    }

    public ClassObj(String className, String classDesc, boolean reqClass) {
        this.className = className;
        this.classDesc = classDesc;
        this.reqClass = reqClass;
    }

    public ClassObj(String className, String classDesc, boolean reqClass, String responseClass) {
        this.className = className;
        this.classDesc = classDesc;
        this.reqClass = reqClass;
        this.responseClass = responseClass;
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

    public boolean isReqClass() {
        return reqClass;
    }

    public void setReqClass(boolean reqClass) {
        this.reqClass = reqClass;
    }

    public void setFields(List<FieldObj> fields) {
        this.fields = fields;
    }

    public String getResponseClass() {
        return responseClass;
    }

    public void setResponseClass(String responseClass) {
        this.responseClass = responseClass;
    }
}
