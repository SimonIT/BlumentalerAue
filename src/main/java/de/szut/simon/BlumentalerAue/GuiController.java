package de.szut.simon.BlumentalerAue;

import de.szut.simon.BlumentalerAue.data.Pflanzenmittel;
import de.szut.simon.BlumentalerAue.data.Umweldatum;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.sql.Statement;

public class GuiController {

	@FXML
	public TableColumn<Umweldatum, Double> valueColumn;
	@FXML
	public TableView<Umweldatum> umweltdatenTable;
	@FXML
	public ComboBox<Pflanzenmittel> pflanzenmittelBox;
	@FXML
	public LineChart<Long, Double> valueChart;

	Stage stage;

	private DBController dbController = DBController.getInstance();
	private FileChooser.ExtensionFilter sqliteExtensionFilter = new FileChooser.ExtensionFilter("SQLITE Database", "*.sqlite", "*.db", "*.db3", "*.sqlite3");

	@FXML
	public void initialize() {
		pflanzenmittelBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
			valueChart.getData().clear();
			XYChart.Series<Long, Double> pflanzenmittelSeries = new XYChart.Series<>();
			pflanzenmittelSeries.setName(t1.getMittel());

			ObservableList<XYChart.Data<Long, Double>> pflanzenmittelData = pflanzenmittelSeries.getData();
			String property;
			switch (t1.getNr()) {
				case "1":
					property = "one";
					for (Umweldatum umweldatum : umweltdatenTable.getItems()) {
						pflanzenmittelData.add(new XYChart.Data<>(umweldatum.getIndex(), umweldatum.getOne()));
					}
					break;
				case "2":
					property = "two";
					for (Umweldatum umweldatum : umweltdatenTable.getItems()) {
						pflanzenmittelData.add(new XYChart.Data<>(umweldatum.getIndex(), umweldatum.getTwo()));
					}
					break;
				case "3":
					property = "three";
					for (Umweldatum umweldatum : umweltdatenTable.getItems()) {
						pflanzenmittelData.add(new XYChart.Data<>(umweldatum.getIndex(), umweldatum.getThree()));
					}
					break;
				default:
					throw new IllegalStateException("Unexpected value: " + t1.getNr());
			}
			valueColumn.setCellValueFactory(new PropertyValueFactory<>(property));
			umweltdatenTable.refresh();
			valueChart.getData().addAll(pflanzenmittelSeries);
		});
	}

	@FXML
	public void connectDatabase(ActionEvent actionEvent) {
		try {
			dbController.initDBConnection();
		} catch (RuntimeException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("Verbindung zur Datenbank nicht möglich.");
			alert.setContentText(String.format("%s%s%s\n\n\n%s", "Eine Verbindung zur Datenbank \"",
				dbController.getDbPath().getAbsolutePath(), "\" konnte nicht aufgebaut werden.", e.toString()));
			alert.show();
		}
	}

	@FXML
	public void populateDefaultData(ActionEvent actionEvent) {
		try {
			String sqlFile = IOUtils.toString(getClass().getResource("db/Blumenthal.sql"));
			Statement fileStatements = DBController.getConnection().createStatement();
			fileStatements.executeUpdate(sqlFile);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void close(ActionEvent actionEvent) {
		stage.close();
	}

	@FXML
	public void readData(ActionEvent actionEvent) {
		try {
			if (!pflanzenmittelBox.getItems().isEmpty())
				pflanzenmittelBox.getItems().clear();
			pflanzenmittelBox.getItems().addAll(DBController.getInstance().getPflanzenmittel());
			if (!umweltdatenTable.getItems().isEmpty())
				umweltdatenTable.getItems().clear();
			umweltdatenTable.getItems().addAll(DBController.getInstance().getUmweldaten());
		} catch (SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText("DAtenbank nicht lesbar");
			alert.setContentText(String.format("%s\n\n\n%s",
				"Die Daten konnten nicht aus der Datenbank gelesen werden.", e.toString()));
			alert.show();
		}
	}

	@FXML
	public void chooseDatabase(ActionEvent actionEvent) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(dbController.getDbPath().getParentFile());
		chooser.getExtensionFilters().add(sqliteExtensionFilter);
		File dbFile = chooser.showOpenDialog(stage);

		if (dbFile != null) {
			dbController.setDbPath(dbFile);
		}
	}

	@FXML
	public void createDatabase(ActionEvent actionEvent) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(dbController.getDbPath().getParentFile());
		chooser.getExtensionFilters().add(sqliteExtensionFilter);
		File dbFile = chooser.showSaveDialog(stage);

		if (dbFile != null) {
			dbController.setDbPath(dbFile);
			dbController.initDBConnection();
		}
	}

	@FXML
	public void showAboutDialog(ActionEvent actionEvent) {
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
