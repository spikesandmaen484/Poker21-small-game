import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class ABCoolClient implements ActionListener{
    JFrame f;
    Container cp;
    JLabel txt;
    JTextField guess;
    String msg="Start\n";
    JTextArea ta;
    Socket connection;
    
    ABCoolClient(){
        try{
            connection=new Socket("120.107.155.180", 19999);
        } catch (IOException e){
            e.printStackTrace();
        };
        f=new JFrame("ABCoolClient");
        cp=f.getContentPane();
        cp.setLayout(new BorderLayout());
        JPanel p=new JPanel();
        txt=new JLabel("My Guess");
        p.add(txt);
        guess=new JTextField(10);
        guess.addActionListener(this);
        p.add(guess);
        cp.add(p, BorderLayout.NORTH);
        ta=new JTextArea(msg,20,40);
        cp.add(ta, BorderLayout.SOUTH);
        f.pack();
        f.show();
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
    }
    
    public void actionPerformed(ActionEvent e){
        SendRequest(guess.getText());
    }
    
    public void SendRequest(String str){
        String instr;
        int type;
        String message;
        String title;
        try {
            DataOutputStream out=new DataOutputStream(connection.getOutputStream());                    
            out.writeUTF(str);
            out.flush();
            DataInputStream in=new DataInputStream(connection.getInputStream());
            instr=in.readUTF();
            ta.append(str+"\t"+instr+"\n");
            if (instr.equals("3A0B")){
                type=JOptionPane.PLAIN_MESSAGE;
                message="µª¹ï¤F";
                title="ABCool";
                JOptionPane.showMessageDialog(f,message,title,type);
                // End Game ?
            };
            guess.setText("");
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new ABCoolClient();
    }
}
