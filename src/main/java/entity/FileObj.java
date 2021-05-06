package entity;

import java.util.List;

/**
 * @Description: 文件类
 * @Author: liangruihao
 * @Date: 2021/4/30 16:05
 */
public class FileObj {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 每个文件对应的所有类
     */
    private List<ClassObj> classObjs;

    public FileObj() {
    }

    public FileObj(String fileName, List<ClassObj> classObjs) {
        this.fileName = fileName;
        this.classObjs = classObjs;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<ClassObj> getClassObjs() {
        return classObjs;
    }

    public void setClassObjs(List<ClassObj> classObjs) {
        this.classObjs = classObjs;
    }
}
