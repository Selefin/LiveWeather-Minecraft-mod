package com.liveweather.liveweathermod.commands;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Scanner;

public class WeatherAPIClient {
    protected String access_key = "4f599244af6605b3eef504907eb5287e";

    private String country;
    private String city;

    public WeatherAPIClient() {
        updateCity();
    }

    public String getCity() {
        return this.city;
    }

    public void updateCity() {
        Locale userLocale = Locale.getDefault();
        this.country = userLocale.getDisplayCountry(Locale.ENGLISH);
        this.city = findCity(this.country);
    }

    public String findCity(String country) {
        try {
            File file = new File("C:\\Users\\melvy\\Documents\\_\\LiveWeather\\Capitals.txt");
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
            System.err.println("Error: Unknown city");
            return "Error: Unknown city";
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.weatherstack.com/current?access_key=" + this.access_key + "&query=" + city))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return "Unknown";
        }
    }
}
