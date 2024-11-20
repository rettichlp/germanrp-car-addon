package de.rettichlp.germanrpcaraddon.events.keypress;

import net.labymod.api.client.gui.screen.key.Key;

/**
 * Represents an event triggered when the key to jump is double-pressed within a specific interval. This event contains information
 * about the key involved and the time interval between the presses.
 *
 * @author RettichLP
 */
public class DoubleJumpKeyPressEvent extends AbstractDoubleKeyPressEvent {

    public DoubleJumpKeyPressEvent(Key key, long pressInterval) {
        super(key, pressInterval);
    }
}
