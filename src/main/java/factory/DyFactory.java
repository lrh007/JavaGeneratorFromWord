package factory;

/**
 * @Description: 文件转换工厂类
 * @Author: liangruihao
 * @Date: 2021/5/6 10:12
 */
public class DyFactory {

    //工厂模式不需要外部对象实例化，所以构造方法应该是私有化的
    private DyFactory(){}
    //构造方法私有化后，该类中的其他方法应该是static才能被外部调用
    public static <T> T getInstance(Class<T> clazz){
        T instance = null;
        try {
            //通过反射，将静态工厂类转变成动态工厂类
            instance = (T) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

}
