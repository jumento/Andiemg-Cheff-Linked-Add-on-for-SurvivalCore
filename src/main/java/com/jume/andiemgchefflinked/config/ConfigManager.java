package com.jume.andiemgchefflinked.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Type;

public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
    private static final String CONFIG_FILE_NAME = "SurvivalExtendedConfig.json";

    // Map<ItemName, [Hunger, Thirst]>
    private Map<String, float[]> consumptionValues = new HashMap<>();

    public void loadConfig() {
        File dataFolder = new File("mods/Andiemg Cheff Linked");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        File configFile = new File(dataFolder, CONFIG_FILE_NAME);
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, float[]>>() {
        }.getType();

        // 1. Load Default Config from JAR
        Map<String, float[]> defaults = new HashMap<>();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (in != null) {
                java.io.InputStreamReader isr = new java.io.InputStreamReader(in);
                defaults = gson.fromJson(isr, type);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load default config from JAR", e);
        }

        // 2. Load User Config if exists
        Map<String, float[]> userConfig = new HashMap<>();
        boolean needsSave = false;

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                userConfig = gson.fromJson(reader, type);
                if (userConfig == null)
                    userConfig = new HashMap<>();
            } catch (IOException e) {
                LOGGER.error("Could not load user configuration file!", e);
            }
        } else {
            needsSave = true; // New file
        }

        // 3. Merge: Add missing keys from Default to User
        if (defaults != null) {
            for (Map.Entry<String, float[]> entry : defaults.entrySet()) {
                if (!userConfig.containsKey(entry.getKey())) {
                    userConfig.put(entry.getKey(), entry.getValue());
                    needsSave = true;
                    LOGGER.info("Updated config: Added new item '{}'", entry.getKey());
                }
            }
        }

        // 4. Update memory and Save if needed
        this.consumptionValues = userConfig;

        if (needsSave) {
            try (java.io.FileWriter writer = new java.io.FileWriter(configFile)) {
                // Pretty print for user friendliness
                gson = new Gson().newBuilder().setPrettyPrinting().create();
                gson.toJson(consumptionValues, writer);
                LOGGER.info("Saved configuration to disk.");
            } catch (IOException e) {
                LOGGER.error("Failed to save configuration!", e);
            }
        }

        LOGGER.info("Loaded {} items from configuration.", consumptionValues.size());
        validateItems();
    }

    private void validateItems() {
        // Validation against Game Registry
        // This prevents runtime errors if config has typos
        // Note: Registry API access requires finding the correct method in
        // com.hypixel.hytale...
        // For now, we skip verification to prevent compilation errors.
        LOGGER.info("Skipping item registry validation (API unavailable). Assumed valid.");
    }

    public float[] getStats(String itemName) {
        return consumptionValues.get(itemName);
    }

    public boolean hasItem(String itemName) {
        return consumptionValues.containsKey(itemName);
    }
}
