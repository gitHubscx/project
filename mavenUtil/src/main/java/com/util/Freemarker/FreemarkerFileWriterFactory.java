package com.util.Freemarker;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FreemarkerFileWriterFactory {

    private static Configuration cfg;
    private static final String TEMPLATEURL = "template"; // ftl模版路径

    /**
     * 获取Configuration
     *
     * @param url
     * @return
     */
    public static Configuration getConfiguration(String url) {
        if (cfg == null) {
            cfg = new Configuration();
            url = FreemarkerFileWriterFactory.class.getResource("/").getPath() + url;
            // System.out.println(url);
            File file = new File(url);
            try {
                cfg.setDirectoryForTemplateLoading(file);
                cfg.setObjectWrapper(new DefaultObjectWrapper());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cfg;
    }

    /**
     * 创建文件
     *
     * @param table          模版使用的对象
     * @param fltFileName    应用模版的名称
     * @param outPutNamePath 文件输出路径，例子src/xx.xx
     */
    public static void dataSourceOut(Object obj, String fltFileName, String outPutNamePath) {
        Configuration configuration = FreemarkerFileWriterFactory.getConfiguration(TEMPLATEURL);
        Writer out = null;
        try {
            // 初始freemarker模版
            Template temp = configuration.getTemplate(fltFileName, "UTF-8");
            // 设置文件编码
            temp.setEncoding("UTF-8");
            // 生成文件
            out = new FileWriter(new File(outPutNamePath));
            // 套用模版
            temp.process(obj, out);
            // temp.process(table, new OutputStreamWriter(System.out));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
