package moviezz;

public class JsonObject {

    private JsonObject(){}

    private static String body;

    public static String getMessage(){
        return body;
    }

    public static void setMessage(String txt){
        body = txt;
    }

}
