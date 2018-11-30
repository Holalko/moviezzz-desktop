package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MovieInfoController {

    @FXML
    private Label nameTxt;
    @FXML
    private Label yearTxt;
    @FXML
    private Label priceTxt;
    @FXML
    private Label adultsTxt;
    @FXML
    private Label descTxt;
    @FXML
    private Label msg;
    @FXML
    private Button reserveBtn;


    @FXML
    public void initialize() {
        Long movieId = AllMoviesController.selectedMovieId;
        try {

            URL url = new URL("http://localhost:8080/movies/" + movieId);
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
            Movie movie = null;
            while ((output = br.readLine()) != null) {
                Gson gson = new Gson();
                movie = gson.fromJson(output, Movie.class);
            }


            conn.disconnect();
            initFields(movie);
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private void initFields(Movie movie) {
        nameTxt.setText(movie.getName());
        yearTxt.setText(String.valueOf(movie.getYearOfRelease()));
        priceTxt.setText(String.valueOf(movie.getPrice()));
        adultsTxt.setText(String.valueOf(movie.getForAdults()));
        descTxt.setText(movie.getDescription());
        descTxt.setWrapText(true);

        Long userId = Main.getScreenController().getLoggedUserId();
        if (userId == 77777){
            reserveBtn.setDisable(true);
            msg.setText("Admin can't reserve movies");
        }
    }

    public void reserve(){
        Long movieId = AllMoviesController.selectedMovieId;
        Long userId = Main.getScreenController().getLoggedUserId();
        try {

            URL url = new URL("http://localhost:8080/movies/reserve?movieId=" + movieId + "&userId=" + userId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");



            OutputStream os = conn.getOutputStream();
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                msg.setText("Error while reserving the movie");
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            boolean reserved = false;
            String output;
            while ((output = br.readLine()) != null) {
                reserved = Boolean.valueOf(output);
            }
            if(reserved){
                msg.setText("Successfully reserved!");
            } else {
                msg.setText("Movie is already reserved by someone else. Watch dog has been added to movie");
            }
            conn.disconnect();
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }


    }

}
