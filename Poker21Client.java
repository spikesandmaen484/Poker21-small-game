import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;



public class Poker21Client implements ActionListener{
    JFrame f;
    Container cp;
    JLabel txt1,txt2;
    JButton add,stop; //�[�P��B���P��         
    JPanel p1,p2,p3;
    JLabel[] b1=new JLabel[5];
    JLabel[] b2=new JLabel[5];
    Socket connection;
    int i,A1=0,A2=0,answer=0,point=0,youcount=0,cpucount=0,current;
    // A1:���aA�ƶq     A2:�q��A�ƶq        point:���a�I��             answer:�q���I��                  
    // youcount:���a�[�P�ƶq       cpucount:�q���[�P�ƶq             current:�I�ƼȦs                 
    int[] card=new int[52];
    Random r=new Random();
        
    ImageIcon img=new ImageIcon("cards/cover.png");
    Poker21Client(){
        try{
            connection=new Socket("192.168.168.125", 19999);
        } catch (IOException e){
            e.printStackTrace();
        };
        f=new JFrame("Poker21Client");
        cp=f.getContentPane();
        cp.setLayout(new BorderLayout());
        p1=new JPanel();
        p2=new JPanel();
        p3=new JPanel();
        
        txt1=new JLabel("YOU");
        txt2=new JLabel("CPU");
        
        p1.add(txt1);
        p2.add(txt2);
        
        add=new JButton("ADD");
        p3.add(add);
        stop=new JButton("STOP");
        p3.add(stop);
        
        add.addActionListener(this);
        stop.addActionListener(this);
        for (int i=0; i<52; i++) {   //*�~�P    
           card[i]=i;
        }
        for (int i=0; i<52; i++) {
           int j=r.nextInt(52);
           int temp=card[i];
           card[i]=card[j];
           card[j]=temp;
        }                           // 
    //�U���@�}�l����U�o��i            
        for (i=0;i<4;i++){
            if (i==0) {             //�Ĥ@�i�q���t�P        
                if (card[i]%13>=9) {            //*�I�ƧP�_      
                   current=10;  
                }
                else if (card[i]%13==0) {
                   current=11;
                }
                else {
                   current=(card[i]%13)+1;
                }                               //
                answer+=current;// �I�Ʋ֥[(�q��)      
                b2[i]=new JLabel(img);
                p2.add(b2[i]);
                if (card[i]%13==0) A2++; //�Y��A�hA2�ƶq+1       
            }
            else if (i%2==1) {
                b1[i/2]=new JLabel(new ImageIcon("cards/"+Integer.toString(card[i])+".png"));
                p1.add(b1[i/2]);
                if (card[i]%13>=9) {         //*�I�ƧP�_       
                   current=10;  
                }
                else if (card[i]%13==0) {
                   current=11;
                }
                else {
                   current=(card[i]%13)+1;
                }                            //
                point+=current; //�I�Ʋ֥[(���a)        
                if (card[i]%13==0) A1++;//�Y��A�hA1�ƶq+1      
            }
            else {
                b2[i/2]=new JLabel(new ImageIcon("cards/"+Integer.toString(card[i])+".png"));
                p2.add(b2[i/2]);
                if (card[i]%13>=9) {         //*�I�ƧP�_     
                   current=10;  
                }
                else if (card[i]%13==0) {
                   current=11;
                }
                else {
                   current=(card[i]%13)+1;
                }                            //
                answer+=current;
                if (card[i]%13==0) A2++;
            }   
        }
        cp.add(p1, BorderLayout.CENTER);
        cp.add(p2, BorderLayout.SOUTH);
        cp.add(p3, BorderLayout.NORTH); 
        f.pack();
        f.show();
        f.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        f.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
          if ((e.getSource()==add)) {     //���[�P��      
              b1[i-2]=new JLabel(new ImageIcon("cards/"+Integer.toString(card[i])+".png"));
              p1.add(b1[i-2]);
              cp.add(p1, BorderLayout.CENTER);
              if (card[i]%13==0) A1++;
              if (card[i]%13>=9) {          //*�I�ƧP�_      
                   current=10;  
                }
              else if (card[i]%13==0) {
                   current=11;
                }
              else {
                   current=(card[i]%13)+1;
                }                           //
              point+=current;
              //*���a�z�I����A�����p�A�hA����1�I                
              if ((A1>0) && (point>21)) {
                 A1--;
                 point-=10;
              }
              //
              youcount++;//�[�P�ƶq+1      
              i++;
              if ((youcount==3) && (point<=21)){   //���a�L����      
                  String g="IW";
                  SendRequest(g);
              }
          }
          if (e.getSource()==stop) {        //�����P��     
                   for (int j=0;j<3;j++) {
                        if (answer<17) {
                          b2[i-2-youcount]=new JLabel(new ImageIcon("cards/"+Integer.toString(card[i])+".png"));
                          p2.add(b2[i-2-youcount]);
                          if (card[i]%13==0) A2++;
                          if (card[i]%13>=9) {        //*�I�ƧP�_      
                              current=10;  
                          }
                          else if (card[i]%13==0) {
                              current=11;
                          }
                          else {
                              current=(card[i]%13)+1;
                          }                           //
                          answer+=current;
                          //*�q���z�I����A�����p�A�hA����1�I                     
                          if ((A2>0) && (answer>21)) {
                              A2--;
                              answer-=10;
                          }
                          //
                          cpucount++;//�[�P�ƶq+1      
                          i++;
                          }
                          else   
                             break;
                   }
                   if ((cpucount==3) && (answer<=21)) {    //�q���L����      
                       String g="CW";
                       SendRequest(g);
                   } 
                   else {
                      if (((point>answer)&&(point<=21)) || ((point<=21)&&(answer>21))) {  //Ĺ  
                          String g="W";
                          SendRequest(g);
                      } 
                      else if ((point==answer) || ((point>21)&&(answer>21))) {    //�M��    
                          String g="D";
                          SendRequest(g);
                      }
                      else {         //��   
                          String g="L";
                          SendRequest(g);
                      }  
                   } 
              } 
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
            if (instr.equals("WIN*")){
                type=JOptionPane.PLAIN_MESSAGE;
                message="�L����! WIN";
                title="Poker21";
                JOptionPane.showMessageDialog(f,message,title,type);
                add.setEnabled(false);
                stop.setEnabled(false);
                b2[0].addMouseListener(new MouseAdapter(){     //�I���i½�}�t�P        
                public void mouseClicked(MouseEvent e) { 
                   b2[0].setIcon(new ImageIcon("cards/"+Integer.toString(card[0])+".png"));
                } 
                }); 
            };
            if (instr.equals("LOSE*")){
                type=JOptionPane.PLAIN_MESSAGE;
                message="�q���L����! LOSE";
                title="Poker21";
                JOptionPane.showMessageDialog(f,message,title,type);
                add.setEnabled(false);
                stop.setEnabled(false);
                b2[0].addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e) { 
                   b2[0].setIcon(new ImageIcon("cards/"+Integer.toString(card[0])+".png"));
                } 
                }); 
            };
            if (instr.equals("WIN")){
                type=JOptionPane.PLAIN_MESSAGE;
                message="WIN! YOU point:"+point+" CPU point:"+answer;
                title="Poker21";
                JOptionPane.showMessageDialog(f,message,title,type);
                add.setEnabled(false);
                stop.setEnabled(false);
                b2[0].addMouseListener(new MouseAdapter(){          
                public void mouseClicked(MouseEvent e) { 
                   b2[0].setIcon(new ImageIcon("cards/"+Integer.toString(card[0])+".png"));
                } 
                }); 
            };
            if (instr.equals("DRAW")){
                type=JOptionPane.PLAIN_MESSAGE;
                message="DRAW YOU point:"+point+" CPU point:"+answer;
                title="Poker21";
                JOptionPane.showMessageDialog(f,message,title,type);
                add.setEnabled(false);
                stop.setEnabled(false);
                b2[0].addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e) { 
                   b2[0].setIcon(new ImageIcon("cards/"+Integer.toString(card[0])+".png"));
                } 
                }); 
            };
            if (instr.equals("LOSE")){
                type=JOptionPane.PLAIN_MESSAGE;
                message="LOSE! YOU point:"+point+" CPU point:"+answer;
                title="Poker21";
                JOptionPane.showMessageDialog(f,message,title,type);
                add.setEnabled(false);
                stop.setEnabled(false);
                b2[0].addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e) { 
                   b2[0].setIcon(new ImageIcon("cards/"+Integer.toString(card[0])+".png"));
                } 
                }); 
            };
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Poker21Client();
    }
}
