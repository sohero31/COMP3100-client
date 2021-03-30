
import java.io.*;




import java.net.*;


public class MyClient {


    //Initialising the socket, the input/output stream, and the msg string


    //which will allow to send/receive messages.


    Socket s = null;


    String msg = "";


    //I have read that DataInputStream is outdated/deprecated


    //and I have found a way to use buffered reader in a way


    //that works just as well


    BufferedReader din = null;


    DataOutputStream dout = null;







    //Setting up the socket, the input and output streams


    public MyClient() {


        try {


            s = new Socket("localhost", 50000);


            din = new BufferedReader(new InputStreamReader(s.getInputStream()));


            dout = new DataOutputStream(s.getOutputStream());


        }


        catch(Exception e){System.out.println(e);}


    }


    


    public void Communicator() {


        messageSend("HELO");


        msg = messageReceive();


        messageSend("AUTH dom");


        msg = messageReceive();


        messageSend("REDY");


        msg = messageReceive();


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


            message += (char) din.read();


        }


        msg = message;


        //Added in an exception handler, kept getting errors


        //without it


        } catch (IOException e) {


            System.out.println(e);


        }


        return message;


    }


    //trying to use constructors and functions


    //instead of big chunks of code


    public static void main(String[] args) {


        //this establishes a new instance of the ClientTest


        //name client and then runs the communicator function


        //The communicator function just runs the simple message


        //send and receive functions.


            MyClient client = new MyClient();


            client.Communicator();


    /*    String str = (String)din.readLine();


        System.out.println("message= "+str);


        while (!str.equals("NONE")) {


            String strG = "GETS All";


            byte[] bG= strG.getBytes();


            dout.write(bG);


            String strSCH = "SCHD";


            byte[] bSC = strSCH.getBytes();


            dout.write(bSC);


            String strR = "REDY";


            byte[] bR = strR.getBytes();


            dout.write(bR);


        */


        


        


    }


}


