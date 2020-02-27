package de.szut.simon.BlumentalerAue;

import de.szut.simon.BlumentalerAue.data.Pflanzenmittel;
import de.szut.simon.BlumentalerAue.data.Umweldatum;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class DBController {
    private static final DBController dbcontroller = new DBController();
    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des JDBC-Treibers");
            e.printStackTrace();
        }
    }

    private File DB_PATH = Paths.get("db", "aue.db").toFile();

    private DBController() {
    }

    @NotNull
    public static DBController getInstance() {
        return dbcontroller;
    }

    public static void main(String[] args) {
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        dbc.handleDB();
    }

    public static Connection getConnection() {
        return connection;
    }

    @NotNull
    public File getDbPath() {
        return DB_PATH;
    }

    public void setDbPath(@NotNull File dbPath) {
        closeDB();
        this.DB_PATH = dbPath;
        initDBConnection();
    }

    void initDBConnection() {
        try {
            if (connection != null || !DB_PATH.exists())
                return;
            System.out.println("Creating Connection to Database...");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            if (!connection.isClosed())
                System.out.println("...Connection established");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::closeDB));
    }

    void closeDB() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                if (connection.isClosed())
                    System.out.println("Connection to Database closed");
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void handleDB() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS books;");
            stmt.executeUpdate("CREATE TABLE books (author, title, publication, pages, price);");
            //

            PreparedStatement ps = connection.prepareStatement("INSERT INTO books VALUES (?, ?, ?, ?, ?);");

            ps.setString(1, "Willi Winzig");
            ps.setString(2, "Willi's Wille");
            //ps.setDate(3, Date.valueOf("2011-10-16"));
            ps.setDate(3, Date.valueOf("2011-10-16"));
            ps.setInt(4, 432);
            ps.setDouble(5, 32.95);
            ps.addBatch();

            ps.setString(1, "Anton Antonius");
            ps.setString(2, "Anton's Alarm");
            ps.setDate(3, Date.valueOf("2009-11-01"));
            ps.setInt(4, 123);
            ps.setDouble(5, 98.76);
            ps.addBatch();

            connection.setAutoCommit(false);
            ps.executeBatch();
            connection.setAutoCommit(true);

            ResultSet rs = stmt.executeQuery("SELECT * FROM books;");
            while (rs.next()) {
                System.out.println("Autor = " + rs.getString("author"));
                System.out.println("Titel = " + rs.getString("title"));
                System.out.println("Erscheinungsdatum = " + rs.getDate("publication"));
                System.out.println("Seiten = " + rs.getInt("pages"));
                System.out.println("Preis = " + rs.getDouble("price"));
            }
            rs.close();


            rs = stmt.executeQuery("SELECT * FROM umweltdaten;");
            while (rs.next()) {
                System.out.println("1 = " + rs.getString("1"));
                System.out.println("2 = " + rs.getString("2"));
                System.out.println("3 = " + rs.getDate("3"));

            }
            rs.close();


            connection.close();
        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
    }

    @NotNull
    public List<Pflanzenmittel> getPflanzenmittel() throws SQLException {
        List<Pflanzenmittel> pflanzenmittel = new ArrayList<>();

        Statement pflanzenQuery = connection.createStatement();
        ResultSet pflanzenResult = pflanzenQuery.executeQuery("SELECT * FROM pflanzenmittel");

        while (pflanzenResult.next()) {
            pflanzenmittel.add(new Pflanzenmittel(pflanzenResult.getString("Nr"), pflanzenResult.getString("Mittel")));
        }

        pflanzenResult.close();

        return pflanzenmittel;
    }

    @NotNull
    public List<Umweldatum> getUmweldaten() throws SQLException {
        List<Umweldatum> umweltdaten = new ArrayList<>();

        Statement umweltdatenQuery = connection.createStatement();
        ResultSet umweltdatenResult = umweltdatenQuery.executeQuery("SELECT * FROM umweltdaten");

        while (umweltdatenResult.next()) {
            umweltdaten.add(new Umweldatum(umweltdatenResult.getString("index"), umweltdatenResult.getDouble("1"), umweltdatenResult.getDouble("2"), umweltdatenResult.getDouble("3")));
        }

        umweltdatenResult.close();

        return umweltdaten;
    }
}