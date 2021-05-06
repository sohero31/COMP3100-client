import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

import Folder.Server;	

public class Client_Stage1 {
	//Initialising the socket, the input/output stream, and the msg string
	//which will allow to send/receive messages.
	Socket s = null;
	String msg = "";
	//I have read that DataInputStream is outdated/deprecated 
	//and I have found a way to use buffered reader in a way 
	//that works just as well
	BufferedReader din = null;
	DataOutputStream dout = null;
	Boolean flag = false;
	List<Server> sArr  = new ArrayList<Server>();
	int largeServer  = 0;
	
	
	


	//Setting up the socket, the input and output streams
	public Client_Stage1() {
		try {
			s = new Socket("localhost", 50000);
			din = new BufferedReader(new InputStreamReader(s.getInputStream()));
			dout = new DataOutputStream(s.getOutputStream());
		}
		catch(Exception e){System.out.println(e);}
	}
	
	
	public void Communicator() {
		//this is the pseudo-authentication betweeen server and client
		messageSend("HELO\n");
		msg = messageReceive();
		messageSend("AUTH dom\n");
		msg = messageReceive();
		parse();
		messageSend("REDY\n");
		msg = messageReceive();
		

		//this is the section that schedules jobs
		while (!msg.equals("NONE")) {
			if (msg.equals("OK")) {
					messageSend("REDY\n");
					msg = messageReceive();
				}
			
			String[] jobData = new String[10];
			jobData = msg.split("\\s+");
			int count = Integer.parseInt(jobData[2]);
			if (!jobData[0].equals("JCPL")) {	
				
				messageSend("SCHD "+count+" "+sArr.get(largeServer).type + " " + "0\n");
				msg = messageReceive();
			} else {
				messageSend("REDY\n");
				msg = messageReceive();
			}
				
			
			
		}quitCommunicating();
	}
	
	//simple and self-explanatory quitting function.
	//sends quit to the client, if it gets the quit
	//message in response, the input/output and 
	//server closes
	public void quitCommunicating() {
		try {
			messageSend("QUIT\n");
			msg = messageReceive();
			if (msg == "QUIT") {
				din.close();
				dout.close();
				s.close();
			}
		} catch (IOException i) {
			System.out.println(i);
		}
	}
	//sending a message using the DataOutputStream 
	//and then flushing it. Also added in an exception handler
	//this code is the same as before, just wrapped in a function
	//for neatness
	public void messageSend(String message) {
			try {
				dout.write(message.getBytes());
				dout.flush();
			} catch (IOException e) {
				System.out.println(e);
			}
	}
	//receiving a message and returning a string.
	public String messageReceive() {
		String message = "";
		try {
			//no need to do anything if it is not ready
			while (!din.ready()){}
			//if it is ready, then receive the data.
			while (din.ready()) {
			message += din.readLine();
		}
		msg = message;
		//Added in an exception handler, kept getting errors 
		//without it
		} catch (IOException e) {
			System.out.println(e);
		}
		return message;
	}
	public int largeServer() {
		int largeServer = sArr.get(0).id;
		for (int i=1; i < sArr.size(); i++) {
			if (sArr.get(i).coreCount > sArr.get(largeServer).coreCount) {
				largeServer = sArr.get(i).id;
			}
		}
		return largeServer;
	}
	public void parse(){
        try{
            File sysXML = new File("ds-system.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sysXML);

		
            doc.getDocumentElement().normalize();
			NodeList servers = doc.getElementsByTagName("server"); //creating a list of nodes for server
				for (int i = 0; i < servers.getLength(); i++) {	       //looking at each node and retaining information to print it
				Element server = (Element) servers.item(i);
				String t = server.getAttribute("type");
				int l = Integer.parseInt(server.getAttribute("limit"));
				int b = Integer.parseInt(server.getAttribute("bootupTime"));
				float r = Float.parseFloat(server.getAttribute("hourlyRate"));
				int c = Integer.parseInt(server.getAttribute("coreCount"));
				int m = Integer.parseInt(server.getAttribute("memory"));
				int d = Integer.parseInt(server.getAttribute("disk"));
				Server temp = new Server(i, t, l, b, r, c, m, d);	//initialising data server temp to contain all the nodes 
				sArr.add(temp);					
				//System.out.println(sArr.get(sArr.size()-1).coreCount);			//printing out the number of cores
			}
			largeServer = largeServer();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

    }
	//trying to use constructors and functions
	//instead of big chunks of code
	public static void main(String[] args) {
		//this establishes a new instance of the ClientTest
		//name client and then runs the communicat or function
		//The communicator function just runs the simple message
		//send and receive functions.
			
			Client_Stage1 client_Stage1 = new Client_Stage1();
			client_Stage1.Communicator();
		
	}
}
