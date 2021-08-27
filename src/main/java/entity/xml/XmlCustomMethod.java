package entity.xml;

import entity.xml.abstracts.XmlCommonFileObj;

import java.util.List;

/**
 * @Description: xml 自定义方法对象
 * @Author: liangruihao
 * @Date: 2021/8/27 9:47
 */
public class XmlCustomMethod extends XmlCommonFileObj {

    /**
     * 是否在所有类中显示，true 是，false 不是，false的情况下需要配合注解@custom才能起作用
     */
    private Boolean allClass;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 方法注解
     */
    private String methodAnnotation;
    /**
     * 方法返回值
     */
    private String methodReturn;
    /**
     * 方法的参数集合
     */
    private List<XmlCustomMethodParam> methodParams;

    public Boolean getAllClass() {
        return allClass;
    }

    public void setAllClass(Boolean allClass) {
        this.allClass = allClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodAnnotation() {
        return methodAnnotation;
    }

    public void setMethodAnnotation(String methodAnnotation) {
        this.methodAnnotation = methodAnnotation;
    }

    public String getMethodReturn() {
        return methodReturn;
    }

    public void setMethodReturn(String methodReturn) {
        this.methodReturn = methodReturn;
    }

    public List<XmlCustomMethodParam> getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(List<XmlCustomMethodParam> methodParams) {
        this.methodParams = methodParams;
    }
}
