package com.AIMS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class DbConnect {
    private final String url = "jdbc:postgresql://localhost/db_mp";
    private final String user = "postgres";
    private final String password = "admin";

    public Connection connect() throws SQLException, IOException, ClassNotFoundException {
        Connection conn = null;
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to the PostgreSQL server successfully.");
        return conn;
    }
    public void createTable(Connection connection,String command) throws SQLException, IOException {
        //Create a statement using connection object
        Statement statement = connection.createStatement();
        // Execute the query or update query
        statement.execute(command);
    }
}
