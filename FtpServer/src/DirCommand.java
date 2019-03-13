import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class DirCommand implements Command{

    @Override
    public void getResult(String data, Writer writer,ControllerThread t) {
        System.out.println("execute LIST command........");
        String desDir = t.getNowDir()+data;
        //desDir=Share.rootDir;
        System.out.println(desDir);
        File dir = new File(desDir);
        if(!dir.exists()) {
            try {
                writer.write("210  File catalog not exists\r\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            StringBuilder dirs = new StringBuilder();
            System.out.println("File catalog：");
            dirs.append("File catalog:\n");

            Vector<String> allfiles=new Vector<>();
            String[] lists= dir.list();
            String flag = null;
            for(String name : lists) {
                File temp = new File(desDir+File.separator+name);
                if(temp.isDirectory()) {
                    flag = "d";
                }
                else {
                    flag = "f";
                }
                String oneinfo=flag+"rw-rw-rw-   1 ftp      ftp            "+temp.length()+" Dec 30 17:07 "+name;
                System.out.println(oneinfo);
                allfiles.add(oneinfo);
            } 

            try {
                writer.write("150 Opening data connection for directory list...\r\n");
                writer.flush();

                for(String oneinfo : allfiles)
                {
                    writer.write(oneinfo);
                    writer.write("\r\n");
                    writer.flush();
                }

                writer.write("end of files\r\n");
                writer.flush();

                writer.write("226 transfer complete...\r\n");
                writer.flush();
            } catch (NumberFormatException e) {

                e.printStackTrace();
            } catch (UnknownHostException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

}  