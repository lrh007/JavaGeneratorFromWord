import org.apache.log4j.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * @Description:
 * @Author: liangruihao
 * @Date: 2021/5/7 13:39
 */
public class MainFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = -1189035634361220261L;
    private static Logger logger = Logger.getLogger(MainFrame.class);
    JFrame mainframe;
    JPanel panel;
    //创建滚动条以及输出文本域
    JScrollPane jscrollPane;
    JEditorPane jEditorPane;
    public void show(){
        Toolkit kit = Toolkit.getDefaultToolkit(); // 定义工具包
        Dimension screenSize = kit.getScreenSize(); // 获取屏幕的尺寸
        int screenWidth = screenSize.width/2; // 获取屏幕的宽
        int screenHeight = screenSize.height/2; // 获取屏幕的高
        mainframe = new JFrame("标题ver-1.0");
        // Setting the width and height of frame
        mainframe.setSize(screenSize.width -200, screenSize.height - 200);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setResizable(false);//固定窗体大小

        int height = mainframe.getHeight(); //获取窗口高度
        int width = mainframe.getWidth(); //获取窗口宽度
        mainframe.setLocation(screenWidth-width/2, screenHeight-height/2);//将窗口设置到屏幕的中部
        //窗体居中，c是Component类的父窗口
        //mainframe.setLocationRelativeTo(c);
        Image myimage=kit.getImage("resourse/hxlogo.gif"); //由tool获取图像
        mainframe.setIconImage(myimage);
        try {
            initPanel();//初始化面板
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainframe.add(panel);
        mainframe.setVisible(true);
    }
    /* 创建面板，这个类似于 HTML 的 div 标签
     * 我们可以创建多个面板并在 JFrame 中指定位置
     * 面板中我们可以添加文本字段，按钮及其他组件。
     */
    public void initPanel() throws IOException {
        this.panel = new JPanel();
        panel.setLayout(null);
        jEditorPane = new JEditorPane();
        String path = "C:\\Users\\MACHENIKE\\Desktop\\testjava\\桔子数科-接口设计说明书v1.3.html";
        File file = new File(path);
        jEditorPane.setContentType("text/html");
//        jEditorPane.setEditable(false);     //请把editorPane设置为只读，不然显示就不整齐
        jEditorPane.setPage(file.toURI().toURL());
        jscrollPane = new JScrollPane(jEditorPane);
        jscrollPane.setBounds(10,20, mainframe.getWidth() - 20, mainframe.getHeight() - 70);
        this.panel.add(jscrollPane);
    }
    /**
     * 打开选择文件窗口并返回文件
     * @param type
     * @return
     */
    public File openChoseWindow(int type){
        JFileChooser jfc=new JFileChooser();
        jfc.setFileSelectionMode(type);//选择的文件类型(文件夹or文件)
        jfc.showDialog(new JLabel(), "选择");
        File file=jfc.getSelectedFile();
        return file;
    }
    public void windowClosed(WindowEvent arg0) {
        System.exit(0);
    }
    public void windowClosing(WindowEvent arg0) {
        System.exit(0);
    }


    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.show();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
