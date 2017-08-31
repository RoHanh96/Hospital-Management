/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Chung;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author trieu_000
 */
public class Connect {

    private Connection con;

    public Connection connect() {

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/qlbv", "postgres", "a9291698");
            //System.out.println("Connected!");
            return con;
        } catch (Exception e) {
            //System.out.println("No connect!");
        }
        return null;
    }
}
