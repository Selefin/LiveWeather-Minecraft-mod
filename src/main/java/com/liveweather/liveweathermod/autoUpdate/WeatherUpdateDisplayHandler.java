package com.liveweather.liveweathermod.autoUpdate;

import com.liveweather.liveweathermod.WeatherService;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import java.util.Objects;

public class WeatherUpdateDisplayHandler {
    private int tickCounter;

    public void updateWeather() {
        int WEATHER_CHANGE_INTERVAL = 444 * 60 * 20;
        if (this.tickCounter >= WEATHER_CHANGE_INTERVAL) {
            this.tickCounter = 0;
            ServerLevel world = Objects.requireNonNull(Minecraft.getInstance().getSingleplayerServer()).getLevel(ServerLevel.OVERWORLD);
            assert world != null;
            world.players().forEach(player -> player.sendSystemMessage(Component.literal("Updating the weather...")));
            WeatherService weatherService = new WeatherService();
            String chatWeather = weatherService.printWeather();
            world.players().forEach(player -> player.sendSystemMessage(Component.nullToEmpty(chatWeather)));
            WeatherService.applyWeather(world, weatherService.getWeather());
        } else {
            this.tickCounter++;
        }
    }

    public WeatherUpdateDisplayHandler() {
        this.tickCounter = 0;
        ServerLevel world = Objects.requireNonNull(Minecraft.getInstance().getSingleplayerServer()).getLevel(ServerLevel.OVERWORLD);
        assert world != null;
        world.players().forEach(player -> player.sendSystemMessage(Component.literal("Updating the weather...")));
        WeatherService weatherService = new WeatherService();
        String chatWeather = weatherService.printWeather();
        world.players().forEach(player -> player.sendSystemMessage(Component.nullToEmpty(chatWeather)));
        WeatherService.applyWeather(world, weatherService.getWeather());
    }
}
