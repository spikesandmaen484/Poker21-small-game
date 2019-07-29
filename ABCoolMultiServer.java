import java.net.*;
import java.io.*;
import java.util.*;

public class ABCoolMultiServer implements Runnable{
	private Socket connection;
    private int ID;
    
    ABCoolMultiServer(Socket s, int i){
    	this.connection=s;
    	this.ID=i;
    }
    
    public void run(){
  	    String guess;
	    String answer;
    	String result="";
        Random r=new Random();
       	int d[]={1,2,3,4,5,6,7,8,9}, x, temp;
       	for (int i=0; i<9; i++){
            x=r.nextInt(9);
   	        temp=d[x];
       	    d[x]=d[i];
            d[i]=temp;
       	};
        answer=Integer.toString(d[0])+Integer.toString(d[1])+Integer.toString(d[2]);
       	System.out.println("ID:"+ID+"\tAnswer:"+answer);
       	while (connection.isConnected()){
	    	try {        	
				DataInputStream in=new DataInputStream(connection.getInputStream());
				guess=in.readUTF();
            	int a=0, b=0;
   	        	for (int i=0; i<3; i++){
	       	        for (int j=0; j<3; j++){
   		       	        if (answer.charAt(i)==guess.charAt(j)){
       		               	if (i==j)
        	                	a++;
   	            	        else
   	   	            	        b++;
    	                };
	    	        };
   		    	};
	            result=a+"A"+b+"B";
		        System.out.println("ID:"+ID+" Result:"+result);
				DataOutputStream out=new DataOutputStream(connection.getOutputStream());            
				out.writeUTF(result);
				out.flush();
    			if (result.equals("3A0B")){
		        	in.close();
		        	out.close();
   					//connection.close();
    			};
	    	} catch (IOException e){
	    		//System.out.println(e);
        	};
       	};
    }

    public static void main(String[] args) {
  	    int port=19999;
  	    int count=0;
        try{
            ServerSocket socket1=new ServerSocket(port);
            System.out.println("MultiSocketServer Initialized");
            while (true) {
            	Socket connection=socket1.accept();
                Runnable r=new ABCoolMultiServer(connection, ++count);
                Thread t=new Thread(r);
               	t.start();
            }
        } catch (IOException e){
        };
    }
}
