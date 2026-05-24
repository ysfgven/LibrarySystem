package db;

import util.ErrorHandler;
import util.LogHelper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

    private static String URL;
    private static String USER;
    private static String PASS;



    static {
        loadConfig();
    }

    private static void loadConfig() {
        try (InputStream is = DatabaseManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) throw new IOException("db.properties not found");
            Properties props = new Properties();
            props.load(is);
            URL  = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASS = props.getProperty("db.password");

        } catch (IOException e) {
            LogHelper.logException(e);
            ErrorHandler.showErrorAlert("Error", "Config Error", "db.properties could not be loaded.", true);
        }
    }

    public static synchronized Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }



}