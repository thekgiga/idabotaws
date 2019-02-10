/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ida.db;

import java.sql.*;

public class DatabaseConnection {
    public Connection conn;
    private Statement statement;
    public static DatabaseConnection db;

    private DatabaseConnection() {
        String url = "jdbc:mysql://idabot.ce5uk1li1gbt.eu-central-1.rds.amazonaws.com:3306/?ssslca=config/rds-ca-2015-root.pem";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "thekgiga";
        String password = "Kragujevac.94";
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection) DriverManager.getConnection(url, userName, password);
        } catch (Exception sqle) {
            sqle.printStackTrace();
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