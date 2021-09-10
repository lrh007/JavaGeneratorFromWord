package entity.xml.enums;

/**
 * @Description: xml自定义注解对象
 * @Author: liangruihao
 * @Date: 2021/9/10 11:13
 */
public class XmlCustomMethodAnnotation {
    /**
     * 方法注解
     */
    private String methodAnnotation;

    public XmlCustomMethodAnnotation() {
    }

    public XmlCustomMethodAnnotation(String methodAnnotation) {
        this.methodAnnotation = methodAnnotation;
    }

    public String getMethodAnnotation() {
        return methodAnnotation;
    }

    public void setMethodAnnotation(String methodAnnotation) {
        this.methodAnnotation = methodAnnotation;
    }
}
