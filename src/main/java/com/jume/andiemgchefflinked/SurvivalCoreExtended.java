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

    @Override
    protected void start() {
        instance = this;
        LOGGER.info("Initializing SurvivalCore-Extended...");

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

    @Override
    protected void shutdown() {
        LOGGER.info("SurvivalCore-Extended disabled.");
    }

    public static SurvivalCoreExtended getInstance() {
        return instance;
    }
}
