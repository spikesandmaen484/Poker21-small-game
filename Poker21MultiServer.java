import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Poker21MultiServer implements Runnable{
  private Socket connection;
    private int ID;
    
    Poker21MultiServer(Socket s, int i){
      this.connection=s;
      this.ID=i;
    }
    
    public void run(){
      
      String instr;  //讀取傳來的文字        
      String result;  //記錄結果      
      
        while (connection.isConnected()){
        try {         
        DataInputStream in=new DataInputStream(connection.getInputStream());
        instr=in.readUTF();
        //*玩家過五關(以WIN*表示)          
          if (instr.equals("IW")){
              result="WIN*";
              System.out.println("ID:"+ID+" Result:"+result);
              DataOutputStream out=new DataOutputStream(connection.getOutputStream());            
              out.writeUTF(result);
              out.flush();
              in.close();
              out.close();
            //connection.close();
          }
        //
        //*電腦過五關(以LOSE*表示)         
          else if (instr.equals("CW")) {
              result="LOSE*";
              System.out.println("ID:"+ID+" Result:"+result);
              DataOutputStream out=new DataOutputStream(connection.getOutputStream());            
              out.writeUTF(result);
              out.flush();
              in.close();
              out.close();
            //connection.close();
          }
        //
          else if (instr.equals("W")){  //結果為贏      
              result="WIN";
              System.out.println("ID:"+ID+" Result:"+result);
              DataOutputStream out=new DataOutputStream(connection.getOutputStream());            
              out.writeUTF(result);
              out.flush();
              in.close();
              out.close();
            //connection.close();
          }
          else if (instr.equals("D")){    //結果為和局       
              result="DRAW";
              System.out.println("ID:"+ID+" Result:"+result);
              DataOutputStream out=new DataOutputStream(connection.getOutputStream());            
              out.writeUTF(result);
              out.flush();
              in.close();
              out.close();
            //connection.close();
          }
          else {      //結果為輸      
              result="LOSE";
              System.out.println("ID:"+ID+" Result:"+result);
              DataOutputStream out=new DataOutputStream(connection.getOutputStream());            
              out.writeUTF(result);
              out.flush();
              in.close();
              out.close();
            //connection.close();
          } 
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
                Runnable r=new Poker21MultiServer(connection, ++count);
                Thread t=new Thread(r);
                t.start();
            }
        } catch (IOException e){
        };
    }
}
