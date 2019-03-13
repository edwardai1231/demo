import java.io.File;
import java.io.IOException;
import java.io.Writer;

public class RegisterCommand implements Command{

    @Override
    public void getResult(String data, Writer writer, ControllerThread t) {

        System.out.println("Start register=======");
        try {
        String [] infos=data.split(",");

        if(Share.users.containsKey(infos[0]))
        {
            writer.write("550 User exists");
            writer.write("\r\n");
        }
        else{
            Share.users.put(infos[0],infos[1]);
            writer.write("1000 Register successfully");
            writer.write("\r\n");
        }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
