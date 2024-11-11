package de.rettichlp.germanrpcaraddon.events;

import net.labymod.api.event.Event;

public class ScreenUpdateEvent implements Event {

    private final int containerId;

    public ScreenUpdateEvent(int containerId) {
        this.containerId = containerId;
    }

    public int getContainerId() {
        return containerId;
    }
}
