package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import com.toedter.calendar.JDateChooser;

public class BookFlight extends JFrame implements ActionListener {

    JTextField tfaadhar, tfname, tfnationality, tfaddress;
    JLabel labelgender, labelfname, labelfcode, labelDeptTime, labelArrTime;
    JComboBox<String> source, destination, seatBox;
    JButton fetchButton, flightButton, bookflight;
    JDateChooser dcdate;

    public BookFlight() {

        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JLabel heading = new JLabel("BOOK FLIGHT");
        heading.setBounds(420, 20, 500, 35);
        heading.setFont(new Font("Tahoma", Font.PLAIN, 32));
        heading.setForeground(Color.BLUE);
        add(heading);

        addLabel("Aadhar", 60, 80);
        tfaadhar = addTextField(220, 80);

        fetchButton = new JButton("Fetch User");
        fetchButton.setBounds(380, 80, 120, 25);
        fetchButton.addActionListener(this);
        add(fetchButton);

        addLabel("Name", 60, 130);
        tfname = addTextField(220, 130);

        addLabel("Nationality", 60, 180);
        tfnationality = addTextField(220, 180);

        addLabel("Address", 60, 230);
        tfaddress = addTextField(220, 230);

        addLabel("Gender", 60, 280);
        labelgender = new JLabel();
        labelgender.setBounds(220, 280, 150, 25);
        add(labelgender);

        addLabel("Source", 60, 330);
        source = new JComboBox<>(new String[]{"Delhi", "Mumbai", "Bangalore", "Chennai"});
        source.setBounds(220, 330, 150, 25);
        add(source);

        addLabel("Destination", 60, 380);
        destination = new JComboBox<>(new String[]{"Delhi", "Mumbai", "Bangalore", "Chennai"});
        destination.setBounds(220, 380, 150, 25);
        add(destination);

        flightButton = new JButton("Fetch Flights");
        flightButton.setBounds(380, 380, 120, 25);
        flightButton.addActionListener(this);
        add(flightButton);

        addLabel("Flight Name", 60, 430);
        labelfname = new JLabel();
        labelfname.setBounds(220, 430, 150, 25);
        add(labelfname);

        addLabel("Flight Code", 60, 480);
        labelfcode = new JLabel();
        labelfcode.setBounds(220, 480, 150, 25);
        add(labelfcode);

        // ✅ CLEAN SPACING (NO OVERLAP)

        addLabel("Departure Time", 60, 520);
        labelDeptTime = new JLabel();
        labelDeptTime.setBounds(220, 520, 150, 25);
        add(labelDeptTime);

        addLabel("Seat", 60, 560);
        seatBox = new JComboBox<>();
        seatBox.setBounds(220, 560, 150, 25);
        add(seatBox);

        addLabel("Arrival Time", 60, 600);
        labelArrTime = new JLabel();
        labelArrTime.setBounds(220, 600, 150, 25);
        add(labelArrTime);

        addLabel("Date", 60, 640);
        dcdate = new JDateChooser();
        dcdate.setBounds(220, 640, 150, 25);
        add(dcdate);

        bookflight = new JButton("Book Flight");
        bookflight.setBounds(220, 690, 150, 30);
        bookflight.addActionListener(this);
        add(bookflight);

        setSize(900, 800);
        setLocation(300, 50);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {

        // FETCH USER
        if (ae.getSource() == fetchButton) {
            try {
                Conn conn = new Conn();
                PreparedStatement pst = conn.c.prepareStatement("SELECT * FROM customer WHERE aadhar=?");
                pst.setString(1, tfaadhar.getText());

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    tfname.setText(rs.getString("name"));
                    tfnationality.setText(rs.getString("nationality"));
                    tfaddress.setText(rs.getString("address"));
                    labelgender.setText(rs.getString("gender"));
                } else {
                    JOptionPane.showMessageDialog(null, "No user found");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // FETCH FLIGHT + SEATS
        else if (ae.getSource() == flightButton) {
            try {
                Conn conn = new Conn();

                PreparedStatement pst = conn.c.prepareStatement(
                        "SELECT * FROM flight WHERE source=? AND destination=?");
                pst.setString(1, (String) source.getSelectedItem());
                pst.setString(2, (String) destination.getSelectedItem());

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String code = rs.getString("f_code");

                    labelfname.setText(rs.getString("f_name"));
                    labelfcode.setText(code);

                    labelDeptTime.setText(rs.getString("departure_time"));
                    labelArrTime.setText(rs.getString("arrival_time"));

                    seatBox.removeAllItems();

                    PreparedStatement pst2 = conn.c.prepareStatement(
                            "SELECT seat_no FROM seats WHERE flight_code=? AND status='available'");
                    pst2.setString(1, code);

                    ResultSet rs2 = pst2.executeQuery();

                    boolean hasSeat = false;

                    while (rs2.next()) {
                        seatBox.addItem(rs2.getString("seat_no"));
                        hasSeat = true;
                    }

                    if (!hasSeat) {
                        JOptionPane.showMessageDialog(null, "No seats available");
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "No Flights Found");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // BOOK FLIGHT
        else if (ae.getSource() == bookflight) {

            try {
                String flightcode = labelfcode.getText();
                String seat = (String) seatBox.getSelectedItem();

                if (seat == null) {
                    JOptionPane.showMessageDialog(null, "Please select seat");
                    return;
                }

                boolean booked = BookingService.bookSeat(flightcode, seat);

                if (!booked) {
                    JOptionPane.showMessageDialog(null, "Seat already booked!");
                    return;
                }

                Conn conn = new Conn();

                PreparedStatement pst = conn.c.prepareStatement(
                        "INSERT INTO reservation VALUES(?,?,?,?,?,?,?,?,?,?,?)");

                pst.setString(1, "PNR-" + new Random().nextInt(100000));
                pst.setString(2, "TIC-" + new Random().nextInt(10000));
                pst.setString(3, tfaadhar.getText());
                pst.setString(4, tfname.getText());
                pst.setString(5, tfnationality.getText());
                pst.setString(6, labelfname.getText());
                pst.setString(7, flightcode);
                pst.setString(8, (String) source.getSelectedItem());
                pst.setString(9, (String) destination.getSelectedItem());
                pst.setString(10, ((JTextField) dcdate.getDateEditor().getUiComponent()).getText());
                pst.setString(11, seat);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Flight Booked! Seat: " + seat);
                setVisible(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private JLabel addLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 150, 25);
        add(l);
        return l;
    }

    private JTextField addTextField(int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 150, 25);
        add(tf);
        return tf;
    }

    public static void main(String[] args) {
        new BookFlight();
    }
}