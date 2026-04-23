package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Home extends JFrame implements ActionListener {

    public Home() {

        setLayout(null);

        // 🔥 SCREEN SIZE
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // 🔥 BACKGROUND IMAGE
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("airlinemanagementsystem/icons/front_1.jpg"));
        Image i2 = i1.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);

        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, width, height);
        image.setLayout(null);
        add(image);

        // 🔥 TOP TOOLBAR (CENTERED)
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 10)); // ✅ CENTER FIX
        toolbar.setBackground(new Color(0, 0, 0, 120));
        toolbar.setBounds(0, 0, width, 60);
        image.add(toolbar);

        Font btnFont = new Font("Tahoma", Font.BOLD, 14);

        // 🔘 BUTTONS
        toolbar.add(createButton("Flight Details", btnFont));
        toolbar.add(createButton("Add Customer", btnFont));
        toolbar.add(createButton("Book Flight", btnFont));
        toolbar.add(createButton("Journey Details", btnFont));
        toolbar.add(createButton("View Bookings", btnFont));
        toolbar.add(createButton("Boarding Pass", btnFont));

        // 🔥 HEADING
        JLabel heading = new JLabel("AIR INDIA WELCOMES YOU");
        heading.setBounds(width/4, 100, 800, 50);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Tahoma", Font.BOLD, 40));
        image.add(heading);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    // 🔧 BUTTON CREATOR
    private JButton createButton(String text, Font font) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 🔥 hover cursor
        btn.addActionListener(this);
        return btn;
    }

    // 🔥 ACTIONS
    public void actionPerformed(ActionEvent ae) {

        String text = ae.getActionCommand();

        if (text.equals("Add Customer")) {
            new AddCustomer();
        } 
        else if (text.equals("Flight Details")) {
            new FlightInfo();
        } 
        else if (text.equals("Book Flight")) {
            new BookFlight();
        } 
        else if (text.equals("Journey Details")) {
            new JourneyDetails();
        } 
        else if (text.equals("View Bookings")) {
            new ViewBookings();
        } 
        else if (text.equals("Boarding Pass")) {
            new BoardingPass();
        }
    }

    public static void main(String[] args) {
        new Home();
    }
}