package com.jume.andiemgchefflinked.commands;

import com.jume.andiemgchefflinked.SurvivalCoreExtended;
import com.jume.andiemgchefflinked.ui.ConfigEditorPage;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import javax.annotation.Nonnull;

/**
 * Alias command for /andiemgcheff
 * Usage: /cheff
 */
public class CheffCommand extends AbstractPlayerCommand {

    public CheffCommand() {
        super("cheff", "Opens the Andiemg Cheff configuration UI");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        // Open the configuration UI
        // Note: Permission check should be configured in the server's permission system
        ConfigEditorPage page = new ConfigEditorPage(
                playerRef,
                SurvivalCoreExtended.getInstance().getConfigManager());
        player.getPageManager().openCustomPage(ref, store, page);
    }
}
