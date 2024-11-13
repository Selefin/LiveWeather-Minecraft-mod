package com.liveweather.liveweathermod;

import net.minecraft.server.level.ServerLevel;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Scanner;

public class WeatherAPIClient {
    protected String access_key = "your_weatherstack_api_key_here";

    private String city;

    public WeatherAPIClient() {
        File file = new File("D:\\Documents (HDD)\\Cours\\3A_V2\\POO_Java\\Final Project\\LiveWeather - Minecraft mod\\src\\main\\resources\\location.txt");
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
            System.err.println("Error: Unknown country");
            return "Unknown";
        }
        try {
            File file = new File("D:\\Documents (HDD)\\Cours\\3A_V2\\POO_Java\\Final Project\\LiveWeather - Minecraft mod\\src\\main\\resources\\Capitals.txt");
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

    public static void applyWeather(ServerLevel world, String weather, int precipitation) {
        switch(weather) {
            case "Clear", "Sunny", "Partly Cloudy", "Cloudy", "Fog", "Haze", "Mist", "Windy":
                world.setWeatherParameters(12000, 0, false, false);
                break;
            case "Rain", "Heavy Rain", "Snow", "Light Snow", "Heavy Snow", "Drizzle", "Sleet", "Light Rain":
                world.setWeatherParameters(0, 12000, true, false);
                break;
            case "Thunderstorm", "Storm":
                world.setWeatherParameters(0, 12000, true, true);
                break;
            default:
                if (precipitation > 0) {
                    world.setWeatherParameters(0, 12000, true, false);
                }
                else {
                    world.setWeatherParameters(12000, 0, false, false);
                }
                break;
        }
    }
}
