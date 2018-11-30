package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.BorrowedMovie;
import models.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

public class BorrowedMoviesController {

    @FXML
    private Label msg;

    @FXML
    private TableView<BorrowedMovie> borrowedTable;

    @FXML private TableColumn<BorrowedMovie, String> nameCol;
    @FXML private TableColumn<BorrowedMovie, Integer> yearCol;
    @FXML private TableColumn<BorrowedMovie, String> dueDateCol;
    @FXML private TableColumn<BorrowedMovie, Boolean> adultsCol;

    @FXML
    public void initialize(){
        Long userId = Main.getScreenController().getLoggedUserId();
        try {

            URL url = new URL("http://localhost:8080/movies/user/" + userId);
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
            List<BorrowedMovie> movies = null;
            while ((output = br.readLine()) != null) {
                Gson gson = new Gson();
                movies = gson.fromJson(output, new TypeToken<List<BorrowedMovie>>() {
                }.getType());
            }

            conn.disconnect();
            initTable(movies);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        borrowedTable.setRowFactory( tv -> {
            TableRow<BorrowedMovie> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Movie rowData = row.getItem();
                    extendReservation(rowData.getId());
                }
            });
            return row ;
        });

    }

    private void extendReservation(Long movieId){
        try {

            URL url = new URL("http://localhost:8080/movies/extend-reservation/" + movieId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");



            OutputStream os = conn.getOutputStream();
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                msg.setText("Error while extending reservation the movie");
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            boolean extended = false;
            String output;
            while ((output = br.readLine()) != null) {
                extended = Boolean.valueOf(output);
            }
            if(extended){
                msg.setText("Successfully extended!");
            } else {
                msg.setText("Movie is already followed by someone else. Could not be extended");
            }
            conn.disconnect();
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    private void initTable(List<BorrowedMovie> movies) {
        ObservableList<BorrowedMovie> movieObservableList = FXCollections.observableList(movies);
        borrowedTable.setItems(movieObservableList);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("yearOfRelease"));
        adultsCol.setCellValueFactory(new PropertyValueFactory<>("forAdults"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    }

}
