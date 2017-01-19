package fe.common.bean;

import fe.common.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fe on 2017/1/19.
 */
public class ClassUtils {
    public static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    public static List<Class<?>> loadClassByPath(String rootPath) {
        Assert.assertNotNull(rootPath);
        String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String str = rootPath.replace(".","/");
        File f = new File(classPath + "/" + str);
        Assert.assertNotNull(f,"file无法找到!");
        List<Class<?>> classList = new ArrayList<Class<?>>();
        load(new File[]{f},classList);
        return classList;
    }

    /**
     * 搜索工程classPath下的接口class
     * @param files
     * @param classList
     */
    public static void load(File[] files, List<Class<?>> classList) {
        for (File file : files) {
            if (file.isFile()) {
                loadClazz(file,classList);
            } else {
                File[] childFiles = file.listFiles();
                if (childFiles != null && childFiles.length > 0) {
                    for (File childFile : childFiles) {
                        if (childFile.isDirectory()) {
                            load(childFile.listFiles(),classList);
                        } else {
                            loadClazz(childFile,classList);
                        }
                    }
                }
            }

        }
    }
    public static void loadClazz(File file,List<Class<?>> classList) {
        int start = file.getPath().indexOf("com");
        int end = file.getPath().lastIndexOf(".");
        String className = file.getPath().substring(start,end).replace("/",".");
        try {
            Class<?> clazz = Class.forName(className);
            classList.add(clazz);
        } catch (Exception e) {
            logger.error("load clazz error, e : {}",e);
        }
    }
}
