package sample;


import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button addNewMovieBtn;


    @FXML
    private Button showBorrowedBtn;

    @FXML
    private Label errorMsg;

    public void openAddNewMovie() {
        Long userId = Main.getScreenController().getLoggedUserId();
        if (userId != 77777) {
            addNewMovieBtn.setDisable(true);
            errorMsg.setText("Only admin can add new movies!");
        } else {
            Main.getScreenController().activate("addMovieWindow");
        }
    }

    public void showMovies() {
        try {
            Main.getScreenController().addScreen("allMovies", FXMLLoader.load(getClass().getResource("allMovies.fxml")));
            Main.getScreenController().activate("allMovies");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBorrowedMovies() {
        try {
            Long userId = Main.getScreenController().getLoggedUserId();
            if (userId != 77777) {
                Main.getScreenController().addScreen("borrowedMovies", FXMLLoader.load(getClass().getResource("borrowedMovies.fxml")));
                Main.getScreenController().activate("borrowedMovies");
            } else {
                showBorrowedBtn.setDisable(true);
                errorMsg.setText("Admin doesn't have any borrowed movies!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
