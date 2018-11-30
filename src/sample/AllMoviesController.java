package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AllMoviesController {

    public static Long selectedMovieId;

    @FXML
    private TableView<Movie> movieTable;

    @FXML
    private TableColumn<Movie, String> nameCol;
    @FXML
    private TableColumn<Movie, Integer> yearCol;
    @FXML
    private TableColumn<Movie, Boolean> adultsCol;

    @FXML
    public void initialize() {
        try {

            URL url = new URL("http://localhost:8080/movies");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            List<Movie> movies = null;
            while ((output = br.readLine()) != null) {
                Gson gson = new Gson();
                movies = gson.fromJson(output, new TypeToken<List<Movie>>() {
                }.getType());
            }

            conn.disconnect();
            initTable(movies);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        movieTable.setRowFactory( tv -> {
            TableRow<Movie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Movie rowData = row.getItem();
                    try {
                        selectedMovieId = rowData.getId();
                        Main.getScreenController().addScreen("movieInfo", FXMLLoader.load(getClass().getResource( "movieInfo.fxml" )));
                        Main.getScreenController().activate("movieInfo");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });

    }

    private void initTable(List<Movie> movies) {
        ObservableList<Movie> movieObservableList = FXCollections.observableList(movies);
        movieTable.setItems(movieObservableList);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("yearOfRelease"));
        adultsCol.setCellValueFactory(new PropertyValueFactory<>("forAdults"));
    }




}
