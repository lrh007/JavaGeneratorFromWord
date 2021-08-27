package entity.xml.abstracts;

import entity.xml.enums.XmlJavaScope;

/**
 * @Description: xml field字段对象
 * @Author: liangruihao
 * @Date: 2021/8/27 9:55
 */
abstract public class XmlFieldObj extends XmlCommonFileObj{
    /**
     * 是否生成字段setter方法， true 生成，false 不生成
     */
    private Boolean setter;
    /**
     * 是否生成字段getter方法，true 生成，false 不生成
     */
    private Boolean getter;


    public Boolean getSetter() {
        return setter;
    }

    public void setSetter(Boolean setter) {
        this.setter = setter;
    }

    public Boolean getGetter() {
        return getter;
    }

    public void setGetter(Boolean getter) {
        this.getter = getter;
    }
}
