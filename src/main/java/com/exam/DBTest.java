package com.exam;

import java.sql.Connection;

public class DBTest {

public static void main(String[] args) {

    Connection con = DBConnection.getConnection();

    if (con != null) {

        System.out.println("JDBC Connection Successful!");

        try {
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    } else {

        System.out.println("JDBC Connection Failed!");
    }
}


}
