package de.rettichlp.germanrpcaraddon.events.keypress;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Event;

/**
 * Represents an event triggered when a key is double-pressed within a specific interval. This event contains information about the key
 * involved and the time interval between the presses.
 *
 * @author RettichLP
 * @see Event
 * @see Key
 */
@Getter
@Accessors(fluent = true)
public abstract class AbstractDoubleKeyPressEvent extends AbstractKeyPressEvent {

    /**
     * The interval (in milliseconds) between the two consecutive key presses.
     */
    private final long pressInterval;

    public AbstractDoubleKeyPressEvent(Key key, long pressInterval) {
        super(key);
        this.pressInterval = pressInterval;
    }
}
