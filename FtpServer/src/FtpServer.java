import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;


public class FtpServer {

    private int port;
    public static int maxClient=5;
    public static int clientNumber=0;
    private DataInputStream din;
    private DataOutputStream dout;

    ServerSocket serverSocket;

    public FtpServer(int port) throws IOException {

        this.serverSocket = new ServerSocket(port);
        Share.init();
    }

    public void listen() throws IOException {

        Socket socket = null;
        while(true) {  
            socket = this.serverSocket.accept();
            if(clientNumber == maxClient)
            {
//          PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//          out.println("MAX");
//          	this.dout = new DataOutputStream(socket.getOutputStream());
//          	this.dout.writeUTF("MAX");
            	socket.close();
            	continue;
            }
            ControllerThread thread = new ControllerThread(socket);
            thread.start();
            clientNumber++;
        }
    }

    public static void main(String args[]) throws IOException {
    		//System.out.println("Input the portNumber: ");
    		int portNumber = Integer.parseInt(args[0]);
    		maxClient = Integer.parseInt(args[1]);
        FtpServer ftpServer = new FtpServer(portNumber);
        ftpServer.listen();
    }
}