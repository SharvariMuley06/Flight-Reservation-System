package airlinemanagementsystem;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Login extends JFrame implements ActionListener {

    JButton submit, reset, close;
    JTextField tfusername;
    JPasswordField tfpassword;

    // 🔥 GLOBAL USER (VERY IMPORTANT)
    public static String currentUser = "";

    public Login() {

        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel lblusername = new JLabel("Username");
        lblusername.setBounds(20, 20, 100, 20);
        add(lblusername);

        tfusername = new JTextField();
        tfusername.setBounds(130, 20, 200, 20);
        add(tfusername);

        JLabel lblpassword = new JLabel("Password");
        lblpassword.setBounds(20, 60, 100, 20);
        add(lblpassword);

        tfpassword = new JPasswordField();
        tfpassword.setBounds(130, 60, 200, 20);
        add(tfpassword);

        reset = new JButton("Reset");
        reset.setBounds(40, 120, 120, 25);
        reset.addActionListener(this);
        add(reset);

        submit = new JButton("Submit");
        submit.setBounds(190, 120, 120, 25);
        submit.addActionListener(this);
        add(submit);

        close = new JButton("Close");
        close.setBounds(120, 160, 120, 25);
        close.addActionListener(this);
        add(close);

        setSize(400, 250);
        setLocation(600, 250);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == submit) {

            String username = tfusername.getText().trim();
            String password = tfpassword.getText().trim();

            if (username.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter username and password");
                return;
            }

            try {
                Conn c = new Conn();

                // 🔥 SAFE QUERY (PreparedStatement)
                String query = "SELECT * FROM login WHERE username = ? AND password = ?";
                PreparedStatement pst = c.c.prepareStatement(query);
                pst.setString(1, username);
                pst.setString(2, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {

                    // 🔥 SAVE LOGGED USER
                    currentUser = username;

                    JOptionPane.showMessageDialog(null, "Login Successful");

                    new Home();
                    setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (ae.getSource() == close) {
            System.exit(0);

        } else if (ae.getSource() == reset) {
            tfusername.setText("");
            tfpassword.setText("");
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}