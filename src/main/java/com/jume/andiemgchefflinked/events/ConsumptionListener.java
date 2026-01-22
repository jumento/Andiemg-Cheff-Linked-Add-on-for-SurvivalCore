package com.jume.andiemgchefflinked.events;

import com.jume.andiemgchefflinked.config.ConfigManager;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.entity.LivingEntityInventoryChangeEvent;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackSlotTransaction;
import com.hypixel.hytale.server.core.inventory.transaction.ItemStackTransaction;
import com.hypixel.hytale.server.core.inventory.transaction.SlotTransaction;
import com.hypixel.hytale.server.core.inventory.transaction.Transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumptionListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumptionListener.class);
    private final ConfigManager configManager;

    public ConsumptionListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void onInventoryChange(LivingEntityInventoryChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Transaction transaction = event.getTransaction();

        if (transaction == null || !matchesConsumption(transaction)) {
            return;
        }

        String itemId = extractItemId(transaction);
        if (itemId == null)
            return;

        if (configManager.hasItem(itemId)) {
            float[] stats = configManager.getStats(itemId);

            float thirstInfo = stats[1];

            // Feedback removed as per user request
            // player.sendMessage(Message.raw("SurvivalExtended: Consumed " + itemId + "
            // (H:+" + hungerInfo + ", T:+" + thirstInfo + ")"));

            // Apply Thirst Logic Here (Add your ThirstComponent integration or placeholder)
            applyThirst(player, thirstInfo);
        }
    }

    private boolean matchesConsumption(Transaction transaction) {
        // Based on reference mod logic: filter out moves, check for quantity -1
        String info = transaction.toString();
        if (info.contains("MoveTransaction"))
            return false; // Drops/Moves

        if (transaction instanceof ItemStackSlotTransaction) {
            ItemStackSlotTransaction t = (ItemStackSlotTransaction) transaction;
            return checkSlotConsumption(t.getSlotBefore(), t.getSlotAfter());
        }

        if (transaction instanceof ItemStackTransaction) {
            ItemStackTransaction stackTrans = (ItemStackTransaction) transaction;
            for (Object obj : stackTrans.getSlotTransactions()) {
                if (obj instanceof SlotTransaction) {
                    SlotTransaction t = (SlotTransaction) obj;
                    if (checkSlotConsumption(t.getSlotBefore(), t.getSlotAfter())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkSlotConsumption(ItemStack before, ItemStack after) {
        if (before == null)
            return false;
        int qtyBefore = before.getQuantity();
        int qtyAfter = (after != null) ? after.getQuantity() : 0;
        return (qtyBefore - qtyAfter) == 1;
    }

    private String extractItemId(Transaction transaction) {
        // Quick extraction from the slot that changed
        if (transaction instanceof ItemStackSlotTransaction) {
            ItemStackSlotTransaction t = (ItemStackSlotTransaction) transaction;
            if (t.getSlotBefore() != null)
                return t.getSlotBefore().getItemId();
        }
        if (transaction instanceof ItemStackTransaction) {
            ItemStackTransaction stackTrans = (ItemStackTransaction) transaction;
            for (Object obj : stackTrans.getSlotTransactions()) {
                if (obj instanceof SlotTransaction) {
                    SlotTransaction t = (SlotTransaction) obj;
                    if (checkSlotConsumption(t.getSlotBefore(), t.getSlotAfter())) {
                        return t.getSlotBefore().getItemId();
                    }
                }
            }
        }
        return null;
    }

    private void applyThirst(Player player, float amount) {
        try {
            // 1. Get the EasyHunger plugin instance via Reflection/Static helper
            Class<?> easyHungerClass = Class.forName("com.haas.easyhunger.EasyHunger");
            Object easyHungerInstance = easyHungerClass.getMethod("get").invoke(null);

            // 2. Get the ThirstComponentType
            java.lang.reflect.Method getThirstCompTypeMethod = easyHungerClass.getMethod("getThirstComponentType");
            Object thirstComponentType = getThirstCompTypeMethod.invoke(easyHungerInstance);

            // 3. Get the entity's store
            Object store = player.getReference().getStore();

            // 4. Access the ThirstComponent from the store
            // Assuming store.getComponent(entityRef, componentType)
            // Method signature: getComponent(Ref<EntityStore>, ComponentType<EntityStore,
            // T>)
            java.lang.reflect.Method getComponentMethod = store.getClass().getMethod("getComponent",
                    com.hypixel.hytale.component.Ref.class, com.hypixel.hytale.component.ComponentType.class);
            Object thirstComponent = getComponentMethod.invoke(store, player.getReference(), thirstComponentType);

            if (thirstComponent != null) {
                // 5. Call drink(amount) on the component
                java.lang.reflect.Method drinkMethod = thirstComponent.getClass().getMethod("drink", float.class);
                drinkMethod.invoke(thirstComponent, amount);
                LOGGER.info(
                        "Applied " + amount + " thirst to " + player.getDisplayName() + " via EasyHunger integration.");
            } else {
                LOGGER.warn("ThirstComponent not found on player: " + player.getDisplayName());
            }

        } catch (Exception e) {
            LOGGER.error("Failed to integrate with EasyHunger: " + e.toString());
            // Safe fallback logging
            LOGGER.debug("Stack trace:", e);
        }
    }
}
