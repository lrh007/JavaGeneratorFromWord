package util;

import comm.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: String工具类
 * @Author: liangruihao
 * @Date: 2021/5/6 13:50
 */
public class StringUtil {

    /**
     * 获取字符串中括号()里面的内容，多个括号，只获取第一个
     * @param msg :
     * @return java.lang.String
     * @author: liangruihao
     * @date: 2021/5/6 13:58
     */
    public static String getFieldType(String msg) {
        Pattern p = Pattern.compile(Const.SPLIT_1);
        Matcher m = p.matcher(msg);
        while (m.find()) {
            String s = m.group(0).substring(1, m.group().length() - 1);
            if(!"".equals(s.trim())){
                return s;
            }
        }
        return null;
    }
    /**
     *  获取字符串中尖括号<>里面的内容，多个尖括号，只获取第一个
     * @param msg :
     * @return java.lang.String
     * @author: liangruihao
     * @date: 2021/5/6 17:23
     */
    public static String getClassInterface(String msg) {
        Pattern p = Pattern.compile(Const.SPLIT_4);
        Matcher m = p.matcher(msg);
        while (m.find()) {
            String s = m.group(0).substring(1, m.group().length() - 1);
            if(!"".equals(s.trim())){
                return s;
            }
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        String msg = "userInfo<<1221>sss11s>";
        String list = getClassInterface(msg);
        System.out.println(list);

    }
}
