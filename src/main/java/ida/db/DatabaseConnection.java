/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ida.db;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class DatabaseConnection {
    public Connection conn;
    private Statement statement;
    public static DatabaseConnection db;

    //    private DatabaseConnection() {
//        String url = "jdbc:mysql://idabot.ce5uk1li1gbt.eu-central-1.rds.amazonaws.com:3306/";
//        String driver = "com.mysql.jdbc.Driver";
//        String userName = "thekgiga";
//        String password = "Kragujevac.94";
//        try {
//            Class.forName(driver).newInstance();
//            this.conn = (Connection) DriverManager.getConnection(url, userName, password);
//        } catch (Exception sqle) {
//            sqle.printStackTrace();
//        }
//    }
    private DatabaseConnection() {
        try {
            URI dbUri = new URI(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

            this.conn = DriverManager.getConnection(dbUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * @return MysqlConnect Database connection object
     */
    public static synchronized DatabaseConnection getInstance() {
        if (db == null) {
            db = new DatabaseConnection();
        }
        return db;

    }

    /**
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException {
        statement = db.conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }

    /**
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException
     * @desc Method to insert data to a table
     */
    public int insert(String insertQuery) throws SQLException {
        statement = db.conn.createStatement();
        int result = statement.executeUpdate(insertQuery);
        return result;

    }
}