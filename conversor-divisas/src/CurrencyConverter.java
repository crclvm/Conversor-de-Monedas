import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String[] targetCurrency = {"EUR", "MXN", "BRL", "ARS", "GBP"};
    private static final String[] currencyNames = {"Euros", "Pesos Mexicanos", "Reales Brasilenos", "Pesos Argentinos", "Libras Inglesas"};
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00");

    public static double convert(String fromCurrency, String toCurrency, double amount) throws IOException, InterruptedException {
        double exchangeRate = ExchangeRateClient.getExchangeRate(fromCurrency, toCurrency);
        return amount * exchangeRate;
    }

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        try {
            Map<String, String> currencies = ExchangeRateClient.getSupportedCurrencies();
            System.out.println("Divisas soportadas:");

            System.out.println( "ARS- Peso Argentino\n" +
                                "BOB - Boliviano boliviano\n" +
                                "BRL - Real Brasileno\n" +
                                "CLP - Peso Chileno\n" +
                                "COP - Peso Colombiano\n" +
                                "USD - Dollar Estadounidense\n");

            System.out.print("Ingrese el codigo de la moneda base: ");
            String fromCurrency = teclado.nextLine().toUpperCase();

            System.out.print("Ingrese la cantidad: ");
            double amount = teclado.nextDouble();

            System.out.println("\n-----------------------------\n");

            // Convertir la divisa seleccionada primero a USD
            double amountInUSD = convert(fromCurrency, "USD", amount);
            System.out.printf("%.2f %s equivale a %.2f USD%n", amount, fromCurrency, amountInUSD);

            // Convertir a las divisas seleccionadas
            for (int i = 0; i < targetCurrency.length; i++) {
                double convertedAmount = convert(fromCurrency, targetCurrency[i], amount);
                System.out.printf("%.2f %s vale %.2f %s%n", amount, fromCurrency, convertedAmount, currencyNames[i]);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ha ocurrido un error durante la conversion...");
            e.printStackTrace();
        }
    }
}