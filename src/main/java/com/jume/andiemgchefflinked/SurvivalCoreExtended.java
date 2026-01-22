package com.jume.andiemgchefflinked;

import com.jume.andiemgchefflinked.config.ConfigManager;
import com.jume.andiemgchefflinked.events.ConsumptionListener;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurvivalCoreExtended extends JavaPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(SurvivalCoreExtended.class);
    private static SurvivalCoreExtended instance;
    private ConfigManager configManager;

    public SurvivalCoreExtended(JavaPluginInit init) {
        super(init);
    }

    public static final String VERSION = "1.0.1";

    @Override
    protected void start() {
        if (isOutdated()) {
            LOGGER.warn("A newer version of SurvivalCore-Extended was found. Disabling version " + VERSION);
            return;
        }

        instance = this;
        LOGGER.info("Initializing SurvivalCore-Extended v" + VERSION + "...");

        // 1. Dependency Check (Optional/Skipped for now as API differs)
        // if (!isSurvivalCoreLoaded()) { ... }

        LOGGER.info("SurvivalCore assumed present. hooking into events...");

        // 2. Load Config
        configManager = new ConfigManager();
        configManager.loadConfig();

        // 3. Register Event Listener
        // Using correct API from provided reference
        this.getEventRegistry().registerGlobal(LivingEntityInventoryChangeEvent.class,
                new ConsumptionListener(configManager)::onInventoryChange);

        LOGGER.info("SurvivalCore-Extended enabled successfully.");
    }

    private boolean isOutdated() {
        try {
            java.io.File myJar = new java.io.File(
                    SurvivalCoreExtended.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            java.io.File parentDir = myJar.getParentFile();
            if (parentDir == null || !parentDir.exists())
                return false;

            java.io.File[] candidates = parentDir.listFiles((dir, name) -> name.startsWith("SurvivalCore-Extended")
                    && name.endsWith(".jar") && !name.equals(myJar.getName()));

            if (candidates == null)
                return false;

            for (java.io.File candidate : candidates) {
                String name = candidate.getName();
                String versionPart = extractVersion(name);
                if (versionPart != null && compareVersions(versionPart, VERSION) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to check for duplicate versions: " + e.getMessage());
        }
        return false;
    }

    private String extractVersion(String filename) {
        // Expected format: SurvivalCore-Extended-1.0.1.jar or similar
        // We look for the first digit part
        // Simple regex to grab X.Y.Z
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("(\\d+\\.\\d+\\.\\d+)");
        java.util.regex.Matcher m = p.matcher(filename);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        int length = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < length; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (num1 < num2)
                return -1;
            if (num1 > num2)
                return 1;
        }
        return 0;
    }

    @Override
    protected void shutdown() {
        LOGGER.info("SurvivalCore-Extended disabled.");
    }

    public static SurvivalCoreExtended getInstance() {
        return instance;
    }
}
