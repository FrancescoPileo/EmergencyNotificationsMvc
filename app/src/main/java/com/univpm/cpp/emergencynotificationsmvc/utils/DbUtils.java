package com.univpm.cpp.emergencynotificationsmvc.utils;

import android.util.Log;

import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.envValues.EnviromentalValues;
import com.univpm.cpp.emergencynotificationsmvc.models.map.Map;
import com.univpm.cpp.emergencynotificationsmvc.models.node.Node;
import com.univpm.cpp.emergencynotificationsmvc.models.position.Position;
import com.univpm.cpp.emergencynotificationsmvc.models.session.Session;
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
                user.setGuest(rs.getBoolean("isGuest"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Ottiene l'utente con l'username specificato
     * @return oggetto User che rappresenta l'utente, null se non vi è utente con quell'username
     */
    public static User getLastGuestUser(){
        User user = null;
        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM User WHERE isGuest='1' ORDER BY idUser DESC LIMIT 1");
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
                user.setGuest(rs.getBoolean("isGuest"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static boolean newValues(EnviromentalValues values){
        int rows = 0;
        try {
            rows = executeManipulationQuery("INSERT INTO `EnviromentalValues`(`idEnv`, `idBeacon`, `time`, `temperature`, `humidity`, " +
                    "`accX`, `accY`, `accZ`, `gyrX`, `gyrY`, `gyrZ`, `magX`, `magY`, `magZ`) " +
                    "VALUES (NULL,'" + values.getBeacon() + "','" + values.getTime() + "' , '" + values.getTemperature() + "', " + values.getHumidity() +
                    ", '" + values.getAccX() + "', '" + values.getAccY() + "','" + values.getAccZ() + "','"
                    + values.getGyrX() + "','" + values.getGyrY() + "','" + values.getGyrZ() + "','"
                    + values.getMagX() + "','" + values.getMagY() + "','" + values.getMagZ() + "')");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows != 0;
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

    public static boolean newGuestUser(int index){
        int rows = 0;
        try {
            rows = executeManipulationQuery("INSERT INTO `User`(`username`, `isGuest`) " +
                    "VALUES ('guest#" + String.valueOf(index) + "' ,'1')");
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
                map.setName(rs.getString("name"));
                //map.setImagePath(rs.getString("path"));
                map.setxRef(rs.getInt("xRef"));
                map.setyRef(rs.getInt("yRef"));
                map.setxRefpx(rs.getInt("xRefpx"));
                map.setyRefpx(rs.getInt("yRefpx"));
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
                map.setName(rs.getString("name"));
                //map.setImagePath(rs.getString("path"));
            }
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map getMapByName(String name) {

        Map map = null;
        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM Map WHERE name='" + name + "'");
            if (rs.next()) {

                map = new Map();

                map.setIdMap(rs.getInt("idMap"));
                map.setBuilding(rs.getString("building"));
                map.setFloor(rs.getString("floor"));
                map.setName(rs.getString("name"));
                map.setScale(rs.getFloat("scale"));
                //map.setImagePath(rs.getString("path"));
                map.setxRef(rs.getInt("xRef"));
                map.setyRef(rs.getInt("yRef"));
                map.setxRefpx(rs.getInt("xRefpx"));
                map.setyRefpx(rs.getInt("yRefpx"));
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
                map.setName(rs.getString("name"));
                //map.setImagePath(rs.getString("path"));

                list.add(map);
            }
            connection.close();

        }
        catch (Exception e) {
        e.printStackTrace();
    }
        return list;
    }

    public static ArrayList<String> getAllNames () {

        ArrayList<String> list = new ArrayList<String>();
        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM Map");
            while (rs.next()) {
                list.add(rs.getString("name"));
            }
            connection.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Beacon getBeaconById(String idBeacon) {

        Beacon beacon = null;
        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM Beacon WHERE idBeacon='" + idBeacon + "'");
            if (rs.next()) {

                beacon = new Beacon();

                beacon.setIdBeacon(rs.getString("idBeacon"));
                //beacon.setNode(rs.getInt("idNode"));

            }
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return beacon;
    }

    public static Node getNodeById(int idNode) {

        Node node = null;
        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM Node WHERE idNode='" + idNode + "'");
            if (rs.next()) {

                node = new Node();

                node.setIdNode(rs.getInt("idNode"));
                //node.setMap(rs.getInt("idMap"));
                node.setX(rs.getInt("x"));
                node.setY(rs.getInt("y"));

            }
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return node;
    }

    public static ArrayList<Node> getNodeByMap(int idMap) {

        ArrayList<Node> list = new ArrayList<Node>();

        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM Node WHERE idMap='" + idMap + "");
            while (rs.next()) {

                Node node = new Node();

                node.setIdNode(rs.getInt("idNode"));
                //node.setMap(rs.getInt("idMap"));
                node.setX(rs.getInt("x"));
                node.setY(rs.getInt("y"));

                list.add(node);
            }
            connection.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean newPosition(Position position){

        int rows = 0;
        try {
            rows = executeManipulationQuery("INSERT INTO `Position`(`idPosition`, `idNode`, `idUser`, `time` )" +
                    "VALUES (NULL,'" + position.getIdPosition() + "','" + position.getNode() + "' , '" + position.getUser() + "', " + position.getTime()  + "')");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows != 0;
    }

    public static ArrayList<Position> getPositionByIdUser(int idUser) {

        ArrayList<Position> positions = new ArrayList<>();

        try {
            ResultSet rs = executeSelectQuery("SELECT * FROM Position WHERE idUser='" + idUser + "'");
            while (rs.next()) {

                Position position = new Position();

                position.setIdPosition(rs.getInt("idPosition"));
                //position.setNode(rs.getInt("idNode"));
                //position.setUser(rs.getInt("idUser"));
                position.setTime(rs.getString("time"));

                positions.add(position);
            }
            connection.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return positions;
    }

    public static boolean newSession(Session session){
        int rows = 0;
        try {
            rows = executeManipulationQuery("INSERT INTO `Session`(`username`, `timeIn`) " +
                    "VALUES ('" + session.getUser() + "','" + session.getTimeIn() + "')");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows != 0;
    }

    public static boolean updateSession(Session session){
        int rows = 0;
        try {
            rows = executeManipulationQuery("UPDATE `Session` SET timeOut='" + session.getTimeOut() +
                    "' WHERE  username=' " + session.getUser() + "' AND timeIn='" + session.getTimeIn() + "'");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows != 0;
    }

}
