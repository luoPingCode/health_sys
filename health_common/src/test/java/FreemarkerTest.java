import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LuoPing
 * @date 2022/11/6 22:56
 */
public class FreemarkerTest {
//    public static void main(String[] args) throws Exception {
////        1、那模板
//        Configuration configuration = new Configuration(Configuration.getVersion());
//        configuration.setDefaultEncoding("UTF-8");
//        configuration.setDirectoryForTemplateLoading(new File("D:\\ftl"));
//        Template template = configuration.getTemplate("test.ftl");
////        拿数据
//        Map<String, String> map = new HashMap<>();
//        map.put("name", "罗评");
//        map.put("message", "工资过万");
////               生成文本
//        FileWriter fileWriter = new FileWriter(new File("D:\\ftl\\test.html"));
//        template.process(map,fileWriter);
//        fileWriter.flush();
//        fileWriter.close();
//
//    }
}
