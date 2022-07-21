package bankoffers;

import java.sql.*;

public class DBReadWrite {

    // Variables
    private Connection connection;
    private PreparedStatement newOfferStmt;
    private PreparedStatement searchByVendor;

    // Constructor
    public DBReadWrite() throws SQLException {
        System.out.println("-------- MySQL JDBC Connection Test ------------");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found !!");
            return;
        }
        System.out.println("MySQL JDBC Driver Registered!");
        connection = null;
        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/creditcardoffers", "CreditCardOffersUser",
                            "CreditCardOffersPassword");
            System.out.println("SQL Connection to database established!");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            return;
        }

        String newOfferString = "INSERT INTO offers (Bank_Name, Use_Type, Vendor, Percent, Amount, Min_Spend, Max_Return, Expiration_Date) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        newOfferStmt = connection.prepareStatement(newOfferString, Statement.RETURN_GENERATED_KEYS);
        String searchByVendorString = "SELECT * FROM offers WHERE Vendor = ?";
        searchByVendor = connection.prepareStatement(searchByVendorString, Statement.RETURN_GENERATED_KEYS);
    }

    public boolean close() {
        // try {
        // rs.close();
        // stmt.close();
        // pstmt.close();
        // } catch (SQLException e) {
        // System.out.println(e.getMessage());
        // }
        // close connection
        try {
            if (connection != null)
                connection.close();
            System.out.println("Connection closed !!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; // Does this make sense to do? This will never return false, but will throw
                     // errors instead
    }

    public int write(Offer newOffer) throws SQLException { // Returns offer_id of new item written
        newOfferStmt.setString(1, newOffer.getCard());
        newOfferStmt.setString(2, "Single");
        newOfferStmt.setString(3, newOffer.getVendor());
        newOfferStmt.setDouble(4, newOffer.getPercent());
        newOfferStmt.setDouble(5, newOffer.getAmount());
        newOfferStmt.setDouble(6, newOffer.getMinimum());
        newOfferStmt.setDouble(7, newOffer.getMaximum());
        newOfferStmt.setDate(8, new java.sql.Date(newOffer.getExpiration().getTime()));
        int rowAffected = newOfferStmt.executeUpdate();
        System.out.println(rowAffected);
        ResultSet newIDRS = newOfferStmt.getGeneratedKeys(); // this doesn't return anything if you select the key
                                                             // yourself
        newIDRS.next();
        int newID = newIDRS.getInt(1);
        return newID;
    }

    public ResultSet searchByVendor(String vendor) throws SQLException {
        searchByVendor.setString(1, vendor);
        ResultSet foundVendors = searchByVendor.executeQuery();
        return foundVendors;
    }

    public void displayResults(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1)
                System.out.print(",  ");
            System.out.print(rsmd.getColumnName(i));
        }
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    System.out.print(",  ");
                System.out.print(rs.getString(i));
            }
            System.out.println("");
        }
    }

    // rs = stmt.executeQuery("SELECT * from city WHERE name = 'testcity'");
    // rs.next();
    // System.out.println(rs.getString("ID") + " " + rs.getString("Name") + " " +
    // rs.getString("district"));

    // stmt.execute("DELETE FROM city WHERE name = 'testcity'");

}