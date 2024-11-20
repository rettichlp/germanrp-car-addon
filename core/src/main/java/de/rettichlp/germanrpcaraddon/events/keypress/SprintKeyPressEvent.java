package de.rettichlp.germanrpcaraddon.events.keypress;

import net.labymod.api.client.gui.screen.key.Key;

/**
 * Represents an event triggered when the key to sprint is pressed. This event contains also information about the key involved in the
 * press.
 *
 * @author RettichLP
 */
public class SprintKeyPressEvent extends AbstractKeyPressEvent {

    public SprintKeyPressEvent(Key key) {
        super(key);
    }
}
