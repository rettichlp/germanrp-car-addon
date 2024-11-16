package de.rettichlp.germanrpcaraddon.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.labymod.api.event.Event;

/**
 * Represents an event triggered when a container-screen update occurs. It is fired, if the identifier of the opened container on the
 * screen changes. This event contains information about the container being updated.
 *
 * @author RettichLP
 * @see Event
 */
@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class ScreenUpdateEvent implements Event {

    /**
     * The identifier of the container associated with the screen update.
     */
    private final int containerId;
}
