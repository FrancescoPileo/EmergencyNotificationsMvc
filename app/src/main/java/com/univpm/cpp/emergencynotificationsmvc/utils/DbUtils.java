package com.univpm.cpp.emergencynotificationsmvc.utils;

import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.models.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DbUtils {

    private static final String DB_HOST = "sql11.freemysqlhosting.net:3306/";
    private static final String DB_NAME = "sql11163669";
    private static final String DB_USERNAME = "sql11163669";
    private static final String DB_PASSWORD = "2s4U2p4n4U";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    private static Connection connection;


    /**
     * Permette la connessione con il DB
     * @return L'oggetto Connection che identifica la connessione
     * @throws Exception
     */
    private static Connection DbConnection() throws Exception
    {
        Class.forName(DB_DRIVER).newInstance();
        String ConnURL = "jdbc:mysql://" + DB_HOST + DB_NAME;
        return DriverManager.getConnection(ConnURL, DB_USERNAME, DB_PASSWORD);
    }


    /**
     * Permette l'esecuzione di queries di tipo SELECT
     * @param query Stringa che descrive la query in linguaggio SQL
     * @return Il ResultSet ottenuto dall'esecuzione della query
     * @throws Exception
     */
    private static ResultSet executeSelectQuery(String query) throws Exception
    {
        ResultSet rSet = null;
        connection = DbConnection();
        Statement stmt = connection.createStatement();
        rSet = stmt.executeQuery(query);
        return rSet;
    }

    /**
     * Permette l'esecuzione di queries di manipolazione
     * @param query Stringa che descrive la query in linguaggio SQL
     * @return Il numero di righe inlfuenzate dalla manipolazione
     * @throws Exception
     */
    private static int executeManipulationQuery(String query) throws Exception
    {
        int affectedRows = 0;
        connection = DbConnection();
        Statement stmt = connection.createStatement();
        affectedRows = stmt.executeUpdate(query);
        connection.close();
        return affectedRows;
    }



    /**
     * Ottiene l'utente con l'username specificato
     * @param username dell'utente da cercare
     * @return oggetto User che rappresenta l'utente, null se non vi è utente con quell'username
     */
    public static User getUser(String username){
        User user = null;
        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM User WHERE username='" + username + "'");
            if (rs.next()) {

                user = new User();

                user.setId(rs.getInt("idUser"));
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setUsername(rs.getString("username"));
                user.setAge(rs.getInt("age"));
                user.setMobilephone(rs.getString("mobilephone"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static boolean newUser(User user){
        Log.w("DBUtils", user.getName());
        String mobilephone = "NULL";
        if (user.getMobilephone() != null){
            mobilephone = user.getMobilephone();
        }

        String age = "NULL";
        if (user.getAge()!= -1){
            age = String.valueOf(user.getAge());
        }

        int rows = 0;
        try {
            rows = executeManipulationQuery("INSERT INTO `User`(`idUser`, `name`, `surname`, `username`, `age`, `mobilephone`, `email`, `password`) " +
                    "VALUES (NULL,'" + user.getName() + "','" + user.getSurname() + "' , '" + user.getUsername() + "', " + age +
                    ", '" + mobilephone + "', '" + user.getEmail() + "','" + user.getPassword() + "')");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows != 0;
    }

    public static Map getMapById(int idMap) {

        Map map = null;
        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM Map WHERE idMap='" + idMap + "'");
            if (rs.next()) {

                map = new Map();

                map.setIdMap(rs.getInt("idMap"));
                map.setBuilding(rs.getString("building"));
                map.setFloor(rs.getString("floor"));
                map.setImagePath(rs.getString("image"));
            }
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map getMapByFloor(String building, String floor) {

        Map map = null;
        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM Map WHERE building='" + building + "' AND floor='" + floor + "'");
            if (rs.next()) {

                map = new Map();

                map.setIdMap(rs.getInt("idMap"));
                map.setBuilding(rs.getString("building"));
                map.setFloor(rs.getString("floor"));
                map.setImagePath(rs.getString("image"));
            }
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static ArrayList<Map> getAllMaps () {

        ArrayList<Map> list = new ArrayList<Map>();
        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM Map");
            while (rs.next()) {

                Map map = new Map();

                map.setIdMap(rs.getInt("idMap"));
                map.setBuilding(rs.getString("building"));
                map.setFloor(rs.getString("floor"));
                map.setImagePath(rs.getString("image"));

                list.add(map);
            }
            connection.close();

        }
        catch (Exception e) {
        e.printStackTrace();
    }
        return list;
    }
}
