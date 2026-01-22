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

    // Reflection Cache
    private Class<?> easyHungerClass;
    private Object easyHungerInstance;
    private java.lang.reflect.Method getThirstCompTypeMethod;
    private Object thirstComponentType;
    private java.lang.reflect.Method getComponentMethod;
    private java.lang.reflect.Method drinkMethod;
    private boolean reflectionInitialized = false;

    public ConsumptionListener(ConfigManager configManager) {
        this.configManager = configManager;
        initializeReflection();
    }

    private void initializeReflection() {
        try {
            easyHungerClass = Class.forName("com.haas.easyhunger.EasyHunger");
            java.lang.reflect.Method getMethod = easyHungerClass.getMethod("get");
            easyHungerInstance = getMethod.invoke(null);

            getThirstCompTypeMethod = easyHungerClass.getMethod("getThirstComponentType");
            thirstComponentType = getThirstCompTypeMethod.invoke(easyHungerInstance);

            // We can't easily cache 'getComponentMethod' cleanly without an instance to get
            // the class from,
            // but we can try to guess or just look it up once if we know the interface.
            // For safety, we'll look up getComponentMethod lazily or just cache the name
            // lookup if possible.
            // However, 'drink' method is on the component class. We need the component
            // class first.
            // Since we don't have a component instance yet, we can't fully cache the
            // 'drink' method here
            // unless we know the exact class name of the component.
            // But we can certainly cache the EasyHunger instance and the ComponentType.

            reflectionInitialized = true;
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Reflection for EasyHunger: " + e.toString());
        }
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

            // Apply Thirst Logic
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
        if (!reflectionInitialized)
            return;

        try {
            // 3. Get the entity's store
            Object store = player.getReference().getStore();

            // 4. Access the ThirstComponent from the store
            if (getComponentMethod == null) {
                getComponentMethod = store.getClass().getMethod("getComponent",
                        com.hypixel.hytale.component.Ref.class, com.hypixel.hytale.component.ComponentType.class);
            }

            Object thirstComponent = getComponentMethod.invoke(store, player.getReference(), thirstComponentType);

            if (thirstComponent != null) {
                // 5. Call drink(amount) on the component
                // Cache drink method if possible, but might vary by implementation class?
                // Unlikely.
                if (drinkMethod == null) {
                    drinkMethod = thirstComponent.getClass().getMethod("drink", float.class);
                }

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
