package net.hytale.api;

public class HytaleServer {
    public static EventBus getEventBus() {
        return null;
    }

    public static ModManager getModManager() {
        return null;
    }

    public static ItemRegistry getItemRegistry() {
        return null;
    }

    public interface EventBus {
        void registerListener(Object listener);
    }

    public interface ModManager {
        boolean isLoaded(String modId);
    }

    public interface ItemRegistry {
        boolean contains(String itemName);
    }
}
