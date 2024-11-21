package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.events.VehicleEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.lifecycle.GameTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.DRIVE;
import static de.rettichlp.germanrpcaraddon.events.VehicleEvent.Phase.ENTER;
import static de.rettichlp.germanrpcaraddon.events.VehicleEvent.Phase.LEAVE;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static net.labymod.api.Laby.fireEvent;
import static net.labymod.api.Laby.labyAPI;

/**
 * Listener for handling car enter events. This listener detects when the player enters a vehicle and schedules actions for the car.
 * The main responsibility is to turn on the engine and set the gear to drive when the player enters a car.
 *
 * <p>When the player enters a car, the following actions are scheduled:
 * <ul>
 *     <li>Turn on the engine.</li>
 *     <li>Set the gear to drive.</li>
 *     <li>Press the key to swap the offhand, which opens the car inventory.</li>
 * </ul>
 *
 * @author RettichLP
 * @see VehicleEvent
 */
@RequiredArgsConstructor
public class CarEnterListener {

    private final GermanRPCarAddon addon;

    @Nullable
    private UUID lastVehicleUniqueId = null;

    /**
     * Handles game tick events to detect vehicle changes regarding the player.
     *
     * @param event the game tick event
     */
    @Subscribe
    public void onGameTick(GameTickEvent event) {
        ofNullable(labyAPI().minecraft().getClientPlayer()).ifPresent(clientPlayer -> {
            Entity vehicle = clientPlayer.getVehicle();
            UUID uniqueId = ofNullable(vehicle).map(Entity::getUniqueId).orElse(null);

            if (Objects.equals(this.lastVehicleUniqueId, uniqueId)) {
                return;
            }

            if (isNull(uniqueId)) {
                fireEvent(new VehicleEvent(vehicle, LEAVE));
            }

            if (isNull(this.lastVehicleUniqueId)) {
                fireEvent(new VehicleEvent(vehicle, ENTER));
            }

            this.lastVehicleUniqueId = uniqueId;
        });
    }

    /**
     * Handles vehicle events to manage car-related actions.
     *
     * @param event the vehicle event containing the vehicle entity and phase
     */
    @Subscribe
    public void onVehicleEvent(VehicleEvent event) {
        // Only handle vehicle enter events
        if (event.phase() == ENTER) {
            this.addon.carService().executeOnCar(car -> {
                // Schedule actions for the car when the player enters it
                car.setScheduledEngineTurnOn(true);
                car.setScheduledGearChange(DRIVE);

                // Press the key to swap the offhand, it is the key to open the car inventory
                this.addon.minecraftController().pressSwapOffhandKey();
            }, () -> {});
        }
    }
}
