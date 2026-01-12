//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import com.google.gson.JsonElement;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        String apiKey = "e911aa02468b117013783dbd";
        String direccion = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(direccion))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        String jsonLimpio = response.body().trim();

        JsonElement jsonElement = JsonParser.parseString(jsonLimpio);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("""
            *********************************
            Bienvenido/a al Conversor de Monedas [$]

            1) USD a MXN
            2) MXN a USD
            3) USD a EUR
            4) EUR a USD
            5) Salir

            Elija una opción válida:
            *********************************
            """);

            opcion = scanner.nextInt();

            if (opcion == 5) {
                System.out.println("Gracias por usar el conversor");
                break;
            }

            System.out.print("Ingrese la cantidad a convertir: ");
            double cantidad = scanner.nextDouble();

            double resultado = 0;
            String monedaOrigen = "";
            String monedaDestino = "";

            switch (opcion) {
                case 1 -> {
                    monedaOrigen = "dólares";
                    monedaDestino = "pesos mexicanos";
                    resultado = convertir(cantidad, obtenerTasa(conversionRates, "MXN"));
                }
                case 2 -> {
                    monedaOrigen = "pesos mexicanos";
                    monedaDestino = "dólares";
                    resultado = cantidad / obtenerTasa(conversionRates, "MXN");
                }
                case 3 -> {
                    monedaOrigen = "dólares";
                    monedaDestino = "euros";
                    resultado = convertir(cantidad, obtenerTasa(conversionRates, "EUR"));
                }
                case 4 -> {
                    monedaOrigen = "euros";
                    monedaDestino = "dólares";
                    resultado = cantidad / obtenerTasa(conversionRates, "EUR");
                }
                default -> {
                    System.out.println("Opción no válida");
                    continue;
                }
            }

            System.out.println(
                    "El valor de " + cantidad + " " + monedaOrigen +
                            " equivale a " + String.format("%.2f", resultado) +
                            " " + monedaDestino
            );
            System.out.println();

        } while (opcion != 5);

        scanner.close();
    }

    public static double obtenerTasa(JsonObject conversionRates, String monedaDestino) {
        return conversionRates.get(monedaDestino).getAsDouble();
    }

    public static double convertir(double cantidad, double tasa) {
        return cantidad * tasa;
    }
}





