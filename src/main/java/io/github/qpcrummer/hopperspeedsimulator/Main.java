package io.github.qpcrummer.hopperspeedsimulator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

import static io.github.qpcrummer.hopperspeedsimulator.commands.Command.registerCommand;


public class Main implements ModInitializer {

    public static String cfgver;
    public static int ticks;
    public static int items;
    public static boolean unsafe;
    public static boolean first = true;

    public static Properties properties = new Properties();

    @Override
    public void onInitialize() {

        try {
            registerCommand();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        var path = FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties");

        if (Files.notExists(path)) {
            mkfile();
            System.out.println("Creating Hopper Speed Simulator config");
        } else {
            loadcfg();
            cfgver = properties.getProperty("config-version");
            if (!(Objects.equals(cfgver, "1.2"))) {
                mkfile();
                System.out.println("Updating Hopper Speed Simulator config");
            } else {
                parse();
            }
        }
    }

    public void mkfile() {
        try (OutputStream output = Files.newOutputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
            if (!properties.contains("config-version")) {
                properties.setProperty("config-version", "1.2");
            }
            if (!properties.contains("ticks-per-transfer")) {
                properties.setProperty("ticks-per-transfer", "8");
            }
            if (!properties.contains("items-per-transfer")) {
                properties.setProperty("items-per-transfer", "1");
            }
            if (!properties.contains("unsafe-speed")) {
                properties.setProperty("unsafe-speed", "false");
            }
            properties.store(output, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        parse();
    }

    public static void loadcfg() {
        try (InputStream input = Files.newInputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void parse() {
        cfgver = properties.getProperty("config-version");
        ticks = Integer.parseInt(properties.getProperty("ticks-per-transfer"));
        items = Integer.parseInt(properties.getProperty("items-per-transfer"));
        unsafe = Boolean.parseBoolean(properties.getProperty("unsafe-speed"));
    }
}
