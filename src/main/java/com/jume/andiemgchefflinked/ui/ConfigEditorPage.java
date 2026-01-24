package com.jume.andiemgchefflinked.ui;

import com.jume.andiemgchefflinked.config.ConfigManager;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;

/**
 * ConfigEditorPage - Interactive UI for editing food/drink configuration
 * 
 * This page allows operators to modify hunger and thirst values for items
 * in the SurvivalExtendedConfig.json file.
 */
public class ConfigEditorPage extends InteractiveCustomUIPage<ConfigEditorPage.ConfigEventData> {

    private final ConfigManager configManager;

    /**
     * Event data structure for handling UI interactions.
     * Contains the action performed and values from all input fields.
     */
    public static class ConfigEventData {
        public String action; // Button action: "save" or "cancel"
        public String itemName; // Selected item name
        public String hungerValue; // Hunger value as string
        public String thirstValue; // Thirst value as string

        public static final BuilderCodec<ConfigEventData> CODEC = BuilderCodec
                .builder(ConfigEventData.class, ConfigEventData::new)
                .append(
                        new KeyedCodec<>("Action", Codec.STRING),
                        (ConfigEventData o, String v) -> o.action = v,
                        (ConfigEventData o) -> o.action)
                .add()
                .append(
                        new KeyedCodec<>("@ItemName", Codec.STRING),
                        (ConfigEventData o, String v) -> o.itemName = v,
                        (ConfigEventData o) -> o.itemName)
                .add()
                .append(
                        new KeyedCodec<>("@HungerValue", Codec.STRING),
                        (ConfigEventData o, String v) -> o.hungerValue = v,
                        (ConfigEventData o) -> o.hungerValue)
                .add()
                .append(
                        new KeyedCodec<>("@ThirstValue", Codec.STRING),
                        (ConfigEventData o, String v) -> o.thirstValue = v,
                        (ConfigEventData o) -> o.thirstValue)
                .add()
                .build();
    }

    public ConfigEditorPage(@Nonnull PlayerRef playerRef, @Nonnull ConfigManager configManager) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, ConfigEventData.CODEC);
        this.configManager = configManager;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        // Load the UI definition file
        cmd.append("Pages/AndiemgCheffConfig.ui");

        // Bind the Save button event
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#SaveButton",
                new EventData()
                        .append("Action", "save")
                        .append("@ItemName", "#ItemInput.Value")
                        .append("@HungerValue", "#HungerInput.Value")
                        .append("@ThirstValue", "#ThirstInput.Value"));

        // Bind the Cancel button event
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CancelButton",
                new EventData()
                        .append("Action", "cancel"));
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull ConfigEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());

        switch (data.action) {
            case "save":
                handleSave(ref, store, player, data);
                break;

            case "cancel":
                handleCancel(ref, store, player);
                break;
        }
    }

    private void handleSave(Ref<EntityStore> ref, Store<EntityStore> store, Player player, ConfigEventData data) {
        try {
            // Validate item name (required)
            if (data.itemName == null || data.itemName.trim().isEmpty()) {
                playerRef.sendMessage(Message.raw("Error: Please enter an item name!"));
                player.getPageManager().setPage(ref, store, Page.None); // Close UI to prevent hang
                return;
            }

            // Use default values if fields are empty
            float hungerVal = 0.0f;
            float thirstVal = 0.0f;

            // Parse hunger value or use default
            if (data.hungerValue != null && !data.hungerValue.trim().isEmpty()) {
                try {
                    hungerVal = Float.parseFloat(data.hungerValue.trim());
                } catch (NumberFormatException e) {
                    playerRef.sendMessage(Message.raw("Warning: Invalid hunger value, using 0.0"));
                }
            }

            // Parse thirst value or use default
            if (data.thirstValue != null && !data.thirstValue.trim().isEmpty()) {
                try {
                    thirstVal = Float.parseFloat(data.thirstValue.trim());
                } catch (NumberFormatException e) {
                    playerRef.sendMessage(Message.raw("Warning: Invalid thirst value, using 0.0"));
                }
            }

            // Validate ranges
            if (hungerVal < 0 || hungerVal > 1000) {
                playerRef.sendMessage(Message.raw("Warning: Hunger value adjusted to valid range (0-1000)"));
                hungerVal = Math.max(0, Math.min(1000, hungerVal));
            }

            if (thirstVal < 0 || thirstVal > 1000) {
                playerRef.sendMessage(Message.raw("Warning: Thirst value adjusted to valid range (0-1000)"));
                thirstVal = Math.max(0, Math.min(1000, thirstVal));
            }

            // Save the configuration
            configManager.updateItemValues(data.itemName.trim(), hungerVal, thirstVal);
            configManager.saveConfig();

            // Success message
            playerRef.sendMessage(Message.raw("Configuration saved successfully!"));
            playerRef.sendMessage(Message.raw(data.itemName + " -> Hunger: " + hungerVal + ", Thirst: " + thirstVal));

            // Close the UI
            player.getPageManager().setPage(ref, store, Page.None);

        } catch (Exception e) {
            playerRef.sendMessage(Message.raw("Error saving configuration: " + e.getMessage()));
            player.getPageManager().setPage(ref, store, Page.None); // Always close to prevent hang
        }
    }

    private void handleCancel(Ref<EntityStore> ref, Store<EntityStore> store, Player player) {
        playerRef.sendMessage(Message.raw("Configuration editing cancelled."));
        player.getPageManager().setPage(ref, store, Page.None);
    }
}
