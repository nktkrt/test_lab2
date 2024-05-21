package koors;
import java.io.*;
import java.util.*;
import javax.swing.JTextArea;

public class Konferen implements Serializable{
   public ArrayList<User> my_users=new ArrayList<User>();
    User adminit;
    String konfName;
    JTextArea my_textarea;
    Konferen(User a,String newName){
        my_textarea=new JTextArea();
        my_textarea.setBounds(27, 23, 330, 170);
        my_textarea.setEditable(false);
        my_textarea.setVisible(false);
       adminit=a;
       my_users.add(a);
       konfName=newName;
       a.sostoyu.add(this);
       a.adminu.add(this);
    }
    Konferen(String newName)
    {konfName=newName;
            my_textarea=new JTextArea();
        my_textarea.setBounds(27, 23, 330, 170);
        my_textarea.setEditable(false);
        my_textarea.setVisible(false);} 

}
