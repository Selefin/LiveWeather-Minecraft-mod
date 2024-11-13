package com.liveweather.liveweathermod.commands;

import com.liveweather.liveweathermod.WeatherService;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class WeatherCommandDisplayHandler {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("get-weather")
                .executes(WeatherCommandDisplayHandler::execute));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendSystemMessage(Component.literal("Searching for weather..."));
        WeatherService weatherService = new WeatherService();
        String chatWeather = weatherService.printWeather();
        source.sendSystemMessage(Component.nullToEmpty(chatWeather));
        ServerLevel world = source.getLevel();
        WeatherService.applyWeather(world, weatherService.getWeather(), weatherService.getPrecipitation());
        return Command.SINGLE_SUCCESS;
    }
}
