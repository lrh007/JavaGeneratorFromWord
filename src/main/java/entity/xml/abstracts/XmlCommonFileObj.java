package entity.xml.abstracts;

import entity.xml.enums.XmlJavaScope;

/**
 * @Description: xml中公共字属性
 * @Author: liangruihao
 * @Date: 2021/8/27 10:18
 */
abstract public class XmlCommonFileObj {
    /**
     * java作用域，public、private、protected、default
     */
    private XmlJavaScope scope = XmlJavaScope.PUBLIC;
    /**
     * java 类型
     */
    private String javaType;
    /**
     * 是否是静态变量， true 是，false 不是
     */
    private Boolean staticVar = false;
    /**
     * 是否是终态变量，true 是，false不是
     */
    private Boolean finalVar = false;

    public XmlJavaScope getScope() {
        return scope;
    }

    public void setScope(XmlJavaScope scope) {
        this.scope = scope;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public Boolean getStaticVar() {
        return staticVar;
    }

    public void setStaticVar(Boolean staticVar) {
        this.staticVar = staticVar;
    }

    public Boolean getFinalVar() {
        return finalVar;
    }

    public void setFinalVar(Boolean finalVar) {
        this.finalVar = finalVar;
    }
}

