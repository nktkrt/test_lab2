package koors;
import java.awt.Component;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;

public class Serv extends javax.swing.JFrame {
    final static int port=6100;
    final static String ip="127.0.0.1";
    

    ArrayList<User> userList=new ArrayList<User>();
    ArrayList<Konferen> confList=new ArrayList<Konferen>();

    class Connect extends Thread{       // Класс - поток, управляющий соединением
        Socket sk=null;                 // Сокет соединения
        DataInputStream is=null;        // Двоичные потоки данных на сокете
        DataOutputStream os=null;
        ObjectOutputStream osob=null;
        ObjectInputStream isob=null;
        String nick=null;
        Connect(Socket sk0){
            try {                       // Запомнить полученный сокет
                sk=sk0;                 // и открыть на нем потоки даннных
                is=new DataInputStream(sk.getInputStream());
                os=new DataOutputStream(sk.getOutputStream());
                osob=new ObjectOutputStream(sk.getOutputStream());
                isob=new ObjectInputStream(sk.getInputStream());
                synchronized (list){    // Добавить СЕБЯ в вектор
                    list.add(this);
                    }
               
                start();                // Стартануть собственный поток - run
                out.append("Порт "+sk.getPort()+", соединение установлено\n");
                
                } catch (Exception ex) { out.append("1:"+ex.toString()+"\n"); }
            }
        public void close(){            // Закрыть соединение
            synchronized (list){
                try {
                    list.remove(this);  // Удалить себя из вектора
                    out.append("Порт "+sk.getPort()+", соединение закрыто\n");
                    for(int iai=0;iai<confList.size();iai++)
                    {confList.get(iai).my_textarea.append("Порт "+sk.getPort()+", соединение закрыто\n");
                    }
                    sk.close();         // Закрыть сокет и потоки данных
                    sk=null;
                    is=null;
                    os=null;
                    osob=null;
                    isob=null;
                    nick=null;
                    } catch(Exception ee){}
                }
            }
        @Override
        public void run(){ // Функционал потока - прием и рассылка сообщений
            try {
                while(true){
                    leaveif:{
                    String ss=this.is.readUTF(); // Читать строку из входного потока данных
                    if (ss.length()==0){    // Пустая строка - сообщение о разрыве соединения
                        close();
                        break;
                        }
                    else
                    if (ss.charAt(0)=='/')
                    {
                        if (ss.contains("/loguser "))
                        {
                            for(int iai=0;iai<userList.size();iai++)
                            {if (ss.substring(9).equals(userList.get(iai).nickname))
                                {this.os.writeUTF("/nicktaken "+userList.get(iai).nickname);
                               this.nick=userList.get(iai).nickname;
                               osob.writeObject(userList.get(iai));
                               updateSpiss();          
                                break leaveif;
                                }
                            }
                            User tmp=new User(ss.substring(9,ss.length()));
                            userList.add(tmp);
                                this.os.writeUTF("/registerd "+tmp.nickname);
                                this.nick=tmp.nickname;
                                userSpis.addItem(tmp.nickname);
                                osob.writeObject(tmp);
                                updateSpiss();          
                        }
                        else
                        if (ss.contains("/addconf "))
                        {
                            for(int iai=0;iai<confList.size();iai++)
                            {
                                if (ss.substring(9).equals(confList.get(iai).konfName))
                                    {
                                        this.os.writeUTF("Сервер: Название конференции занято");
                                        break leaveif;
                                    }
                            }
                                    this.os.writeUTF("/confadded "+ss.substring(9));
                                    Konferen tmp;
                                    for(int iai=0;iai<userList.size();iai++){
                                        if(userList.get(iai).nickname.equals(nick)){
                                           tmp= new Konferen(userList.get(iai),ss.substring(9));
                                           
                                    confList.add(tmp);
                                userList.get(iai).sostoyu.add(tmp);
                                userList.get(iai).adminu.add(tmp);
                                    osob.writeObject(tmp);}}
                                    konfSpis.addItem(ss.substring(9));
                          updateSpiss();
                        }
                        else
                        if (ss.contains("/joincon "))
                        {
                            Konferen tmp;
                            for(int iai=0;iai<confList.size();iai++)
                            {if (ss.substring(9).equals(confList.get(iai).konfName))
                                {this.os.writeUTF("/confjoind "+confList.get(iai).konfName);
                                
                                for(int iai1=0;iai1<userList.size();iai1++){
                                    if(this.nick.equals(userList.get(iai1).nickname)){
                                        
                                confList.get(iai).my_users.add(userList.get(iai1));
                                userList.get(iai1).sostoyu.add(confList.get(iai));
                                osob.writeObject(confList.get(iai));}
                                }
                                updateSpiss();          
                                break leaveif;
                                }
                            }
                        this.os.writeUTF("Сервер: Нет такой конференции");
                        }
                        else
                        if (ss.contains("/deluser "))
                        {
                            if (ss.substring(9).equals(this.nick))
                                    {
                            for(int iai=0;iai<userList.size();iai++)
                            {if (ss.substring(9).equals(userList.get(iai).nickname))
                                {
                                    
                                     for(int iai1=0;iai1<confList.size();iai1++)
                                {confList.get(iai1).my_users.remove(userList.get(iai));
                                

                                }
                                    this.os.writeUTF("/nickdeled "+userList.get(iai).nickname);
                                osob.writeObject(userList.get(iai));
                                userList.remove(iai);
                                updateSpiss();    
                                break leaveif;
                                }
                             
                                    }
                            this.os.writeUTF("Сервер: Нет такого пользователя");
                            }
                            else {
                            this.os.writeUTF("Сервер: Нет прав на удаление");
                            }
                             
                        }
                        else
                        if (ss.contains("/delconf "))
                        {
                            
                            for(int iai=0;iai<confList.size();iai++)
                            {if (ss.substring(9).equals(confList.get(iai).konfName))
                                if(this.nick.equals(confList.get(iai).adminit.nickname))
                                {
                                this.os.writeUTF("/confdeled "+confList.get(iai).konfName);
                                osob.writeObject(confList.get(iai));
                                for(int iai1=0;iai<userList.size();iai1++)
                                {userList.get(iai1).adminu.remove(confList.get(iai));
                                userList.get(iai1).sostoyu.remove(confList.get(iai));
                                }
                                
                                confList.remove(iai);
                                updateSpiss();
                                break leaveif;
                                }
                                else {
                            this.os.writeUTF("Сервер: Нет прав на удаление");
                                }
                                
                            else
                              this.os.writeUTF("Сервер: Нет такой конференции");
                            break leaveif;
                            }
                            }
                            
                        else{
                        this.os.writeUTF("Сервер: Нет такой команды");
                        }
                    }
                    else
                    {
                        Konferen kudaPisat=(Konferen)isob.readObject();
                    int aga=0;
                        for(;aga<confList.size()&&!confList.get(aga).konfName.equals(kudaPisat.konfName);aga++){
                       
                            }
                   confList.get(aga).my_textarea.append(nick+": "+ss+"\n");
                    synchronized (list){    // Для всех объектов - соединений
                        for (int i=0;i<list.size();i++){
                            
                            Connect cc=list.get(i);
                            
                            cc.os.writeUTF(this.nick+": "+ss+"\n");
                            cc.osob.writeObject(confList.get(aga));
                            }              // Записать строку в поток соединения
                        }
                    }
                    }
                }
                } catch (Exception ex) {
                    out.append("2:"+ex.toString()+"\n"); 
                    close();
                    }
            }
        }
    Vector<Connect> list=new Vector();      // Вектор объектов - соединений
    ServerSocket srv=null;                  // Объект для ожидания соединений
    Thread listen=new Thread(){             // Поток для ожидания соедиенений
        @Override
        public void run(){
            try {
                srv=new ServerSocket(Serv.port);        
                while(true){          // Цикл ожидания
                    Socket ss=srv.accept();// Получить очередное соединение
                    new Connect(ss);       // Создать объект-соединение
                    }                      // и передать ему Socket
                } catch (Exception ex) { out.append("3:"+ex.toString()+"\n"); }
            }};
    public Serv() {
/*User u1=new User("Krutoi chel");
User u2=new User("Hunter");
User u3=new User("Larva");
User u4=new User("bog etogo mira");
Konferen k1=new Konferen(u1,"Konfa dla krutih");
Konferen k2=new Konferen(u2,"EDGelords");
Konferen k3=new Konferen(u3,"JUKI");
k2.my_users.add(u1);
k3.my_users.add(u2);
k3.my_users.add(u4);
u1.sostoyu.add(k2);
u2.sostoyu.add(k3);
u4.sostoyu.add(k3);
userList.add(u1);
userList.add(u2);
userList.add(u3);
userList.add(u4);
confList.add(k1);
confList.add(k2);
confList.add(k3);
saveAll();*/
//loadAll();
        initComponents();
        
        this.setBounds(200, 100, 700, 300);
        this.setTitle("Сервер");
        listen.start();
    }

     
    public void loadAll(){
      try {
             FileInputStream fisu = new FileInputStream("users.tmp");
      ObjectInputStream oisu = new ObjectInputStream(fisu);
             FileInputStream fisc = new FileInputStream("confs.tmp");
      ObjectInputStream oisc = new ObjectInputStream(fisc);
      int kolvousers=oisu.readInt();
      for(int iai=0;iai<kolvousers;iai++){
      User us = (User) oisu.readObject();
      userList.add(us);
      }
      int kolvoconfs=oisc.readInt();
      for(int iai=0;iai<kolvoconfs;iai++){
      Konferen con = (Konferen) oisc.readObject();
      confList.add(con);
      }
      oisu.close();
      oisc.close();
        }
        catch (Exception e)
        {e.printStackTrace();}
    }
    public void updateSpiss(){
    userSpis.removeAll();
    konfSpis.removeAll();

    for (int iai=0;iai<confList.size();iai++)
    {
    konfSpis.addItem(confList.get(iai).konfName);
    }
    for (int iai=0;iai<userList.size();iai++)
    userSpis.addItem(userList.get(iai).nickname);
    }
    
