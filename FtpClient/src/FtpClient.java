
import java.awt.EventQueue;

import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.net.ftp.FTPFile;

import java.awt.ScrollPane;
import java.awt.Label;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.awt.Scrollbar;

public class FtpClient implements ActionListener{


    //initialize parameter--------------------------------
    static FTPFile[] file;
    static String FTP="127.0.0.1";
    static String username="*******";
    static String password="*******";
    static String portNumber="";
    //initialize parameter--------------------------------


    private JFrame frame;
    private JTable table;
    static Ftp_Active ftp;
    public static Ftp_Active getFtp() {
        return ftp;
    }
    public static FTPFile[] getFile(){
        return file;
    }


    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FtpClient window = new FtpClient();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public FtpClient() {
        initialize();
    }


    private void initialize() {
        frame = new JFrame();
        frame.setTitle("FTP Client");
        frame.setBounds(100, 100, 470, 534);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);


        //show basic info(FTP username)-----------------------------------------------
        JLabel lblNewLabel = new JLabel("FTP IP address");
        lblNewLabel.setBounds(32, 8, 70, 15);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("username");
        lblNewLabel_1.setBounds(32, 40, 70, 15);
        frame.getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("password");
        lblNewLabel_2.setBounds(32, 55, 70, 15);
        frame.getContentPane().add(lblNewLabel_2);
        
        JLabel lblNewLabel_3 = new JLabel("portNumber");
        lblNewLabel_3.setBounds(32, 25, 70, 15);
        frame.getContentPane().add(lblNewLabel_3);

        JTextField url = new JTextField("127.0.0.1");   //FTP server address
        url.setBounds(110,8,82,15);
        frame.getContentPane().add(url);

        JTextField usernameField = new JTextField("admin"); //username
        usernameField.setBounds(110,40,82,15);
        frame.getContentPane().add(usernameField);

        JPasswordField passwordField = new JPasswordField("000000");  //password
        passwordField.setBounds(110,55,82,15);
        frame.getContentPane().add(passwordField);
        
        JTextField portNumberField = new JTextField("9999"); //portNumber
        portNumberField.setBounds(110,25,82,15);
        frame.getContentPane().add(portNumberField);



        //login bottom------------------------------------------------
        JButton login=new JButton("Login");
        login.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        login.setBackground(UIManager.getColor("Button.highlight"));
        login.setBounds(210, 10, 82, 23);
        frame.getContentPane().add(login);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Login==============");
                try {
                    FTP=url.getText().trim();
                    username=usernameField.getText().trim();
                    password=passwordField.getText().trim();
                    portNumber=portNumberField.getText().trim();
                    Global.port=Integer.parseInt(portNumber);

                    ftp=new Ftp_Active(FTP,username,password);
                    if(Ftp_Active.isLogined)
                    {
                        file=ftp.getAllFile();
                        setTableInfo();//show all the files info

                        url.setEditable(false);
                        usernameField.setEditable(false);
                        passwordField.setEditable(false);
                        portNumberField.setEditable(false);
                    }


                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showConfirmDialog(null, "wrong username or possword\n username："+username, "ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        //Login bottom------------------------------------------------
        JButton regist=new JButton("Register");
        regist.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        regist.setBackground(UIManager.getColor("Button.highlight"));
        regist.setBounds(210, 40, 82, 23);
        frame.getContentPane().add(regist);
        regist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Register==============");
                try {
                		
                	    FTP=url.getText().trim();
                    username=usernameField.getText().trim();
                    password=passwordField.getText().trim();
                    portNumber=portNumberField.getText().trim();
                    Global.port=Integer.parseInt(portNumber);

                    ftp=new Ftp_Active(FTP,username,password,"register");
                    if(Ftp_Active.isLogined)
                    {
                        file=ftp.getAllFile();
                        setTableInfo();//show all the files info

                        url.setEditable(false);
                        usernameField.setEditable(false);
                        passwordField.setEditable(false);
                        portNumberField.setEditable(false);
                    }


                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showConfirmDialog(null, "wrong username or possword\n username："+username, "ERROR_MESSAGE",JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        //upload bottom--------------------------------------------------
        JButton upload = new JButton("Upload");
        upload.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        upload.setBackground(UIManager.getColor("Button.highlight"));
        upload.setBounds(312, 40, 82, 23);
        frame.getContentPane().add(upload);
        upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //press upload bottom triggers upload action------------------------------------
                System.out.println("Upload！！！！！");
                int result = 0;
                File file = null;
                String path = null;
                JFileChooser fileChooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();
                System.out.println(fsv.getHomeDirectory());                //Get desktop route  
                fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                fileChooser.setDialogTitle("Please select the file you want to upload...");
                fileChooser.setApproveButtonText("Confirm");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                result = fileChooser.showOpenDialog(null);
                if (JFileChooser.APPROVE_OPTION == result) {
                    path=fileChooser.getSelectedFile().getPath();
                    System.out.println("path: "+path);
                    try {
                        ftp.upload(path);
                        System.out.println("successfully upload the file");
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            }
        });

        //upload bottom--------------------------------------------------



        //refresh bottom--------------------------------------------------
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try{
                    file=ftp.getAllFile();
                    setTableInfo();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        refresh.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        refresh.setBackground(UIManager.getColor("Button.highlight"));
        refresh.setBounds(312, 10, 82, 23);
        frame.getContentPane().add(refresh);
        //refresh bottom--------------------------------------------------
        
        JButton disconnect = new JButton("Disconnect");
        disconnect.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        disconnect.setBackground(UIManager.getColor("Button.highlight"));
        disconnect.setBounds(312, 70, 82, 23);
        disconnect.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent arg0)
        	{
                url.setEditable(true);
                usernameField.setEditable(true);
                passwordField.setEditable(true);
                portNumberField.setEditable(true);
                for(int i = model.getRowCount()-1;i>=0;i--) {
                    model.removeRow(i);
                   }
                
        		try{
                    ftp.Quit();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        	}
        }); 
        frame.getContentPane().add(disconnect);
    }
    DefaultTableModel model;

    //show basic info-----------------------------------------------
    private void setTableInfo()
    {
        //get all the files from FTP
        String[][] data1=new String[file.length][4];
        for(int row=0;row<file.length;row++)
        {

            data1[row][0]=file[row].getName();
            if(file[row].isDirectory())
            {
                data1[row][1]="folder";
            }
            else if(file[row].isFile()){
                String[] geshi=file[row].getName().split("\\.");
                data1[row][1]=geshi[1];
            }
            data1[row][2]=file[row].getSize()+"";
            data1[row][3]="download";
        }



        //table's columnName-----------------------------------------------------
        String[] columnNames = {"file", "filetype", "filesize(B)", ""  };
        model = new DefaultTableModel();
        model.setDataVector(data1, columnNames);

        //scroll--------------------------------------------------------
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(32, 100, 400, 384);
        frame.getContentPane().add(scrollPane);
        

        //table function------------------------------------------------------
        table = new JTable(model);
        scrollPane.setViewportView(table);
        table.setColumnSelectionAllowed(true);
        table.setCellSelectionEnabled(true);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.setToolTipText("press bottom to download");

        //table button initialize(last column bottom)--------------------
        ButtonColumn buttonsColumn = new ButtonColumn(table, 3);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // TODO Auto-generated method stub

    }
}