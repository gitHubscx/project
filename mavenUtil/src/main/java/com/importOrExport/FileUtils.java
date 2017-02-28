package com.importOrExport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtils {
    /**
     * 读取TXT文本文件内容 -- 换行占位符为\n
     *
     * @return
     */
    public static String readTextFile(String filePath) {
        StringBuffer sbStr = new StringBuffer();
        try {
            String encoding = "UTF-8";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sbStr.append(lineTxt + "\n");
                }
                read.close();
            } else {
                System.out.println(filePath + "找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return sbStr.toString();
    }

    /**
     * 删除目录
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 删除目录
     */
    public static boolean deleteDir(String dir) {
        return deleteDir(new File(dir));
    }

    /**
     * 清空目录
     */
    public static boolean clearDir(File dir) {
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除目录
     */
    public static boolean clearDir(String dir) {
        return clearDir(new File(dir));
    }

}
