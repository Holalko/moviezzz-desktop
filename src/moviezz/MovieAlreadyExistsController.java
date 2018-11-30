package moviezz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieAlreadyExistsController {

    public void clickYes() {
        try {
            String messageBody = JsonObject.getMessage();
            URL url = new URL("http://localhost:8080/movies/rewrite");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");


            OutputStream os = conn.getOutputStream();
            os.write(messageBody.getBytes());
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
                System.out.println(output);
            }
            conn.disconnect();
            JsonObject.setMessage("");
                Main.getScreenController().activate("movieCreated");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clickNo() {
        JsonObject.setMessage("");
        Main.getScreenController().activate("menu");
    }

}
