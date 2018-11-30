package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    public static ScreenController getScreenController() {
        return screenController;
    }


    private static ScreenController screenController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource( "login.fxml" ));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        screenController = new ScreenController(scene);
        screenController.addScreen("login", FXMLLoader.load(getClass().getResource( "login.fxml" )));
        screenController.addScreen("menu", FXMLLoader.load(getClass().getResource( "menu.fxml" )));
        screenController.addScreen("addMovieWindow", FXMLLoader.load(getClass().getResource( "addMovieWindow.fxml" )));
        screenController.addScreen("movieCreated", FXMLLoader.load(getClass().getResource( "movieCreated.fxml" )));
        screenController.addScreen("movieAlreadyExists", FXMLLoader.load(getClass().getResource( "movieAlreadyExists.fxml" )));
        screenController.activate("login");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
