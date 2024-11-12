package com.liveweather.liveweathermod.commands;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class WeatherDisplayHandler {

    private static final int WEATHER_CHANGE_INTERVAL = 1 * 60 * 20;
    private static int tickCounter = 0;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("getweather")
                .executes(WeatherDisplayHandler::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSystemMessage(Component.literal("Searching for weather..."));
        WeatherService weatherService = new WeatherService();
        String chatWeather = weatherService.printWeather();
        source.sendSystemMessage(Component.nullToEmpty(chatWeather));
        ServerLevel world = source.getLevel();
        applyWeather(world, weatherService.getWeather(), weatherService.getPrecipitation());
        return Command.SINGLE_SUCCESS;
    }

    public static void applyWeather(ServerLevel world, String weather, int precipitation) {
        switch(weather) {
            case "Clear", "Sunny", "Partly Cloudy", "Cloudy", "Fog", "Haze", "Mist", "Windy":
                world.setWeatherParameters(12000, 0, false, false);
                break;
            case "Rain", "Heavy Rain", "Snow", "Light Snow", "Heavy Snow", "Drizzle", "Sleet":
                world.setWeatherParameters(0, 12000, true, false);
                break;
            case "Thunderstorm", "Storm":
                world.setWeatherParameters(0, 12000, true, true);
                break;
            default:
                if (precipitation > 0) {
                    world.setWeatherParameters(0, 12000, true, false);
                } else {
                    world.setWeatherParameters(12000, 0, false, false);
                }
                break;
        }
    }
}
