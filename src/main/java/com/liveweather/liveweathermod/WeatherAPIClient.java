package com.liveweather.liveweathermod;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Scanner;
import io.github.cdimascio.dotenv.Dotenv;

public class WeatherAPIClient {

    private String city;
    private Dotenv dotenv;

    public WeatherAPIClient() {
        this.dotenv = null;
        this.dotenv = Dotenv.configure()
                .directory("..\\src\\main\\resources")
                .load();

        File file = new File("..\\src\\main\\resources\\location.txt");

        try {
            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            String cityName = line.replace("_", " ");
            if(cityName.equals("Unknown")) {
                updateCity();
            }
            else {
                this.city = cityName.split("Location: ")[1];
            }
        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
            updateCity();
        }
    }

    public String getCity() {
        return this.city;
    }

    public void updateCity() {
        Locale userLocale = Locale.getDefault();
        String country = userLocale.getDisplayCountry(Locale.ENGLISH);
        this.city = findCity(country);
    }

    public String findCity(String country) {
        if(country.equals("Unknown")) {
            System.err.println("Error: The country could not be found");
            return "Unknown";
        }
        try {
            File file = new File("..\\src\\main\\resources\\Capitals.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(country)) {
                    return line.split(" - ")[1];
                }
            }
        } catch (IOException e) {
            System.err.println("File not found: " + e.getMessage());
            return "Unknown";
        }
        return "Unknown";
    }

    public String findWeather(String city) {
        if (city.equals("Unknown")) {
            System.err.println("Error: Cannot find weather for unknown city");
            return "Error: Cannot find weather for unknown city";
        }

        String access_key = this.dotenv.get("WEATHERSTACK_API_KEY");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.weatherstack.com/current?access_key=" + access_key + "&query=" + city))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}
