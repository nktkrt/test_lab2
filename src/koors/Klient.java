package koors;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;
public class Klient extends javax.swing.JFrame {
    String my_nickname;
    ArrayList<Konferen> my_confs=new ArrayList<Konferen>();
    ArrayList<Konferen> my_adminki=new ArrayList<Konferen>();

    class Listen extends Thread{
        public void run(){
            String ss="";
            try {
                while(true){
                    leaveif:{
                   ss=is.readUTF();  
                   
                   if (ss.length()==0) 
                   { close(); break; }
                   else
                    if (ss.charAt(0)=='/')
                    {
                        if (ss.contains("/nicktaken "))
                        {
                           out.append("logged in as "+ss.substring(11)+"\n");
                       User newme=(User)isob.readObject();
                           my_nickname=newme.nickname;
                            my_confs=newme.sostoyu;
                            my_adminki=newme.adminu;
                            updateSpiss();
                        }
                        else
                        if (ss.contains("/registerd "))
                        {
                            out.append("logged in as "+ss.substring(11)+"\n");
                            User newme=(User)isob.readObject();
                            my_confs=newme.sostoyu;
                            my_adminki=newme.adminu;
                            updateSpiss();
                        }
                        else
                        if (ss.contains("/confadded "))
                        {
                            Konferen newconf=(Konferen)isob.readObject();
                                    my_confs.add(newconf);
                                    my_adminki.add(newconf);
                                    updateSpiss();
                        }
                        else
                        if (ss.contains("/confjoind "))
                        {
                            Konferen newconf=(Konferen)isob.readObject();
                                    
                            my_confs.add(newconf);
                                    
                                    updateSpiss();
                        }
                        else
                        if (ss.contains("/nickdeled "))
                        {
                           User removing=(User)isob.readObject();
                                    //my_confs.remove(removing);
                                    listen.stop();
                                    close();
                        }
                         else
                        if (ss.contains("/confdeled "))
                        {
                            Konferen removing=(Konferen)isob.readObject();
                                    my_confs.remove(removing);
                                    my_adminki.remove(removing);
                                    updateSpiss();
                        }
                        else{
                        out.append("Нет такой команды"+"\n");
                        }
                    }
                    
                    else{
                        Konferen temp=(Konferen)isob.readObject();
                        int aga=0;
                        for(;aga<my_confs.size()&&!my_confs.get(aga).konfName.equals(temp.konfName);aga++){
                            
                        }
                        if(my_confs.get(aga).konfName.equals(temp.konfName))
                        {my_confs.remove(aga);
                            my_confs.add(aga, temp);}
                        if (aga<my_confs.size())
                        synchronized (my_confs.get(aga).my_textarea) {
                            if(my_confs.get(aga).konfName.equals(temp.konfName)){
                            my_confs.get(aga).my_textarea.append(ss);
                        }
                        }}
                    }}
                } catch (Exception ex) 
                    { out.append("1:"+ex.toString()+"\n"); close(); }
            }
        }
    //---------------------------------------------------------
    Listen listen=null;
    Socket sk=null;
    DataInputStream is=null;
    DataOutputStream os=null;
    ObjectInputStream isob=null;
     ObjectOutputStream osob=null;
    public void connect(){
        try {
            sk=new Socket(Serv.ip,Serv.port);
            is=new DataInputStream(sk.getInputStream());
            os=new DataOutputStream(sk.getOutputStream());
            isob=new ObjectInputStream(sk.getInputStream());
            osob=new ObjectOutputStream(sk.getOutputStream());
            listen=new Listen();
            listen.start();
            os.writeUTF("/loguser "+my_nickname);
            out.append("Порт "+sk.getLocalPort()+", соединение установлено\n");
            setTitle("Клиент: "+my_nickname);
            } catch (Exception ex) 
                { out.append("2:"+ex.toString()+"\n"); close(); }
        }
     public void close(){            // Закрыть соединение
        synchronized (out){
            try {
                if (sk==null) return;
                out.append("Порт "+sk.getLocalPort()+", соединение закрыто\n");
                setTitle("Клиент");
                sk.close();         // Закрыть сокет и потоки
                sk=null;    
                is=null; 
                isob=null;
                osob=null;
                os=null;
                } catch(Exception ee){}
            }
       }


    public Klient(User a){
        my_nickname=a.nickname;
        my_confs=a.sostoyu;
        my_adminki=a.adminu;
        initComponents();
        this.setBounds(600, 100, 600, 350);
        
        this.setTitle("Клиент - "+my_nickname);
        connect();
        
    }
        public Klient(String a){
        my_nickname=a;
        initComponents();
        this.setBounds(600, 100, 600, 350);
        this.setTitle("Клиент - "+my_nickname);
        connect();
    }
        public Klient(){
                    my_nickname="default";
        //my_confs=null;
        //my_adminki=null;
        initComponents();
        this.setBounds(600, 100, 550, 350);
        this.setTitle("Клиент for testing");
        connect();
    }

