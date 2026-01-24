package com.jume.andiemgchefflinked.commands;

import com.jume.andiemgchefflinked.SurvivalCoreExtended;
import com.jume.andiemgchefflinked.ui.ConfigInfoPage;
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
 * Command to view current configuration values
 * Usage: /cheffinfo
 */
public class CheffInfoCommand extends AbstractPlayerCommand {

    public CheffInfoCommand() {
        super("cheffinfo", "View Andiemg Cheff configuration");
    }

    @Override
    protected void execute(
            @Nonnull CommandContext context,
            @Nonnull Store<EntityStore> store,
            @Nonnull Ref<EntityStore> ref,
            @Nonnull PlayerRef playerRef,
            @Nonnull World world) {
        Player player = store.getComponent(ref, Player.getComponentType());

        // Open the info UI
        ConfigInfoPage page = new ConfigInfoPage(
                playerRef,
                SurvivalCoreExtended.getInstance().getConfigManager());
        player.getPageManager().openCustomPage(ref, store, page);
    }
}
