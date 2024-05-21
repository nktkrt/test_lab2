package koors;
import java.util.*;
import java.io.*;
public class User implements Serializable{
    String nickname;
   ArrayList<Konferen> adminu=new ArrayList<Konferen>();
    ArrayList<Konferen> sostoyu=new ArrayList<Konferen>();
        User(String a){
    nickname=a;
   }

}