    public void initComponents() {
        for(int iai=0;iai<my_confs.size();iai++){
            my_confs.get(iai).my_textarea.setBounds(27, 23, 330, 170);
            add(my_confs.get(iai).my_textarea);
            //my_confs.get(iai).my_textarea.append("eto chat"+my_confs.get(iai).konfName+"\n");
        }
        out = new JTextArea();
        in = new java.awt.TextField();
        button1 = new java.awt.Button();
        button2 = new java.awt.Button();
        userSpis=new java.awt.List();
        konfSpis=new java.awt.List();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter(){
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        setLayout(null);
        add(out);
        out.setBounds(27, 23, 330, 170);
        out.setEditable(false);
        add(in);
        in.setBounds(30, 210, 330, 20);
konfSpis.setBounds(360, 23, 100, 170);
userSpis.setBounds(460, 23, 100, 170);
konfSpis.addItemListener(new ItemListener() {
             @Override
        public void itemStateChanged(ItemEvent e) {
           changed();
            }
        }
  );

add(konfSpis);
add(userSpis);
        button1.setLabel("send");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });
        add(button1);
        button1.setBounds(30, 240, 90, 24);

        button2.setLabel("connect");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2ActionPerformed(evt);
            }
        });
        add(button2);
        button2.setBounds(267, 240, 90, 24);

        pack();
    }

public void formWindowClosing(java.awt.event.WindowEvent evt) {
    try {
        listen.stop();

         for(int iai=0;iai<my_confs.size();iai++)
              synchronized (my_confs.get(iai).my_textarea){
            if (sk!=null) {
                os.writeUTF("");
                os.flush();
                }
        }}
    catch (Exception ex) {out.append("3:"+ex.toString()+"\n"); }
        finally { close(); }
}

public void button1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            synchronized (out){
                String ss=in.getText();
                if (ss.length()==0) return;
                if (os==null) return;
                
                os.writeUTF(ss);
                if (ss.charAt(0)!='/'){
                int iai=0;
                for(;iai<my_confs.size()&&!my_confs.get(iai).konfName.equals(konfSpis.getSelectedItem());iai++)
                {
                }
                osob.writeObject(my_confs.get(iai));}
                in.setText("");
                }
        } catch (Exception ex) { out.append(ex.getMessage()+"\n"); close(); }
}
public void button2ActionPerformed(java.awt.event.ActionEvent evt) {
    synchronized (out){
        if (sk!=null) return;
        connect();
    }
}
    public void updateSpiss(){
    userSpis.removeAll();
    konfSpis.removeAll();

    for (int iai=0;iai<my_confs.size();iai++)
    {
    konfSpis.addItem(my_confs.get(iai).konfName);
    }
    {
    String konfa=konfSpis.getSelectedItem();
             for (int iai=0;iai<my_confs.size();iai++)
             {if(my_confs.get(iai).konfName.equals(konfa))
             { 
                 for(int iai1=0;iai1<my_confs.get(iai).my_users.size();iai1++)
                         userSpis.addItem(my_confs.get(iai).my_users.get(iai1).nickname);
             }
             }
    
    }
    //out.updateUI();
    }

              public void changed() {
                  boolean gege=false;
                   aga:  
                   for(int iai=0;iai<my_confs.size();iai++){
             for (Component c : this.getComponents()){
                     if (c==my_confs.get(iai).my_textarea){
                         gege=true;
                         break aga;
           }} 
             if(gege)
                 my_confs.get(iai).my_textarea.append("eto chat"+my_confs.get(iai).konfName+"\n"); 
           if(!gege){
                       my_confs.get(iai).my_textarea.setBounds(27, 23, 330, 170);
            add(my_confs.get(iai).my_textarea);
           }  }
             String konfa=konfSpis.getSelectedItem();
       out.setVisible(false);        
             for (int iai=0;iai<my_confs.size();iai++)
             {
                 if((my_confs.get(iai).konfName).equals(konfa)){
                 userSpis.removeAll();
             my_confs.get(iai).my_textarea.setVisible(false);
                 for(int iai1=0;iai1< my_confs.get(iai).my_users.size();iai1++)
                     userSpis.addItem(my_confs.get(iai).my_users.get(iai1).nickname);
                 my_confs.get(iai).my_textarea.setVisible(true);
             }
             else{
                 my_confs.get(iai).my_textarea.setVisible(false);
                 }}}
        
    public java.awt.Button button1;
    public java.awt.Button button2;
    public java.awt.TextField in;
    public JTextArea out;
    public java.awt.List konfSpis;
    public java.awt.List userSpis;
}