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
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;
import java.util.Map;

/**
 * ConfigInfoPage - Displays current configuration values
 */
public class ConfigInfoPage extends InteractiveCustomUIPage<ConfigInfoPage.InfoEventData> {

    private final ConfigManager configManager;

    public static class InfoEventData {
        public String action;

        public static final BuilderCodec<InfoEventData> CODEC = BuilderCodec
                .builder(InfoEventData.class, InfoEventData::new)
                .append(
                        new KeyedCodec<>("Action", Codec.STRING),
                        (InfoEventData o, String v) -> o.action = v,
                        (InfoEventData o) -> o.action)
                .add()
                .build();
    }

    public ConfigInfoPage(@Nonnull PlayerRef playerRef, @Nonnull ConfigManager configManager) {
        super(playerRef, CustomPageLifetime.CanDismissOrCloseThroughInteraction, InfoEventData.CODEC);
        this.configManager = configManager;
    }

    @Override
    public void build(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull UICommandBuilder cmd,
            @Nonnull UIEventBuilder evt,
            @Nonnull Store<EntityStore> store) {
        // Load the UI file
        cmd.append("Pages/AndiemgCheffInfo.ui");

        // Build configuration info text
        StringBuilder configText = new StringBuilder();
        Map<String, float[]> allItems = configManager.getAllItems();

        if (allItems.isEmpty()) {
            configText.append("No items configured yet.\n\n");
            configText.append("Use /cheff to add configurations.");
        } else {
            for (Map.Entry<String, float[]> entry : allItems.entrySet()) {
                String itemName = entry.getKey();
                float[] values = entry.getValue();
                configText.append(String.format("%-35s  H: %6.1f  T: %6.1f\n",
                        itemName, values[0], values[1]));
            }

            configText.append("\n\nTotal items configured: ").append(allItems.size());
        }

        // Set the text content
        cmd.set("#ConfigInfo.Text", configText.toString());

        // Bind the Close button event
        evt.addEventBinding(
                CustomUIEventBindingType.Activating,
                "#CloseButton",
                new EventData()
                        .append("Action", "close"));
    }

    @Override
    public void handleDataEvent(
            @Nonnull Ref<EntityStore> ref,
            @Nonnull Store<EntityStore> store,
            @Nonnull InfoEventData data) {
        Player player = store.getComponent(ref, Player.getComponentType());

        if ("close".equals(data.action)) {
            player.getPageManager().setPage(ref, store, Page.None);
        }
    }
}
