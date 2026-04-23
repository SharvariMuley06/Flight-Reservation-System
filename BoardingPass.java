package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BoardingPass extends JFrame implements ActionListener {

    JTextField tfpnr;
    JLabel tfname, tfnationality, lblsrc, lbldest, labelfname, labelfcode, labeldate, labelpnr, labelseat;
    JButton fetchButton;

    public BoardingPass() {

        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("AIR INDIA");
        heading.setBounds(380, 10, 450, 35);
        heading.setFont(new Font("Tahoma", Font.BOLD, 32));
        add(heading);

        JLabel subheading = new JLabel("Boarding Pass");
        subheading.setBounds(360, 50, 300, 30);
        subheading.setFont(new Font("Tahoma", Font.PLAIN, 24));
        subheading.setForeground(Color.BLUE);
        add(subheading);

        JLabel lblpnrInput = new JLabel("Enter PNR");
        lblpnrInput.setBounds(60, 100, 150, 25);
        add(lblpnrInput);

        tfpnr = new JTextField();
        tfpnr.setBounds(220, 100, 150, 25);
        add(tfpnr);

        fetchButton = new JButton("Fetch");
        fetchButton.setBounds(380, 100, 120, 25);
        fetchButton.addActionListener(this);
        add(fetchButton);

        // Labels
        addLabel("Name", 60, 150);
        tfname = createValueLabel(220, 150);

        addLabel("Nationality", 60, 190);
        tfnationality = createValueLabel(220, 190);

        addLabel("From", 60, 230);
        lblsrc = createValueLabel(220, 230);

        addLabel("To", 380, 230);
        lbldest = createValueLabel(540, 230);

        addLabel("Flight Name", 60, 270);
        labelfname = createValueLabel(220, 270);

        addLabel("Flight Code", 380, 270);
        labelfcode = createValueLabel(540, 270);

        addLabel("Date", 60, 310);
        labeldate = createValueLabel(220, 310);

        addLabel("PNR", 380, 310);
        labelpnr = createValueLabel(540, 310);

        addLabel("Seat", 60, 350);
        labelseat = createValueLabel(220, 350);

        // 🔥 AIR INDIA LOGO (RIGHT SIDE)
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/airindia.png"));
        Image i2 = i1.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);

        JLabel logo = new JLabel(i3);
        logo.setBounds(720, 140, 220, 220); // adjust if needed
        add(logo);

        // Frame
        setSize(1000, 500);
        setLocation(300, 150);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        String pnr = tfpnr.getText().trim();

        if (pnr.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter PNR");
            return;
        }

        try {
            Conn conn = new Conn();

            String query = "SELECT * FROM reservation WHERE pnr = ?";
            PreparedStatement pst = conn.c.prepareStatement(query);
            pst.setString(1, pnr);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                tfname.setText(rs.getString("name"));
                tfnationality.setText(rs.getString("nationality"));
                lblsrc.setText(rs.getString("src"));
                lbldest.setText(rs.getString("des"));
                labelfname.setText(rs.getString("flight_name"));
                labelfcode.setText(rs.getString("flight_code"));
                labeldate.setText(rs.getString("ddate"));
                labelpnr.setText(rs.getString("pnr"));
                labelseat.setText(rs.getString("seat"));

            } else {
                JOptionPane.showMessageDialog(this, "Invalid PNR");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔧 Helper Methods
    private void addLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 150, 25);
        add(label);
    }

    private JLabel createValueLabel(int x, int y) {
        JLabel label = new JLabel();
        label.setBounds(x, y, 200, 25);
        add(label);
        return label;
    }

    public static void main(String[] args) {
        new BoardingPass();
    }
}