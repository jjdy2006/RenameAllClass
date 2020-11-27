import com.gosing.chatroom.utils.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 批量改类名和修改java文件中引用
 */
public class AllReName {
    /**
     * 新字符串,如果是去掉前缀后缀就留空，否则写上需要替换的字符串
     */
    static String newString = "MJ_";


    public static final String root_path = "/Users/liuqn/Desktop/test_rename/chatroom";

    public static void main(String[] args) {
//        readFile("/Users/liuqn/Desktop/test_rename/base/BaseDialog.java", "", "BaseDialog", "MJ_BaseDialog");
        dgRename(root_path);
    }

    /**
     * 递归遍历文件夹获取文件
     */
    private static void dgRename(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            System.out.println("文件不存在! path=" + path);
            return;
        }

        File[] fileArr = folder.listFiles();
        if (null == fileArr || fileArr.length == 0) {
            System.out.println("文件夹是空的! fileArr=" + fileArr);
            return;
        }

        //文件所在文件夹路径+新文件名
        File newDir = null;
        //新文件名
        String newName = "";
        //旧文件名
        String fileName = null;
        //文件所在父级路径
        File parentPath = new File("");
        for (File file : fileArr) {
            //是文件夹，继续递归，如果需要重命名文件夹，这里可以做处理
            if (file.isDirectory()) {
                System.out.println("文件夹:" + file.getAbsolutePath() + "，继续递归！");
                dgRename(file.getAbsolutePath());
                continue;
            }

            //是文件，判断是否需要重命名
            fileName = file.getName();
            parentPath = file.getParentFile();
            if (fileName.contains(".java")) {
                //文件名包含需要被替换的字符串
                newName = fileName.replaceAll(fileName, newString + fileName);
                //文件所在文件夹路径+新文件名
                newDir = new File(parentPath + "/" + newName);
                //重命名
                file.renameTo(newDir);
                System.out.println("修改后：" + newDir);
//                print(new File(root_path),0,fileName,newName);
                dgReFileStr(root_path, fileName.replace(".java", ""), newName.replace(".java", ""));

            }
        }

    }


    private static void dgReFileStr(String path, String oldStr, String newStr) {
        File folder = new File(path);
        if (!folder.exists()) {
            System.out.println("文件不存在! path=" + path);
            return;
        }

        File[] fileArr = folder.listFiles();
        if (null == fileArr || fileArr.length == 0) {
            System.out.println("文件夹是空的! fileArr=" + fileArr);
            return;
        }

        //文件所在文件夹路径+新文件名
        File newDir = null;
        //新文件名
        String newName = "";
        //旧文件名
        String fileName = null;
        //文件所在父级路径
        File parentPath = new File("");
        for (File file : fileArr) {
            //是文件夹，继续递归，如果需要重命名文件夹，这里可以做处理
            if (file.isDirectory()) {
                dgReFileStr(file.getAbsolutePath(), oldStr, newStr);
                continue;
            }

            //是文件，判断是否需要重命名
            fileName = file.getName();
            parentPath = file.getParentFile();
            if (fileName.contains(".java")) {
                //文件名包含需要被替换的字符串
                readFile(file.getAbsolutePath(), fileName, oldStr, newStr);
            }
        }

    }

    /**
     * 读取文件夹下的文件
     *
     * @return
     */
    public static void readFile(String absolutepath, String filename, String oldStr, String newStr) {

        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(absolutepath))));//数据流读取文件
            StringBuffer strBuffer = new StringBuffer();
            for (String temp = null; (temp = bufReader.readLine()) != null; temp = null) {
                if (temp.indexOf(oldStr) != -1) { // 判断当前行是否存在想要替换掉的字符
                    temp = temp.replace(oldStr, newStr); // 此处进行替换
                }
                strBuffer.append(temp);
                strBuffer.append(System.getProperty("line.separator"));//行与行之间的分割
            }
            bufReader.close();
            String newAbs = absolutepath.replace("base", "base_test");
            PrintWriter printWriter = new PrintWriter(absolutepath);//替换后输出的文件位置
            printWriter.write(strBuffer.toString().toCharArray());
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
