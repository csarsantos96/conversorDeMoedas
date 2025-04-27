package org.conversorDeMoedas;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConversorMoedas {

    // 1) Só a chave, sem URL
    private static final String API_KEY  = "971a2d142af2e66193aa00e0";
    // 2) Base da ExchangeRate-API v6
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== CONVERSOR DE MOEDAS ===");
        System.out.println("1  – USD → BRL");
        System.out.println("2  – EUR → BRL");
        System.out.println("3  – BRL → USD");
        System.out.println("4  – BRL → EUR");
        System.out.println("5  – USD → EUR");
        System.out.println("6  – EUR → USD");
        System.out.println("7  – ARS → BRL");
        System.out.println("8  – BRL → ARS");
        System.out.println("9  – JPY → BRL");
        System.out.println("10 – BRL → JPY");
        System.out.println("11 – CNY → BRL");
        System.out.println("12 – BRL → CNY");
        System.out.println("13 – RUB → BRL");
        System.out.println("14 – BRL → RUB");
        System.out.print("Escolha uma opção (1–14): ");
        int opcao = sc.nextInt();

        String from, to;
        switch (opcao) {
            case 1:  from = "USD"; to = "BRL"; break;
            case 2:  from = "EUR"; to = "BRL"; break;
            case 3:  from = "BRL"; to = "USD"; break;
            case 4:  from = "BRL"; to = "EUR"; break;
            case 5:  from = "USD"; to = "EUR"; break;
            case 6:  from = "EUR"; to = "USD"; break;
            case 7:  from = "ARS"; to = "BRL"; break;
            case 8:  from = "BRL"; to = "ARS"; break;
            case 9:  from = "JPY"; to = "BRL"; break;
            case 10: from = "BRL"; to = "JPY"; break;
            case 11: from = "CNY"; to = "BRL"; break;
            case 12: from = "BRL"; to = "CNY"; break;
            case 13: from = "RUB"; to = "BRL"; break;
            case 14: from = "BRL"; to = "RUB"; break;
            default:
                System.out.println("Opção inválida. Encerrando.");
                sc.close();
                return;
        }

        System.out.print("Informe o valor em " + from + ": ");
        double amount = sc.nextDouble();
        sc.close();

        double resultado = convert(from, to, amount);
        System.out.printf(">>> %.2f %s = %.2f %s%n", amount, from, resultado, to);
        System.out.println("Programa encerrado. Até a próxima!");
    }

    private static double convert(String from, String to, double amount) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // 4) força ponto decimal
            String amt = String.format(Locale.US, "%.4f", amount);

            // 3) formato correto do endpoint
            String endpoint = String.format(
                    "%s/%s/pair/%s/%s/%s",
                    BASE_URL, API_KEY, from, to, amt
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body())
                    .getAsJsonObject();

            // checa sucesso
            if (!"success".equals(json.get("result").getAsString())) {
                System.err.println("API retornou erro: " + json);
                return 0;
            }

            // 5) captura o valor já convertido
            return json.get("conversion_result").getAsDouble();

        } catch (Exception e) {
            System.err.println("Falha na conversão: " + e.getMessage());
            return 0;
        }
    }
}
