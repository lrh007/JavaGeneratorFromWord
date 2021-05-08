package util;

import java.io.File;
import java.util.Objects;

/**
 * @Description: 文件工具类
 * @Author: liangruihao
 * @Date: 2021/5/7 10:59
 */
public class FileUtil {

    /**
     * 删除路径下所有文件夹
     * @param path :路径
     * @return void
     * @author: liangruihao
     * @date: 2021/5/7 11:00
     */
    public static void deleteDirecs(String path){
        try{
            File file = new File(path);
            if(file.listFiles().length == 0){
                System.out.println("删除文件夹："+file.getAbsolutePath());
                file.delete();
                return;
            }
            for (File f : Objects.requireNonNull(file.listFiles())){
                if(f.isDirectory()){
                    for (File f1 : Objects.requireNonNull(f.listFiles())){
                        if(!f1.isDirectory()){
                            System.out.println("删除文件："+f1.getAbsolutePath());
                            f1.delete();
                        }else{
                            System.out.println("进入文件夹："+f1.getAbsolutePath());
                            deleteDirecs(f1.getAbsolutePath());
                        }
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
