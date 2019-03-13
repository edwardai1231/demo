import org.apache.commons.net.ftp.FTPFile;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Ftp_Active {

    private BufferedReader controlReader;
    private PrintWriter controlOut;

    private String ftpusername;
    private String ftppassword;
    
    private DataInputStream din;
    private DataOutputStream dout;


    //private static final int PORT = 9999;
    private int PORT = Global.port;

    public static boolean isLogined=false;
    
    private Socket socket;


    public Ftp_Active(String url, String username, String password) {
        try {
            socket = new Socket(url, PORT);//make a connection with server

            this.setUsername(username);
            this.setPassword(password);

            this.controlReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.controlOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            this.initftp();  //login to the FTP server
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public Ftp_Active(String url, String username, String password,String regist) {
        try {
            socket = new Socket(url, PORT);//make a connection with server

            this.setUsername(username);
            this.setPassword(password);

            this.controlReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.controlOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            
//            this.din = new DataInputStream(socket.getInputStream());
//            this.dout = new DataOutputStream(socket.getOutputStream());
//            
//            if(din.equals("MAX"))
//            {
//            		dout.writeUTF("MAX");
//            		String response = din.readLine();
//            		JOptionPane.showConfirmDialog(null, response, "ERROR_MESSAGE",JOptionPane.OK_OPTION);
//            			throw new IOException("SimpleFTP received an unknown response after sending the register: " + response);
//            }

            String msg;
            do {
                msg = this.controlReader.readLine();
                System.out.println(msg);
                
            } while (!msg.startsWith("220 "));

            this.controlOut.println("REGISTER " + this.ftpusername+","+this.ftppassword);

            String response = this.controlReader.readLine();
            System.out.println(response);

            if (!response.startsWith("1000 ")) {
                JOptionPane.showConfirmDialog(null, response, "ERROR_MESSAGE",JOptionPane.OK_OPTION);
                throw new IOException("SimpleFTP received an unknown response after sending the register: " + response);

            }
            System.out.println("register succesfully=========");
            socket.close();
            JOptionPane.showConfirmDialog(null, response, "Successful",JOptionPane.OK_OPTION);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void initftp() throws Exception {
        String msg;
        do {
            msg = this.controlReader.readLine();
            System.out.println(msg);
        } while (!msg.startsWith("220 "));

        this.controlOut.println("USER " + ftpusername);

        String response = this.controlReader.readLine();
        System.out.println(response);

        if (!response.startsWith("331 ")) {
            JOptionPane.showConfirmDialog(null, response, "ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE);
            throw new IOException("SimpleFTP received an unknown response after sending the user: " + response);

        }

        controlOut.println("PASS " + ftppassword);

        response = this.controlReader.readLine();
        System.out.println(response);
        if (!response.startsWith("230 ")) {
            JOptionPane.showConfirmDialog(null, response, "ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE);
           throw new IOException("SimpleFTP was unable to log in with the supplied password: "+ response);
        }

        isLogined=true;
    }

    private void setUsername(String username) {
        this.ftpusername = username;
    }

    private void setPassword(String password) {
        this.ftppassword = password;
    }

    //get all the files and the folder name
    public FTPFile[] getAllFile() throws Exception {
        String response;
        // Send LIST command
        this.controlOut.println("LIST");

        // Read command response
        response = this.controlReader.readLine();
        System.out.println(response);


        // Read data from server
        Vector<FTPFile> tempfiles = new Vector<>();

        String line = null;
        while ((line = controlReader.readLine()) != null) {
            if(line.equals("end of files")) break;
            
            System.out.println(line);
            FTPFile temp = new FTPFile();
            this.setFtpFileInfo(temp, line);
            tempfiles.add(temp);
        }

        // Read command response
        response = controlReader.readLine();
        System.out.println(response);

        FTPFile[] files = new FTPFile[tempfiles.size()];
        tempfiles.copyInto(files);  //put vector data to the array

        return files;

    }

    public void Quit() {
    	this.controlOut.println("CLOSE");
    	/*try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    }
    
    private void setFtpFileInfo(FTPFile in, String info) {
        String infos[] = info.split(" ");
        Vector<String> vinfos = new Vector<>();
        for (int i = 0; i < infos.length; i++) {
            if (!infos[i].equals(""))
                vinfos.add(infos[i]);
        }
        in.setName(vinfos.get(8));
        in.setSize(Integer.parseInt(vinfos.get(4)));
        String type=info.substring(0,1);
        if(type.equals("d"))
        {
            in.setType(1);//set as folder
        }else
        {
            in.setType(0);//set as file
        }

    }


    //create an InputStream to upload local file
    public void upload(String File_path) throws Exception {
        //read local file-----------------------------------
        System.out.print("File Path :" + File_path);
        File f = new File(File_path);
        if (!f.exists()) {
            System.out.println("File not Exists...");
            return;
        }
        FileInputStream is = new FileInputStream(f);
        BufferedInputStream input = new BufferedInputStream(is);
        //-----------------------------------------------

        // Send PORT command
        String url="127.0.0.1";
        int dataport=(int)(Math.random()*100000%9999)+1024;
        String portCommand="MYPORT "+ url+","+dataport;
        controlOut.println(portCommand);

        String response;
        response=controlReader.readLine();
        System.out.println(response);


        // Send command STOR
        controlOut.println("STOR " + f.getName());

        // Open data connection
        ServerSocket dataSocketServ = new ServerSocket(dataport);
        Socket dataSocket=dataSocketServ.accept();

        // Read command response
        response = controlReader.readLine();
        System.out.println(response);

        // Read data from server
        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        output.flush();
        input.close();
        output.close();
        dataSocket.close();


        response = controlReader.readLine();
        System.out.println(response);

    }



    //from_file_name is the download file name, to_path is the download route
    public void download(String from_file_name, String to_path) throws Exception {
        // Send PORT command
        String url="127.0.0.1";
        int dataport=(int)(Math.random()*100000%9999)+1024;
        String portCommand="MYPORT "+ url+","+dataport;
        controlOut.println(portCommand);

        String response;
        response=controlReader.readLine();
        System.out.println(response);

        //send RETR command
        controlOut.println("RETR " + from_file_name);

        // Open data connection
        ServerSocket dataSocketServ = new ServerSocket(dataport);
        Socket dataSocket=dataSocketServ.accept();


        // Read data from server
        BufferedOutputStream output = new BufferedOutputStream(
                new FileOutputStream(new File(to_path, from_file_name)));
        BufferedInputStream input = new BufferedInputStream(
                dataSocket.getInputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }

        output.flush();
        output.close();
        input.close();
        dataSocket.close();

        response = controlReader.readLine();
        System.out.println(response);

        response = controlReader.readLine();
        System.out.println(response);
    }

}