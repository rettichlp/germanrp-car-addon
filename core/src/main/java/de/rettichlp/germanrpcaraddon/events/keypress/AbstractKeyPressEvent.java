package de.rettichlp.germanrpcaraddon.events.keypress;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.DefaultCancellable;
import net.labymod.api.event.Event;

/**
 * Represents an event triggered when a key is pressed. This event contains also information about the key involved in the press.
 *
 * @author RettichLP
 * @see Event
 * @see Key
 */
@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public abstract class AbstractKeyPressEvent extends DefaultCancellable implements Event {

    /**
     * The key that was pressed.
     */
    private final Key key;
}
