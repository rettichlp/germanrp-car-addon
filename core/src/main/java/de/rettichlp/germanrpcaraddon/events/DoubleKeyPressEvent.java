package de.rettichlp.germanrpcaraddon.events;

import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Event;

public class DoubleKeyPressEvent implements Event {

    private final Key key;

    public DoubleKeyPressEvent(Key key) {
        this.key = key;
    }

    public Key key() {
        return key;
    }
}
