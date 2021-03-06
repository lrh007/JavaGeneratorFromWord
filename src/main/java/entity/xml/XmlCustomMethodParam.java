package entity.xml;

/**
 * @Description: xml 中方法参数对象
 * @Author: liangruihao
 * @Date: 2021/8/27 10:29
 */
public class XmlCustomMethodParam {
    /**
     * 参数名称
     */
    private String methodParam;

    /**
     * java 类型
     */
    private String javaType;

    /**
     * 是否是终态变量，true 是，false不是
     */
    private Boolean finalVar = false;

    public String getMethodParam() {
        return methodParam;
    }

    public void setMethodParam(String methodParam) {
        this.methodParam = methodParam;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public Boolean getFinalVar() {
        return finalVar;
    }

    public void setFinalVar(Boolean finalVar) {
        this.finalVar = finalVar;
    }
}
