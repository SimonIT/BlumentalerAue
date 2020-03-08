package de.szut.simon.BlumentalerAue;

import de.szut.simon.BlumentalerAue.data.EnvironmentRecord;
import de.szut.simon.BlumentalerAue.data.PlantProtectant;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

/**
 * Controller of the gui
 */
public class GuiController {

	@FXML
	public TableColumn<EnvironmentRecord, Double> valueColumn;
	@FXML
	public TableView<EnvironmentRecord> umweltdatenTable;
	@FXML
	public ComboBox<PlantProtectant> pflanzenmittelBox;
	@FXML
	public LineChart<Long, Double> valueChart;
	@FXML
	public Label statusLabel;

	/**
	 * Stage of the window
	 */
	Stage stage;

	/**
	 * Database controller instance
	 */
	private DBController dbController = DBController.getInstance();
	/**
	 * Extension filter for sqlite databases
	 */
	private FileChooser.ExtensionFilter sqliteExtensionFilter = new FileChooser.ExtensionFilter("SQLITE Database", "*.sqlite", "*.db", "*.db3", "*.sqlite3");

	@FXML
	public void initialize() {
		pflanzenmittelBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
			valueChart.getData().clear();
			XYChart.Series<Long, Double> pflanzenmittelSeries = new XYChart.Series<>();
			pflanzenmittelSeries.setName(t1.getName());

			ObservableList<XYChart.Data<Long, Double>> pflanzenmittelData = pflanzenmittelSeries.getData();
			String property;
			switch (t1.getNo()) {
				case "1":
					property = "one";
					for (EnvironmentRecord environmentRecord : umweltdatenTable.getItems()) {
						pflanzenmittelData.add(new XYChart.Data<>(environmentRecord.getIndex(), environmentRecord.getOne()));
					}
					break;
				case "2":
					property = "two";
					for (EnvironmentRecord environmentRecord : umweltdatenTable.getItems()) {
						pflanzenmittelData.add(new XYChart.Data<>(environmentRecord.getIndex(), environmentRecord.getTwo()));
					}
					break;
				case "3":
					property = "three";
					for (EnvironmentRecord environmentRecord : umweltdatenTable.getItems()) {
						pflanzenmittelData.add(new XYChart.Data<>(environmentRecord.getIndex(), environmentRecord.getThree()));
					}
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + t1.getNo());
			}
			valueColumn.setCellValueFactory(new PropertyValueFactory<>(property));
			umweltdatenTable.refresh();
			valueChart.getData().addAll(pflanzenmittelSeries);
		});
	}

	/**
	 * Connects to the database
	 */
	@FXML
	public void connectDatabase() {
		try {
			statusLabel.setText("Verbindung zur Datenbank wird hergestellt...");
			dbController.initDBConnection();
			statusLabel.setText("Verbindung zur Datenbank erfolgreich hergestellt!");
		} catch (RuntimeException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Verbindung zur Datenbank nicht möglich.");
			alert.setContentText(
				String.format("Eine Verbindung zur Datenbank \"%s\" konnte nicht aufgebaut werden.\n\n\n%s",
					dbController.getDbFile().getAbsolutePath(), e.toString()));
			alert.show();
		}
	}

	/**
	 * DIsconnects from the database
	 */
	@FXML
	public void disconnectDatabase() {
		statusLabel.setText("Schließe Verbindung zur Datenbank...");
		dbController.closeDB();
		statusLabel.setText("Verbindung zur Datenbank erfolgreich geschlossen!");
	}

	/**
	 * Adds the default data to the database
	 */
	@FXML
	public void populateDefaultData() {
		try {
			statusLabel.setText("Füge Daten zur Datenbank hinzu...");
			dbController.handleDB();
			statusLabel.setText("Daten erfolgreich zur Datenbank hinzugefügt!");
		} catch (IOException | SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Verbindung zur Datenbank nicht möglich.");
			alert.setContentText(
				String.format("Eine Verbindung zur Datenbank \"%s\" konnte nicht aufgebaut werden.\n\n\n%s",
					dbController.getDbFile().getAbsolutePath(), e.toString()));
			alert.show();
		}
	}

	/**
	 * Closes the window
	 */
	@FXML
	private void close() {
		stage.close();
	}

	/**
	 * Reads the data from the database
	 */
	@FXML
	public void readData() {
		try {
			statusLabel.setText("Lese Daten ein...");
			if (!pflanzenmittelBox.getItems().isEmpty())
				pflanzenmittelBox.getItems().clear();
			pflanzenmittelBox.getItems().addAll(DBController.getInstance().getPlantProtectants());
			if (!umweltdatenTable.getItems().isEmpty())
				umweltdatenTable.getItems().clear();
			umweltdatenTable.getItems().addAll(DBController.getInstance().getEnvironmentRecords());
			statusLabel.setText("Daten erfolgreich eingelesen!");
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Datenbank nicht lesbar");
			alert.setContentText(
				String.format("Die Daten konnten nicht aus der Datenbank gelesen werden.\n\n\n%s", e.toString()));
			alert.show();
		}
	}

	/**
	 * Sets the database to the user chosen one
	 */
	@FXML
	public void chooseDatabase() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(dbController.getDbFile().getParentFile());
		chooser.getExtensionFilters().add(sqliteExtensionFilter);
		File dbFile = chooser.showOpenDialog(stage);

		if (dbFile != null) {
			dbController.setDbFile(dbFile);
			statusLabel.setText(String.format("Die Datenbank %s wurde erfolgreich ausgewählt!", dbFile.getAbsolutePath()));
		}
	}

	/**
	 * Creates a new database from the file
	 */
	@FXML
	public void createDatabase() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(dbController.getDbFile().getParentFile());
		chooser.getExtensionFilters().add(sqliteExtensionFilter);
		File dbFile = chooser.showSaveDialog(stage);

		if (dbFile != null) {
			dbController.setDbFile(dbFile);
			dbController.initDBConnection();
			statusLabel.setText(String.format("Die Datenbank %s wurde erfolgreich erstellt!", dbFile.getAbsolutePath()));
		}
	}

	/**
	 * Opens a dialog with information about the author
	 */
	@FXML
	public void showAboutDialog() {
		Dialog<Void> aboutDialog = new Dialog<>();
		aboutDialog.setTitle("Über BlumentalerAue");
		aboutDialog.setHeaderText("Über BlumentalerAue");

		DialogPane pane = aboutDialog.getDialogPane();
		Hyperlink gitHubLink = new Hyperlink("SourceCode auf GitHub");
		gitHubLink.setOnAction(dialogActionEvent -> {
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					desktop.browse(new URI("https://github.com/SimonIT/BlumentalerAue"));
				} catch (Exception ignored) {
				}
			}
		});
		pane.setContent(new VBox(new Label("BlumentalerAue ist ein Programm von Simon Bullik."), gitHubLink));
		pane.getButtonTypes().addAll(new ButtonType("Okay!", ButtonBar.ButtonData.CANCEL_CLOSE));

		aboutDialog.show();
	}
}
