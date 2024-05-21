package koors;
import java.awt.*;
import javax.swing.*;

public class FirstFrame extends javax.swing.JFrame{
    JLabel name=new JLabel("VHOD:");
   
    JTextField login=new JTextField();
    JButton enter=new JButton("enter");
    JPanel logPan=new JPanel();
    
    FirstFrame(){
    initComponents();
    this.setBounds(500, 150, 200, 200);
        this.setTitle("логин");
    }
private void initComponents(){
    login.setSize(100, 100);
    name.setSize(100, 100);
    logPan.setLayout(new GridBagLayout());
     GridBagConstraints wtf=new GridBagConstraints();
         wtf.gridheight=3;
          wtf.ipadx=50;
          wtf.gridwidth=0;
          wtf.anchor=GridBagConstraints.CENTER;
         wtf.weightx=30;
         wtf.ipady=10;
    enter.addActionListener(new java.awt.event.ActionListener(){
      @Override
            public void actionPerformed(java.awt.event.ActionEvent evt){
                String nikname=login.getText();
                if (nikname.equals("serv")){
                    new Serv().setVisible(true);
                dispose();}
                else
                if (!nikname.equals("exit")){
                   new Klient(nikname).setVisible(true);
                dispose();}
                else
                System.exit(0);
            }
        });
setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
logPan.add(name,wtf);
logPan.add(login,wtf);
logPan.add(enter,wtf);
add(logPan);
}
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Klient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new FirstFrame().setVisible(true);
            }
        });
    }
}