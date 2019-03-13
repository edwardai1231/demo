import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Share {

    public static  String rootDir = new File("").getAbsolutePath();
    public static Map<String,String> users = new HashMap<String,String>();
    public static HashSet<String> loginedUser = new HashSet<String>();
    public static HashSet<String> adminUsers = new HashSet<String>();

    //initialize
    public static void init(){
        String path = System.getProperty("user.dir") + "/config/server.xml";

        System.out.println(path);
        File file = new File(path);
        SAXBuilder builder = new SAXBuilder();
        try {
            Document parse = builder.build(file);
            Element root = parse.getRootElement();

            rootDir = root.getChildText("rootDir");
            System.out.print("rootDir is:");
            System.out.println(rootDir);

            Element usersE = root.getChild("users");
            List<Element> usersEC = usersE.getChildren();
            String username = null;
            String password = null;
            System.out.println("\nInfo for all users：");
            for(Element user : usersEC) {
                username = user.getChildText("username");
                password = user.getChildText("password");
                System.out.println("Username："+username);
                System.out.println("Password："+password);
                users.put(username,password);
            }

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}  