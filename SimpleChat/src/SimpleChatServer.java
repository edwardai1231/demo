/*import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;*/

import java.net.*;
import java.io.*;
import java.util.*;

public class SimpleChatServer{
	private int portNumber;
	private static Hashtable<String,Boolean>nameList=new Hashtable<String,Boolean>();
	private static Hashtable<String,InetAddress>nameIP=new Hashtable<String,InetAddress>();
	private static Hashtable<String,TCPThreadServerSocket>nameThread=new Hashtable<String,TCPThreadServerSocket>();
	private ServerSocket server;
	
	public void getPortNumber(int portNumber) {this.portNumber=portNumber;}
	
	private void Getserver()
	{
		try{
			server=new ServerSocket(this.portNumber);
		}
		catch(Exception e) {e.printStackTrace();}
        while(true){  
            try{
				Socket client=server.accept();
				TCPThreadServerSocket handler=new TCPThreadServerSocket(client);  
				new Thread(handler).start();
			}
			catch(Exception e) {e.printStackTrace();}
        }
	}
	
	
	class TCPThreadServerSocket implements Runnable{
		private Socket sockcet=null;
		private DataInputStream din=null;
		private DataOutputStream dout=null;
		public TCPThreadServerSocket target=null;
		private String name;
		private String targetname;
		public boolean BindState=false;
		private boolean userflag=true;
	
		public TCPThreadServerSocket(Socket s)
		{
			this.sockcet=s;
		}
	
		public void run()
		{
			try
			{
				while(true)
				{
					this.din=new DataInputStream(this.sockcet.getInputStream());
					this.dout=new DataOutputStream(this.sockcet.getOutputStream());
					String data=this.din.readUTF();
					
					if(data.equals("$JOIN")) 
					{
						if(!this.userflag) {this.dout.writeUTF("You can only register one username!"); continue;}
						this.dout.writeUTF("username: ");
						this.name=this.din.readUTF();
						if(nameList.containsKey(this.name)) {this.dout.writeUTF("NAMECONFLICT");}
//						if(nameIP.containsValue(this.sockcet.getInetAddress()))   //same IP can only makes one connection
//						{this.dout.writeUTF("You can only register one username!");}
						else 
						{
							nameList.put(name, true);
							nameIP.put(name, this.sockcet.getInetAddress());
							nameThread.put(name, TCPThreadServerSocket.this);
							this.dout.writeUTF("Username successfully saved in server!");
							this.userflag=false;
						}					
						continue;
					}
				
					if(data.equals("$LIST"))
					{
						Enumeration<String>en=nameList.keys();
						while(en.hasMoreElements())
						{
							String sss=en.nextElement();
							if(nameList.get(sss)) {this.dout.writeUTF(sss+"  IDLE");}
							if(!nameList.get(sss)) {this.dout.writeUTF(sss+"  BUSY");}
						}
						continue;
					}
					
					if(data.equals("$BYTALK")){
						target=nameThread.get(targetname);   //target is A now
						this.BindState=true;
						while(this.BindState)
						{
							this.din=new DataInputStream(this.sockcet.getInputStream());
							String msg=this.din.readUTF();
							if(msg.equals("quit"))
							{       //B sends quit msg to server
								target.sendMsg("quit1");  //server returns quit msg to A
								nameList.put(name, true); 
								nameList.put(targetname, true);
								target.BindState=false;
								this.BindState=false;
									
								break;
							}
							if(msg.equals("quit1"))    //only stop talking, both still online
							{      
								target.sendMsg("quit");
								nameList.put(name, true);
								nameList.put(targetname, true);
								target.BindState=false;
								this.BindState=false;
									
								break;
							}
							if(msg.equals("quit2"))   //offline
							{     
								nameList.put(name, true);
								this.BindState=false;
						
								continue;
							}
							if(msg.equals("$CLOSE"))
							{
								target.sendMsg("quit2");
									
								nameList.put(targetname, true);
								target.BindState=false;
									
									
								nameList.remove(name);
								nameThread.remove(name);
								nameIP.remove(name);
							}
									
							else {target.sendMsg(msg);}
						}
						continue;
					}
					
					if(data.equals("$TALK"))
					{
						this.dout.writeUTF("Who you want to talk with?");
						targetname=this.din.readUTF();
						if(!nameList.containsKey(targetname)) {this.dout.writeUTF("User does not exist!");}
						if(nameList.containsKey(targetname)&&nameList.get(targetname)==false)
						{this.dout.writeUTF("User is busy now!");}
						if(nameList.containsKey(targetname)&&nameList.get(targetname)==true)
						{
							this.dout.writeUTF("You can start talking now!");
							
							nameList.put(name, false);
							nameList.put(targetname, false);
							target = nameThread.get(targetname);
							target.targetname = name;
							
							//target.sendMsg(name + " is talking to you");
							target.sendMsg("$TALK");
							target.sendMsg(name);
							this.sendMsg("$BYTALK");
							this.sendMsg(targetname);
							this.BindState = true;
							while(this.BindState){
								this.din=new DataInputStream(this.sockcet.getInputStream());
								String msg=this.din.readUTF();
								if(msg.equals("quit"))
								{
									target.sendMsg("quit1");
									nameList.put(name, true);
									nameList.put(targetname, true);
									target.BindState=false;
									this.BindState=false;
									
									break;
								}
								if(msg.equals("quit1"))
								{
									target.sendMsg("quit");
									nameList.put(name, true);
									nameList.put(targetname, true);
									target.BindState=false;
									this.BindState=false;
									
									break;
								}
								if(msg.equals("quit2"))
								{
									nameList.put(name, true);
									this.BindState=false;
						
									continue;
								}
								if(msg.equals("$CLOSE"))
								{
									target.sendMsg("quit2");
									
									nameList.put(targetname, true);
									target.BindState=false;
									
									
									nameList.remove(name);
									nameThread.remove(name);
									nameIP.remove(name);
						
									continue;
								}
								else {target.sendMsg(msg);}
							}
						}
						continue;
					}
					
					if(data.equals("$CLOSE"))
					{
						nameList.remove(name);
						nameThread.remove(name);
						nameIP.remove(name);
						
						continue;
					}
					
					System.out.println("Client:" + data); 			
				}
		
			} 
			catch (IOException e) { e.printStackTrace();} 
			finally 
			{  
				try 
				{               
					this.din.close();  
					this.dout.close();  
					this.sockcet.close();  
				} 
			catch (IOException e) {e.printStackTrace();}  
			}
		}
		public void sendMsg(String str)
		{
            try {
				this.dout=new DataOutputStream(this.sockcet.getOutputStream());
                //dos = new DataOutputStream(socket.getOutputStream());
                this.dout.writeUTF(str);
            } catch (IOException e) {e.printStackTrace();}
        }
	}
	
	public static void main(String[]args)
	{
		SimpleChatServer s=new SimpleChatServer();
		System.out.print("PortNumber: ");
		Scanner ss=new Scanner(System.in);
		int port=ss.nextInt();
		s.getPortNumber(port);
		s.Getserver();
	}
}

