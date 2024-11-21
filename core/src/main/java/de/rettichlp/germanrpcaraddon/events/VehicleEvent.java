package de.rettichlp.germanrpcaraddon.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * Represents and event triggered when the player enters or leaves a vehicle. It is fired when the player enters or leaves a vehicle.
 * This event contains information about the vehicle being entered or left and the phase (entering or leaving).
 *
 * @author RettichLP
 * @see Event
 */
@Getter
@RequiredArgsConstructor
@Accessors(fluent = true)
public class VehicleEvent implements Event {

    /**
     * The vehicle entity that the player entered or left.
     */
    @Nullable
    private final Entity vehicle;

    /**
     * The phase of the event, either {@link Phase#ENTER} or {@link Phase#LEAVE}.
     */
    private final Phase phase;

    /**
     * Represents the phase of the event, either entering or leaving a vehicle.
     */
    public enum Phase {

        ENTER,
        LEAVE
    }
}
