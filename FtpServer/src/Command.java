import java.io.Writer;

interface Command {

    public void getResult(String data,Writer writer,ControllerThread t);

}  