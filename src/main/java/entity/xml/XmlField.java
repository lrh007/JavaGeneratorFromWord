package entity.xml;

import entity.xml.abstracts.XmlFieldObj;

/**
 * @Description: xml field属性对象
 * @Author: liangruihao
 * @Date: 2021/8/27 9:25
 */
public class XmlField extends XmlFieldObj {

    /**
     * 是否显示注释，true 显示，false 不显示
     */
    private Boolean showDescription = true;

    public Boolean getShowDescription() {
        return showDescription;
    }

    public void setShowDescription(Boolean showDescription) {
        this.showDescription = showDescription;
    }
}
