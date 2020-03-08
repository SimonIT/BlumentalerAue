package de.szut.simon.BlumentalerAue;

import de.szut.simon.BlumentalerAue.data.EnvironmentRecord;
import de.szut.simon.BlumentalerAue.data.PlantProtectant;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * For encapsulated access to the database
 */
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

	/**
	 * Database file, default inside the project
	 */
	private File dbFile = Paths.get("db", "aue.db").toFile();

	/**
	 * Private constructor for singleton
	 */
	private DBController() {
	}

	/**
	 * @return the one and only controller
	 */
	@NotNull
	public static DBController getInstance() {
		return dbcontroller;
	}

	public static void main(String[] args) throws IOException, SQLException {
		DBController dbc = DBController.getInstance();
		dbc.initDBConnection();
		dbc.handleDB();
		dbc.closeDB();
	}

	/**
	 * @return db file
	 */
	@NotNull
	public File getDbFile() {
		return dbFile;
	}

	/**
	 * Sets the database file
	 * If the file does not exist, it will be created
	 *
	 * @param dbFile the db file
	 */
	public void setDbFile(@NotNull File dbFile) {
		closeDB();
		this.dbFile = dbFile;
	}

	/**
	 * Connects to the database
	 */
	void initDBConnection() {
		try {
			if (connection != null) // Don't check if the database exists to create a new oe if none exists
				return;
			if (!dbFile.getParentFile().exists())
				dbFile.getParentFile().mkdirs();
			System.out.println("Creating Connection to Database...");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
			if (!connection.isClosed())
				System.out.println("...Connection established");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		Runtime.getRuntime().addShutdownHook(new Thread(this::closeDB));
	}

	/**
	 * Closes the connection to the database
	 */
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

	/**
	 * Add default values to the database
	 *
	 * @throws IOException  If the sql file is not present
	 * @throws SQLException If the sql can't get executed
	 */
	void handleDB() throws IOException, SQLException {
		connection.createStatement().executeUpdate(
			IOUtils.toString(getClass().getResource("db/Blumenthal.sql"), StandardCharsets.UTF_8));
	}

	/**
	 * Get all plant protectants
	 *
	 * @return a list with all plant protectants
	 * @throws SQLException if the protectants can't get read
	 */
	@NotNull
	public List<PlantProtectant> getPlantProtectants() throws SQLException {
		List<PlantProtectant> plantProtectants = new ArrayList<>();

		if (connection == null)
			return plantProtectants;

		Statement protectantsQuery = connection.createStatement();
		ResultSet protectantsResult = protectantsQuery.executeQuery("SELECT * FROM pflanzenmittel");

		while (protectantsResult.next()) {
			plantProtectants.add(
				new PlantProtectant(
					protectantsResult.getString("Nr"),
					protectantsResult.getString("Mittel")
				)
			);
		}

		protectantsResult.close();

		return plantProtectants;
	}

	/**
	 * Get all environment records
	 *
	 * @return a list with all environment records
	 * @throws SQLException if the environment can't get read
	 */
	@NotNull
	public List<EnvironmentRecord> getEnvironmentRecords() throws SQLException {
		List<EnvironmentRecord> environmentRecords = new ArrayList<>();

		if (connection == null)
			return environmentRecords;

		Statement environmentRecordsQuery = connection.createStatement();
		ResultSet environmentRecordsResult = environmentRecordsQuery.executeQuery("SELECT * FROM umweltdaten");

		while (environmentRecordsResult.next()) {
			environmentRecords.add(
				new EnvironmentRecord(
					environmentRecordsResult.getLong("index"),
					environmentRecordsResult.getDouble("1"),
					environmentRecordsResult.getDouble("2"),
					environmentRecordsResult.getDouble("3")
				)
			);
		}

		environmentRecordsResult.close();

		return environmentRecords;
	}
}
