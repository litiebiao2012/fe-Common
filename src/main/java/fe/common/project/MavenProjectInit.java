package fe.common.project;

import fe.common.velocity.VelocityHelper;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fe on 2017/3/27.
 */
public class MavenProjectInit {
    private static final String PROJECT_PATH = "/Users/fe/Desktop";
    private static final String PARENT_PROJECT_NAME = "martell";
    private static final String PARENT_PROJECT_NAME_SUFFIX = "-all";

    private static List<String> childProjectNameList = new ArrayList<String>();

    static {
        childProjectNameList.add("common");
        childProjectNameList.add("bops");
        childProjectNameList.add("core");
        childProjectNameList.add("mobile-api");
    }



    public static void main(String[] args) throws Exception {
        String fullPath = PROJECT_PATH + "/" +PARENT_PROJECT_NAME + PARENT_PROJECT_NAME_SUFFIX;
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("parentName",PARENT_PROJECT_NAME);
        paramMap.put("childProjectList",childProjectNameList);
        String parentPomContent = VelocityHelper.getContent("parent-pom.xml.vm",paramMap);

        File parentDir = new File(fullPath);

        if (!parentDir.exists()) parentDir.mkdir();

        IOUtils.write(parentPomContent,new FileOutputStream(fullPath + "/pom.xml" ));

        System.out.println("######父工程"+ fullPath +"创建完毕#######");

        for (String childName : childProjectNameList) {
            String childNamePath = fullPath + "/" + PARENT_PROJECT_NAME + "-" + childName;

            File childDir = new File(childNamePath);
            if (!childDir.exists()) childDir.mkdir();

            paramMap.put("childName",childName);
            String childPomContent = VelocityHelper.getContent("child-pom.xml.vm",paramMap);

            IOUtils.write(childPomContent,new FileOutputStream(childNamePath + "/pom.xml" ));

            System.out.println("######子工程"+ childNamePath +"创建完毕#######");
        }

        String gitIgnoreContent = VelocityHelper.getContent("gitignore.vm",paramMap);
        IOUtils.write(gitIgnoreContent,new FileOutputStream(fullPath + "/.gitignore" ));
        System.out.println("######.gitignore创建完毕######");

    }
}
