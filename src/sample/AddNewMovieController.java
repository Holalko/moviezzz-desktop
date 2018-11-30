package sample;

import com.sun.corba.se.impl.orbutil.ObjectWriter;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Movie;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddNewMovieController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField priceField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private CheckBox forAdultsCheckbox;
    @FXML
    private Label validationMessage;

    @FXML
    public void initialize() {
        yearField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                yearField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                priceField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }


    public void addMovieClick() {
        Movie movie = new Movie();

        String name = nameField.getText();
        if (name.length() == 0) {
            setValidationMessage();
        }
        movie.setName(name);

        String year = yearField.getText();
        if (year.length() == 0) {
            setValidationMessage();
        }
        movie.setYearOfRelease(Integer.valueOf(year));

        String price = priceField.getText();
        if (price.length() == 0) {
            setValidationMessage();
        }
        movie.setPrice(Double.valueOf(price));

        String description = descriptionField.getText();
        if (description.length() == 0) {
            setValidationMessage();
        }
        movie.setDescription(description);

        Boolean forAdults = forAdultsCheckbox.isSelected();
        movie.setForAdults(forAdults);

        sendRequest(movie);
    }

    private void sendRequest(Movie movie) {
        try {

            URL url = new URL("http://localhost:8080/movies");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{" +
                    "\"name\":\"" + movie.getName() + "\"," +
                    "\"description\":\"" + movie.getDescription() + "\"," +
                    "\"price\":" + movie.getPrice() + "," +
                    "\"forAdults\":" + movie.getForAdults() + "," +
                    "\"yearOfRelease\":" + movie.getYearOfRelease() +
                    "}";

            JsonObject.setMessage(input);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();


            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            boolean alreadyExists = false;
            while ((output = br.readLine()) != null) {
                if (Long.parseLong(output) == -1L) {
                    alreadyExists = true;
                    Main.getScreenController().activate("movieAlreadyExists");
                }
            }
            if (!alreadyExists) {
                JsonObject.setMessage("");
                Main.getScreenController().activate("movieCreated");
            }
            conn.disconnect();
            nameField.clear();
            yearField.clear();
            priceField.clear();
            descriptionField.clear();
            forAdultsCheckbox.setSelected(false);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void setValidationMessage() {
        validationMessage.setText("INVALID VALUES. Please fill in valid values!");
    }

}
