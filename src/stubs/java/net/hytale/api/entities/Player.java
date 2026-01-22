package net.hytale.api.entities;

public interface Player extends Entity {
    void setHunger(int value);

    int getHunger();

    Attribute getAttribute(String keys);

    interface Attribute {
        float getValue();

        void setValue(float val);
    }
}
