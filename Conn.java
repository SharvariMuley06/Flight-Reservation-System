package airlinemanagementsystem;

import java.sql.*;

public class Conn {

    Connection c;
    Statement s;

    public Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // ✅ CONNECT
            c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/airline?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                "Sharvari@18006"
            );

            s = c.createStatement();

            // 🔥 FORCE DATABASE (important safety)
            s.execute("USE airline");

            // 🔥 DEBUG 1: Which DB?
            ResultSet rs1 = s.executeQuery("SELECT DATABASE()");
            if (rs1.next()) {
                System.out.println("Connected DB (Java): " + rs1.getString(1));
            }

            // 🔥 DEBUG 2: Count rows
            ResultSet rs2 = s.executeQuery("SELECT COUNT(*) FROM customer");
            if (rs2.next()) {
                System.out.println("Rows in customer table (Java): " + rs2.getInt(1));
            }

            // 🔥 DEBUG 3: Print actual data
            ResultSet rs3 = s.executeQuery("SELECT * FROM customer");
            System.out.println("===== JAVA DB DATA =====");
            while (rs3.next()) {
                System.out.println(
                    rs3.getString("name") + " | " +
                    rs3.getString("aadhar")
                );
            }

            // 🔥 DEBUG 4: Data directory (VERY IMPORTANT)
            ResultSet rs4 = s.executeQuery("SHOW VARIABLES LIKE 'datadir'");
            while (rs4.next()) {
                System.out.println("JAVA DATADIR: " + rs4.getString(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}