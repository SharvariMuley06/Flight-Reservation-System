package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewBookings extends JFrame {

    JTable table;
    JButton cancelBtn;

    public ViewBookings() {

        setLayout(new BorderLayout());

        JLabel heading = new JLabel("BOOKED FLIGHTS");
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(new Font("Tahoma", Font.BOLD, 20));
        add(heading, BorderLayout.NORTH);

        table = new JTable();

        // 🔥 Load bookings
        loadTable();

        JScrollPane jsp = new JScrollPane(table);
        add(jsp, BorderLayout.CENTER);

        cancelBtn = new JButton("Cancel Booking");
        add(cancelBtn, BorderLayout.SOUTH);

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelTicket();
            }
        });

        setSize(900, 500);
        setLocation(300, 100);
        setVisible(true);
    }

    // ================= LOAD TABLE =================
    public void loadTable() {
        try {
            Conn conn = new Conn();

            String query = "SELECT * FROM reservation";
            ResultSet rs = conn.s.executeQuery(query);

            table.setModel(net.proteanit.sql.DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CANCEL BOOKING =================
    public void cancelTicket() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row first");
            return;
        }

        try {
            // 🔥 GET DATA FROM TABLE
            String pnr = table.getValueAt(selectedRow, 0).toString();
            String flightCode = table.getValueAt(selectedRow, 6).toString(); // flight_code
            String seat = table.getValueAt(selectedRow, 10).toString(); // seat column

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Cancel ticket with PNR: " + pnr + " ?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                Conn conn = new Conn();

                // 🔥 1. DELETE FROM RESERVATION
                PreparedStatement pst = conn.c.prepareStatement(
                        "DELETE FROM reservation WHERE pnr = ?");
                pst.setString(1, pnr);
                pst.executeUpdate();

                // 🔥 2. MAKE SEAT AVAILABLE AGAIN
                PreparedStatement pst2 = conn.c.prepareStatement(
                        "UPDATE seats SET status='available' WHERE flight_code=? AND seat_no=?");
                pst2.setString(1, flightCode);
                pst2.setString(2, seat);
                pst2.executeUpdate();

                JOptionPane.showMessageDialog(this, "Ticket Cancelled & Seat Released");

                // 🔄 REFRESH TABLE
                loadTable();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        new ViewBookings();
    }
}