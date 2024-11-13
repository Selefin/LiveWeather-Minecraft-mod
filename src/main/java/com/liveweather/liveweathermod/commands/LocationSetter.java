package com.liveweather.liveweathermod.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import java.io.IOException;
import java.io.PrintWriter;

public class LocationSetter {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("set-location")
                .then(Commands.argument("location", StringArgumentType.string())
                        .executes(LocationSetter::execute)));
    }

    private static int execute(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String location = StringArgumentType.getString(context, "location");
        if(location.equals("reset")) {
            location = "Unknown";
        }
        source.sendSystemMessage(Component.literal("Setting location to " + location));
        writeLocationToFile(location);
        return Command.SINGLE_SUCCESS;
    }

    private static void writeLocationToFile(String location) {
        try (PrintWriter writer = new PrintWriter("D:\\Documents (HDD)\\Cours\\3A_V2\\POO_Java\\Final Project\\LiveWeather - Minecraft mod\\src\\main\\resources\\location.txt")) {
            if(location.equals("Unknown")) {
                writer.println("Unknown");
            } else {
                writer.println("Location: " + location);
            }
        } catch (IOException e) {
            System.err.println("Error while writing the file : " + e);
        }
    }
}
