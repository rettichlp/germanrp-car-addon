package de.rettichlp.germanrpcaraddon.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Event;

import static net.labymod.api.client.gui.screen.key.Key.S;
import static net.labymod.api.client.gui.screen.key.Key.SPACE;
import static net.labymod.api.client.gui.screen.key.Key.W;

/**
 * Represents an event triggered when a key is double-pressed within a specific interval. This event contains information about the key
 * involved and the time interval between the presses.
 *
 * <p>It also provides utility to check if the pressed key is relevant for car-related actions.
 *
 * @author RettichLP
 * @see Event
 * @see Key
 */
@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class DoubleKeyPressEvent implements Event {

    /**
     * The key that was double-pressed.
     */
    private final Key key;

    /**
     * The interval (in milliseconds) between the two consecutive key presses.
     */
    private final long pressInterval;

    /**
     * Determines if the double-pressed key is relevant for car-related controls.
     *
     * <p>Relevant keys are:
     * <ul>
     *     <li>{@code W} - Typically used for forward movement.</li>
     *     <li>{@code S} - Typically used for backward movement.</li>
     *     <li>{@code SPACE} - Typically used for horn usage.</li>
     * </ul>
     *
     * @return {@code true} if the key is car-relevant; {@code false} otherwise.
     */
    public boolean isCarRelevantKey() {
        return key == W || key == S || key == SPACE;
    }
}
