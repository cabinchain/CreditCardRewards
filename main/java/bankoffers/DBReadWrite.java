package bankoffers;

import java.sql.*;

public class DBReadWrite {
    // Variables
    private Connection connection;
    private PreparedStatement newOfferStmt;
    private PreparedStatement searchByVendorStmt;
    private PreparedStatement selectOfferStmt;
    private PreparedStatement useOfferStmt;

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
        String searchByVendorString = "SELECT * FROM offers WHERE Vendor LIKE ?";
        searchByVendorStmt = connection.prepareStatement(searchByVendorString, Statement.RETURN_GENERATED_KEYS);
        String selectOfferString = "SELECT * FROM offers WHERE Offer_ID = ? AND Date_Used IS NULL";
        selectOfferStmt = connection.prepareStatement(selectOfferString, Statement.RETURN_GENERATED_KEYS);
        String useOfferString = "UPDATE offers SET Date_Used = ?, Amount_Saved = ? WHERE Offer_ID = ?";
        useOfferStmt = connection.prepareStatement(useOfferString, Statement.RETURN_GENERATED_KEYS);
    }

    public boolean close() {
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
        newOfferStmt.setString(1, newOffer.getBank());
        newOfferStmt.setString(2, "Single");
        newOfferStmt.setString(3, newOffer.getVendor());
        newOfferStmt.setDouble(4, newOffer.getPercent());
        newOfferStmt.setDouble(5, newOffer.getAmount());
        newOfferStmt.setDouble(6, newOffer.getMinimum());
        newOfferStmt.setDouble(7, newOffer.getMaximum());
        newOfferStmt.setDate(8, new java.sql.Date(newOffer.getExpiration().getTime()));
        newOfferStmt.executeUpdate();
        ResultSet newIDRS = newOfferStmt.getGeneratedKeys(); // this doesn't return anything if you select the key
                                                             // yourself
        newIDRS.next();
        int newID = newIDRS.getInt(1);
        return newID;
    }

    public void displayResults(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        for (int i = 1; i <= columnsNumber; i++) {
            if (i > 1)
                System.out.print(",  ");
            System.out.print(rsmd.getColumnName(i));
        }
        System.out.println("");
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    System.out.print(",  ");
                System.out.print(rs.getString(i));
            }
            System.out.println("");
        }
    }

    public ResultSet searchByVendor(String vendor) throws SQLException {
        searchByVendorStmt.setString(1, "%" + vendor + "%");
        ResultSet foundVendors = searchByVendorStmt.executeQuery();
        return foundVendors;
    }

    public Offer selectOffer(int id) throws SQLException {
        selectOfferStmt.setInt(1, id);
        ResultSet rs = selectOfferStmt.executeQuery();
        while (rs.next()) {
            return new Offer(rs.getString(2), rs.getString(4), rs.getDouble(5),
                    rs.getDouble(6), rs.getDouble(7),
                    rs.getDouble(8), rs.getDate(9));
        }
        System.out.println("No eligible offers");
        return null;
    }

    public double checkOffer(int id, double spending) throws SQLException {
        Offer o = this.selectOffer(id);
        return o.expectedSavings(spending);
    }

    public double useOffer(int id, double spending) throws SQLException {

        double savings = this.checkOffer(id, spending);

        // Update table
        useOfferStmt.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
        useOfferStmt.setDouble(2, savings);
        useOfferStmt.setInt(3, id);
        useOfferStmt.executeUpdate();

        return savings;
    }

}