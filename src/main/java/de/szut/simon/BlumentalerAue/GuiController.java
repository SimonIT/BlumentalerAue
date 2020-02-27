package de.szut.simon.BlumentalerAue;

import de.szut.simon.BlumentalerAue.data.Pflanzenmittel;
import de.szut.simon.BlumentalerAue.data.Umweldatum;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
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
    public LineChart<String, Double> valueChart;

    Stage stage;
    private DBController dbController = DBController.getInstance();

    @FXML
    public void initialize() {
        pflanzenmittelBox.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            valueChart.getData().clear();
            XYChart.Series<String, Double> pflanzenmittelSeries = new XYChart.Series<>();
            pflanzenmittelSeries.setName(t1.getMittel());

            ObservableList<XYChart.Data<String, Double>> pflanzenmittelData = pflanzenmittelSeries.getData();
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
            System.out.println("Couldn't connect");
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
            e.printStackTrace();
        }
    }

    @FXML
    public void chooseDatabase(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(dbController.getDbPath().getParentFile());
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SQLITE Database", "*.sqlite", "*.db", "*.db3", "*.sqlite"));
        File dbFile = chooser.showOpenDialog(stage);

        if (dbFile != null) {
            dbController.setDbPath(dbFile);
        }
    }
}
