package airlinemanagementsystem;

import java.sql.*;

public class BookingService {

    public static boolean bookSeat(String flightCode, String seatNo) {

        try {
            Conn conn = new Conn();

            // 🔥 START TRANSACTION
            conn.c.setAutoCommit(false);

            // 🔥 LOCK THE SEAT ROW
            String query = "SELECT * FROM seats WHERE flight_code=? AND seat_no=? FOR UPDATE";
            PreparedStatement pst = conn.c.prepareStatement(query);
            pst.setString(1, flightCode);
            pst.setString(2, seatNo);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                String status = rs.getString("status");

                if (status.equals("available")) {

                    // 🔥 UPDATE SEAT → BOOKED
                    String update = "UPDATE seats SET status='booked' WHERE flight_code=? AND seat_no=?";
                    PreparedStatement pst2 = conn.c.prepareStatement(update);
                    pst2.setString(1, flightCode);
                    pst2.setString(2, seatNo);

                    pst2.executeUpdate();

                    conn.c.commit(); // ✅ SUCCESS
                    return true;

                } else {
                    conn.c.rollback(); // ❌ already booked
                    return false;
                }
            }

            conn.c.rollback();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}