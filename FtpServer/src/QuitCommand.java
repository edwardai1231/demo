import java.io.IOException;
import java.io.Writer;

public class QuitCommand implements Command{

    @Override
    public void getResult(String data, Writer writer, ControllerThread t) {

        try {
            writer.write("221 goodbye.\r\n");
            writer.flush();
            writer.close();
            t.getSocket().close();
            System.out.println("quit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}  