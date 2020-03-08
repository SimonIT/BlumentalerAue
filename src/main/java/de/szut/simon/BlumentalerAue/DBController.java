package de.szut.simon.BlumentalerAue;

import de.szut.simon.BlumentalerAue.data.Pflanzenmittel;
import de.szut.simon.BlumentalerAue.data.Umweldatum;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
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

	public static void main(String[] args) throws IOException, SQLException {
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
	}

	void initDBConnection() {
		try {
			if (connection != null) // Don't check if the database exists to create a new oe if none exists
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

	void handleDB() throws IOException, SQLException {
		connection.createStatement().executeUpdate(IOUtils.toString(getClass().getResource("db/Blumenthal.sql")));
	}

	@NotNull
	public List<Pflanzenmittel> getPflanzenmittel() throws SQLException {
		List<Pflanzenmittel> pflanzenmittel = new ArrayList<>();

		if (connection == null)
			return pflanzenmittel;

		Statement pflanzenQuery = connection.createStatement();
		ResultSet pflanzenResult = pflanzenQuery.executeQuery("SELECT * FROM pflanzenmittel");

		while (pflanzenResult.next()) {
			pflanzenmittel.add(
				new Pflanzenmittel(
					pflanzenResult.getString("Nr"),
					pflanzenResult.getString("Mittel")
				)
			);
		}

		pflanzenResult.close();

		return pflanzenmittel;
	}

	@NotNull
	public List<Umweldatum> getUmweldaten() throws SQLException {
		List<Umweldatum> umweltdaten = new ArrayList<>();

		if (connection == null)
			return umweltdaten;

		Statement umweltdatenQuery = connection.createStatement();
		ResultSet umweltdatenResult = umweltdatenQuery.executeQuery("SELECT * FROM umweltdaten");

		while (umweltdatenResult.next()) {
			umweltdaten.add(
				new Umweldatum(
					umweltdatenResult.getLong("index"),
					umweltdatenResult.getDouble("1"),
					umweltdatenResult.getDouble("2"),
					umweltdatenResult.getDouble("3")
				)
			);
		}

		umweltdatenResult.close();

		return umweltdaten;
	}
}
