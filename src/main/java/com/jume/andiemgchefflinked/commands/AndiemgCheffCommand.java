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
import com.hypixel.hytale.server.core.Message;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * Command to open the configuration UI.
 * Only accessible by operators.
 * Usage: /andiemgcheff
 */
public class AndiemgCheffCommand extends AbstractPlayerCommand {

    public AndiemgCheffCommand() {
        super("andiemgcheff", "Opens the Andiemg Cheff configuration UI");
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
