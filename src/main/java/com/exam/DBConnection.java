package com.exam;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

private static final String URL =
        "jdbc:mysql://localhost:3306/online_exam";

private static final String USER = "root";

private static final String PASSWORD = "sandhiya@2007";

public static Connection getConnection() {

    Connection con = null;

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");

        con = DriverManager.getConnection(
                URL, USER, PASSWORD);

        System.out.println("Database Connected Successfully!");

    } catch (Exception e) {

        System.out.println("Database Connection Failed!");
        e.printStackTrace();

    }

    return con;
}


}
