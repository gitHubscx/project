package com.importOrExport;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.sunsoft.base.constant.ProductConstant;
import com.util.DateTimeUtil;

/**
 * 文件操作类
 */
public class FileUtil {
    private static final Logger logger = Logger.getLogger(FileUtil.class);

    /**
     * 上传文件
     * @param file 要上传的文件
     * @param width 缩放后的宽度
     * @param height 缩放后的高度
     * @param bb 比例不对时是否需要补白：true为补白; false为不补白;
     */
    public static String fileUpload(MultipartFile file, int width, int height, boolean bb) {
        StringBuffer sb = null;
        String fileName = file.getOriginalFilename();// 文件名
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);// 后缀名
        String filePath = "";// 文件地址
        String alias = "";// 缩略图地址
        long fileSize = file.getSize(); // 文件大小


        try {
            // 当日日期
            String datetime = DateTimeUtil.getNow("yyyyMMdd");
            /*
             * 判断是否存在文件夹
             */
            File targetFile = getUploadFile(prefix, ProductConstant.SYS_FILE_PATH + datetime);
            if (!targetFile.exists()) {
                targetFile.mkdirs(); // 如果没有文件夹 自动创建文件夹
            }
            file.transferTo(targetFile);

            if (fileSize != targetFile.length()) {
                sb = new StringBuffer();
                return sb.toString();
            } else {
                sb = new StringBuffer();
                sb.append("{");
                filePath = datetime + "/" + targetFile.getName();// 获取文件夹与文件名
                /*
                 * 图片进行压缩
                 */
                if (prefix.toLowerCase().equals("jpg") || prefix.toLowerCase().equals("png") || prefix.toLowerCase().equals("gif")) {
                    ImageUtil.scale2(targetFile.getParent() + "\\" + targetFile.getName(), ProductConstant.SYS_FILE_PATH + "/" + filePath, height, width, bb);
                    sb.append("alias:" + "'" + datetime + "/" + alias + "',");
                } 
                else if (prefix.equals("mp4") || prefix.equals("avi") || prefix.equals("rmvb") || prefix.equals("flv") || prefix.equals("mov")) {
                    sb.append("alias:" + "'images/video.jpg',");
                } 
                else if (prefix.equals("wav") || prefix.equals("mp3")) {
                    sb.append("alias:" + "'images/audio.jpg',");
                } 
                else if (prefix.equals("doc") || prefix.equals("docx")) {
                    sb.append("alias:" + "'images/word.jpg',");
                } 
                else if (prefix.equals("txt")) {
                    sb.append("alias:" + "'images/txt.png',");
                } else {
                    sb.append("alias:" + "'images/annex.jpg',");
                }
                sb.append("success:'上传成功',");
                sb.append("filename:" + "'" + fileName + "',");
                sb.append("prefix:" + "'" + prefix + "',");
                sb.append("filepath:" + "'" + filePath + "'");
                sb.append("}");
                return sb.toString();
            }
        } catch (Exception e) {
            sb = new StringBuffer();
            sb.append("{success:'上传失败'");
            return sb.toString();
        }
    }

    /**
     * 文件上传获取上传路径，不含压缩图片
     * @param file 文件
     * @return String 文件路径
     */
    public static String fileUpload(MultipartFile file) {
        StringBuffer sb = null;
        String fileName = "";// 文件名
        String prefix = "";// 后缀名
        String filePath = "";// 文件地址
        long fileSize = 0;// 文件大小
        String alias = "";// 类型缩略图地址
        fileName = file.getOriginalFilename();
        prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        fileSize = file.getSize();
        String datetime = DateTimeUtil.getNow("yyyyMMdd");
        File targetFile = getUploadFile(prefix, ProductConstant.SYS_FILE_PATH + datetime);
        if (!targetFile.exists()) {
            targetFile.mkdirs();// 如果没有文件夹 自动创建文件夹
        }
        try {
            file.transferTo(targetFile);
            if (fileSize != targetFile.length()) {
                sb = new StringBuffer();
                sb.append("{success:'上传失败'}");
                return sb.toString();
            } else {
                sb = new StringBuffer();
                sb.append("{");
                filePath = datetime + "/" + targetFile.getName();// 获取文件夹与文件名
                if (prefix.toLowerCase().equals("jpg") || prefix.toLowerCase().equals("png") || prefix.toLowerCase().equals("gif")) {
                    sb.append("alias:" + "'" + datetime + "/" + alias + "',");
                } else if (prefix.equals("mp4") || prefix.equals("avi") || prefix.equals("rmvb") || prefix.equals("flv") || prefix.equals("mov")) {
                    sb.append("alias:" + "'images/video.jpg',");
                } else if (prefix.equals("wav") || prefix.equals("mp3")) {
                    sb.append("alias:" + "'images/audio.jpg',");

                } else if (prefix.equals("doc") || prefix.equals("docx")) {
                    sb.append("alias:" + "'images/word.jpg',");
                } else if (prefix.equals("txt")) {
                    sb.append("alias:" + "'images/txt.png',");
                } else {
                    sb.append("alias:" + "'images/annex.jpg',");
                }
                sb.append("success:'上传成功',");
                sb.append("filename:" + "'" + fileName + "',");
                sb.append("prefix:" + "'" + prefix + "',");
                sb.append("filepath:" + "'" + filePath + "'");
                sb.append("}");
                return sb.toString();
            }
        } catch (Exception e) {
            logger.error("上传失败" + e);
            sb = new StringBuffer();
            sb.append("{success:'上传失败'");
            return sb.toString();
        }
    }


    /**
     * 生成目录文件
     * 
     * @param prefix 后缀
     * @param datetime
     * @return
     */
    public static File getUploadFile(String prefix, String datetime) {
        String filenameGuid = "";// 新生成文件名
        File targetFile = null;// 文件新目录
        filenameGuid = StringUtil.getUUID();
        targetFile = new File(datetime + "/", filenameGuid + "." + prefix);// 产生路径
        return targetFile;
    }


    /**
     * 读取TXT文本文件内容 -- 换行占位符为\n
     */
    public static String readTextFile(String filePath, String encoding) {
        StringBuffer sbStr = new StringBuffer();
        try {
            if(encoding == null || "".equals(encoding)) {
                encoding = "UTF-8";
            }

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
                logger.error("找不到指定的文件" + filePath);
            }
        } catch (Exception e) {
            logger.error("读取文件内容出错", e);
        }
        return sbStr.toString();
    }


    /**
     * 写文件
     * @param path 文件的路径
     * @param content 写入文件的内容
     */
    public static void writerText(String path, String content, String fileName) {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(path + "/" +fileName, true), "utf-8");
            BufferedWriter bw1 = new BufferedWriter(write);
            bw1.write(content);
            bw1.flush();
            bw1.close();
            write.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 生成文件提供下载
     * 
     * @param file
     * @param response
     * @throws Exception
     * @author using using@qq.com
     */
    public static void download(File file, HttpServletResponse response) {
        try {
            String filename = file.getName(); // 取得文件名。
            InputStream fis = new BufferedInputStream(new FileInputStream(file)); // 以流的形式下载文件。
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset(); // 清空response
            response.setHeader("Content-Type", "application/octet-stream"); // 设置response的Header
            // 修改linux服务器上下载乱码问题
            // response.addHeader("Content-Disposition", "attachment;filename=" +
            // new String(filename.getBytes(), "ISO8859-1"));
            response.addHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = response.getOutputStream();
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (Exception e) {
            logger.error("下载文件：FileUtil.download()", e);
        }
    }
}
