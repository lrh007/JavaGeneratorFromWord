package entity.xml.enums;

/**
 * @Description: xml 中java类型枚举
 * @Author: liangruihao
 * @Date: 2021/8/27 9:32
 */
public enum XmlJavaScope {
    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    DEFAULT("default");

    /**
     * java 类型作用域
     */
    private String scope;

    XmlJavaScope() {
    }

    XmlJavaScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
