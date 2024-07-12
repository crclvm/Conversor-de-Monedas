import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRateClient {

    private static final String API_KEY = "83a50c0e2190acd8034c2581"; // Ingresa aqui tu API KEY
    private static final String URLBase = "https://v6.exchangerate-api.com/v6/" + API_KEY;

    public static Map<String, String> getSupportedCurrencies() throws IOException, InterruptedException {
        String url = URLBase + "/codes";

        // Crea un cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Crea una solicitud HTTP GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Envia la solicitud y obtiene la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, String> currencies = new HashMap<>();
        if (response.statusCode() == 200) {
            String responseBody = response.body();

            // Parsea la respuesta JSON con Gson
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray currencyArray = jsonObject.getAsJsonArray("supported_codes");

            for (int i = 0; i < currencyArray.size(); i++) {
                JsonArray currency = currencyArray.get(i).getAsJsonArray();
                String code = currency.get(0).getAsString();
                String name = currency.get(1).getAsString();
                currencies.put(code, name);
            }
        } else {
            throw new IOException("Error en la solicitud... " + response.statusCode());
        }

        return currencies;
    }

    public static double getExchangeRate(String fromCurrency, String toCurrency) throws IOException, InterruptedException {
        String url = URLBase + "/pair/" + fromCurrency + "/" + toCurrency;

        // Crea un cliente HTTP
        HttpClient client = HttpClient.newHttpClient();

        // Crea una solicitud HTTP GET
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        // Envia la solicitud y obtiene la respuesta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();

            // Parsea la respuesta JSON con Gson
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            return jsonObject.get("conversion_rate").getAsDouble();
        } else {
            throw new IOException("Error en la solicitud... " + response.statusCode());
        }
    }
}