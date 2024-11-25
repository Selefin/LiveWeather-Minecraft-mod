package com.liveweather.liveweathermod;

import net.minecraft.server.level.ServerLevel;

public class WeatherService {
    private String city;
    private String weather;
    private int temperature;

    private void getAndTransformData() {
        WeatherAPIClient weatherAPI = new WeatherAPIClient();
        String data = weatherAPI.findWeather(weatherAPI.getCity());
        if (data.contains("Error")) {
            System.out.println("Error: " + data.split("error\":\\{")[1].split("}")[0]);
            return;
        }
        this.city = data.split("name\":\"")[1].split("\",\"")[0];
        this.weather = data.split("weather_descriptions\":\\[\"")[1].split("\"]")[0];
        this.temperature = Integer.parseInt(data.split("temperature\":")[1].split(",")[0]);
    }

    public String toString() {
        return "Weather in " + this.city + ": " + this.weather + ", " + this.temperature + "°C";
    }

    public String printWeather() {
        getAndTransformData();
        return this.toString();
    }

    public String getWeather() {
        return this.weather;
    }

    public static void applyWeather(ServerLevel world, String weather) {
        switch(weather.split(",")[0]) {
            case "Clear", "Sunny", "Partly Cloudy", "Cloudy", "Fog", "Haze", "Mist", "Windy", "Partly cloudy":
                world.setWeatherParameters(444 * 60 * 20, 0, false, false);
                break;
            case "Rain", "Heavy Rain", "Snow", "Light Snow", "Heavy Snow", "Drizzle", "Sleet", "Light Rain":
                world.setWeatherParameters(0, 444 * 60 * 20, true, false);
                break;
            case "Thunderstorm", "Storm":
                world.setWeatherParameters(0, 444 * 60 * 20, true, true);
                break;
            default:
                System.err.println("Error: Unknown weather");
                break;
        }
    }
}
