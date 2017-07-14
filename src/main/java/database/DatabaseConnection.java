package database;

import java.sql.*;

public class DatabaseConnection {

    private String databaseName;
    private Connection connection;

    public DatabaseConnection(String driver, String prefix, String username, String password, String hostname,
                              int port, String databaseName) {
        this.databaseName = databaseName;

        this.connection = null;

        try {
            Class.forName(driver);

            String connectionURL = "jdbc:" + prefix + "://" + hostname + ":" + port + "/" + databaseName;

            this.connection = DriverManager.getConnection(connectionURL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
