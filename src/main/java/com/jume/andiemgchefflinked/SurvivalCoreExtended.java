package com.jume.andiemgchefflinked;

import com.jume.andiemgchefflinked.config.ConfigManager;
import com.jume.andiemgchefflinked.events.ConsumptionListener;
import com.jume.andiemgchefflinked.commands.AndiemgCheffCommand;
import com.jume.andiemgchefflinked.commands.CheffCommand;
import com.jume.andiemgchefflinked.commands.CheffInfoCommand;
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

    public static final String VERSION = "1.0.4";

    @Override
    protected void start() {
        instance = this;
        LOGGER.info("Initializing SurvivalCore-Extended v" + VERSION + "...");

        // 1. Dependency Check (Optional/Skipped for now as API differs)
        // if (!isSurvivalCoreLoaded()) { ... }

        LOGGER.info("SurvivalCore assumed present. hooking into events...");

        // 2. Load Config
        configManager = new ConfigManager();
        configManager.loadConfig();

        // 3. Register Commands
        this.getCommandRegistry().registerCommand(new AndiemgCheffCommand());
        this.getCommandRegistry().registerCommand(new CheffCommand());
        this.getCommandRegistry().registerCommand(new CheffInfoCommand());
        LOGGER.info("Registered commands: /andiemgcheff, /cheff, and /cheffinfo");

        // 4. Register Event Listener
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

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
