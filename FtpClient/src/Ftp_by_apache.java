
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.net.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


public class Ftp_by_apache {


    FTPClient f=null;
    public Ftp_by_apache(String url,String username,String password)
    {
        f=new FTPClient();
        this.get_connection(url,username,password);

    }


    public void get_connection(String url,String username,String password){

        try {
            f.connect(url);
            System.out.println("connect success!");
            f.setControlEncoding("GBK");

            boolean login=f.login(username, password);
            if(login)
                System.out.println("Login successfully!");
            else
                System.out.println("Login failed！");

        }
        catch (IOException e) {

            e.printStackTrace();
        }


    }

    public void close_connection() {

        boolean logout=false;
        try {
            logout = f.logout();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (logout) {
            System.out.println("Delete completely!");
        } else {
            System.out.println("Delete failed!");
        }

        if(f.isConnected())
            try {
                System.out.println("Connection closed！");
                f.disconnect();
            } catch (IOException e) {

                e.printStackTrace();
            }

    }


    public FTPFile[] getAllFile(){


        FTPFile[] files = null;
        try {
            files = f.listFiles();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for(FTPFile file:files)
        {

//            if(file.isDirectory())
//                System.out.println(file.getName()+"is folder");
//            if(file.isFile())
//                System.out.println(file.getName()+"is file");
        }
        return files;

    }


    public void upload(String File_path) throws IOException{

        InputStream input=null;
        String[] File_name = null;
        try {
            input = new FileInputStream(File_path);
            File_name=File_path.split("\\\\");
            System.out.println(File_name[File_name.length-1]);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(File_name[File_name.length-1]);
        f.storeFile(File_name[File_name.length-1], input);
        System.out.println("Upload completely!");

        if(input!=null)
            input.close();



    }


    public void download(String from_file_name,String to_path) throws IOException{



        OutputStream output=null;
        try {
            output = new FileOutputStream(to_path+from_file_name);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        f.retrieveFile(from_file_name, output);
        if(output!=null)
        {
            try {
                if(output!=null)
                    output.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }







}