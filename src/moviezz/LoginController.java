package moviezz;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginController {



    @FXML
    private TextField email;
    @FXML
    private PasswordField password;

    @FXML
    private Label errorMsg;

    public void loginClick(){
        if(email.getText().isEmpty() || password.getText().isEmpty()){
            errorMsg.setText("Invalid username or password!");
            return;
        }
        try {
            URL url = new URL("http://localhost:8080/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = "{" +
                    "\"email\":\"" + email.getText() + "\"," +
                    "\"password\":\"" + password.getText() + "\"" +
                    "}";

            JsonObject.setMessage(input);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();


            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                errorMsg.setText("Invalid username or password!");
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                Long userId = Long.parseLong(output);
                Main.getScreenController().setLoggedUserId(userId);
            }
            Main.getScreenController().activate("menu");
            conn.disconnect();
        } catch (MalformedURLException e) {
            errorMsg.setText("Invalid username or password!");
            e.printStackTrace();
        } catch (IOException e) {
            errorMsg.setText("Invalid username or password!");
            e.printStackTrace();
        }



    }
}
