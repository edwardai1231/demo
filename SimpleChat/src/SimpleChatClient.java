import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class SimpleChatClient extends JFrame{
	private PrintWriter writer;
	private BufferedReader reader;
	private DataOutputStream dout;
	private DataInputStream din;
	private String IP;
	private int PortNumber;
	private JTextArea ta=new JTextArea();
	private JTextField tf=new JTextField();
	Socket socket;
	Container cc;
	
	public SimpleChatClient(String title)
	{
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.cc=this.getContentPane();
		final JScrollPane scrollPane=new JScrollPane();
		scrollPane.setBorder(new BevelBorder(BevelBorder.RAISED));
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(this.ta);
		this.cc.add(this.tf,"South");
		
		this.tf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					SimpleChatClient.this.dout=new DataOutputStream(SimpleChatClient.this.socket.getOutputStream());
					String msg=tf.getText();
					if(msg.equals("$EXIT"))
					{
						dout.writeUTF("$CLOSE");
						System.exit(0);
					}
					else {dout.writeUTF(msg);}
					ta.append("Client(ME): "+tf.getText()+'\n');
					ta.setSelectionEnd(ta.getText().length());
					tf.setText("");	
				}
				catch(IOException e1) {e1.printStackTrace();}			
			}
		});
	}
	private void connect()
	{
		this.ta.append("attempt to connect...."+'\n');
		try
		{
			this.socket=new Socket("127.0.0.1", 9999);   //localhost, can be changed if needs
			this.dout=new DataOutputStream(this.socket.getOutputStream());
			this.din=new DataInputStream(this.socket.getInputStream());
			this.ta.append("connection complete!"+'\n');
			this.GetServerMessage();
		}
		catch(Exception e) {e.printStackTrace();}
	}
	private void GetServerMessage()
	{
		try
		{
			while(true)
			{
				ta.append("\n");
				this.din=new DataInputStream(this.socket.getInputStream());
				String s=this.din.readUTF();
				if(s.equals("$JOIN"))
				{
					ta.append("You should JOIN fist!"+'\n');
					continue;
				}
				if(s.equals("$TALK"))
				{
					String targetname=this.din.readUTF();
					ta.append(targetname+" is talking to you");
					ta.append("\n");
					this.dout=new DataOutputStream(this.socket.getOutputStream());
					dout.writeUTF("$BYTALK");
					while(true)
					{
						String ss=this.din.readUTF();
						if(ss.equals("quit"))
						{
							dout.writeUTF("quit");
							break;
						}
						if(ss.equals("quit1"))
						{
							ta.append("Server: Your partner quit!"+'\n');
							dout.writeUTF("quit1");
							break;
						}
						if(ss.equals("quit2"))
						{
							ta.append("Server: Your partner exit!"+'\n');
							dout.writeUTF("quit2");
							break;
						}
						ta.append(targetname+": "+ss+'\n');
					}
					continue;
				}
				if(s.equals("$BYTALK"))
				{
					String targetname=this.din.readUTF();
					ta.append("\n");
					while(true)
					{
						String ss=this.din.readUTF();
						if(ss.equals("quit"))
						{
							dout.writeUTF("quit");
							break;
						}
						if(ss.equals("quit1"))
						{
							ta.append("Server: Your partner quit!"+'\n');
							dout.writeUTF("quit1");
							break;
						}
						if(ss.equals("quit2"))
						{
							ta.append("Server: Your partner exit!"+'\n');
							dout.writeUTF("quit2");
							break;
						}
						ta.append(targetname+": "+ss+'\n');
					}
					continue;
				}
				if(s.equals("username: "))
				{
					ta.append(s+'\n');
					continue;
				}
				ta.append("Server: "+s+'\n');
			}
		}
		catch(Exception e) {e.printStackTrace();}
		try
		{
			if(this.reader!=null)  this.reader.close();
			if(this.socket!=null)  this.socket.close();
		}
		catch(IOException e) {e.printStackTrace();}
	}
	public void getPortNumber(int PortNumber) {this.PortNumber=PortNumber;}
	public void getIP(String IP) {this.IP=IP;}
	public static void main(String[]args)
	{		
		SimpleChatClient c=new SimpleChatClient("ClientChatConsole");
		c.setSize(500,500);
		c.setVisible(true);
		c.connect();
	}
}
	