    public void saveAll(){
        
        try {
             FileOutputStream fisu = new FileOutputStream("users.tmp");
      ObjectOutputStream oisu = new ObjectOutputStream(fisu);
             FileOutputStream fisc = new FileOutputStream("confs.tmp");
      ObjectOutputStream oisc = new ObjectOutputStream(fisc);
      oisu.writeInt(userList.size());
      for(int iai=0;iai<userList.size();iai++){
      oisu.writeObject(userList.get(iai));
      }
     oisc.writeInt(confList.size());
      for(int iai=0;iai<confList.size();iai++){
      oisc.writeObject(confList.get(iai));
      }
      oisu.close();
      oisc.close();
        }
        catch (Exception e)
        {e.printStackTrace();}
    }
    @SuppressWarnings("unchecked")
    public void initComponents() {
        for(int iai=0;iai<confList.size();iai++){
            confList.get(iai).my_textarea.setBounds(23, 21, 310, 210);
            getContentPane().add(confList.get(iai).my_textarea);
            confList.get(iai).my_textarea.append("eto chat"+confList.get(iai).konfName+"\n");
            jsp = new JScrollPane(confList.get(iai).my_textarea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(jsp);
        }
        
        out = new JTextArea();
        jsp = new JScrollPane(out,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(jsp);
        konfSpis=new java.awt.List();
        userSpis=new java.awt.List();
        Users=new JLabel("Пользователи:");
        Conferences=new JLabel("Конференции:");
        Chat=new JLabel("Чат:");
        for(int iai=0;iai<userList.size();iai++)
        {
        userSpis.addItem(userList.get(iai).nickname);
        }
        for(int iai=0;iai<confList.size();iai++)
        {
        konfSpis.addItem(confList.get(iai).konfName);
        }
        konfSpis.setBounds(330, 21, 150, 210);
        userSpis.setBounds(480, 21, 150, 210);
        Users.setBounds(480, 0, 150, 21);
        Conferences.setBounds(330, 0, 150, 21);
        Chat.setBounds(23, 0, 310, 21);
        konfSpis.addItemListener(new ItemListener() {
             @Override
        public void itemStateChanged(ItemEvent e) {
           changed();
            }
        }
  );
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);
        out.setEditable(false);
        
        getContentPane().add(out);
        getContentPane().add(konfSpis);
        getContentPane().add(userSpis);
        getContentPane().add(Users);
        getContentPane().add(Conferences);
        getContentPane().add(Chat);
        out.setBounds(23, 21, 310, 210);
out.append("zdes vivoditsa ves chat"+"\n");
        pack();
    }
    public void changed()
    {
                   
                   for(int iai=0;iai<confList.size();iai++){
                           boolean gege=false; 
             for (Component c : this.getComponents()){
                     if (c==confList.get(iai).my_textarea){
                         gege=true;
                         break;
           }
            }
             if(gege)
                
             
           if(!gege){
                       confList.get(iai).my_textarea.setBounds(23, 21, 310, 210);
            add(confList.get(iai).my_textarea);
 confList.get(iai).my_textarea.append("eto chat"+confList.get(iai).konfName+"\n"); 
           }
                   
                   }
       String konfa=konfSpis.getSelectedItem();
             int kk=0;
             for (;kk<confList.size()&&!(confList.get(kk).konfName).equals(konfa);kk++)
             {
                  out.setVisible(false);
                  confList.get(kk).my_textarea.setVisible(false);
             }
             confList.get(kk).my_textarea.setVisible(true);
           
    }
public void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
     synchronized (list){       // Для всех объектов - соединений
        while(list.size()!=0){
            Connect cc=list.get(0);
            try {               
                cc.os.writeUTF(""); 
                cc.os.flush();
                }  catch(Exception ee){
            cc.stop();          // Остановить поток
            cc.close();         // Закрыть в объекте сокет и потоки данных
            }                   // и удалить из вектора
        }
     listen.stop();
     }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
public JScrollPane jsp;
    public JTextArea out;
    public JLabel Users;
    public JLabel Chat;
    public JLabel Conferences;
    public java.awt.List konfSpis;
    public java.awt.List userSpis;
    public JTextArea commands;
    // End of variables declaration//GEN-END:variables
}
