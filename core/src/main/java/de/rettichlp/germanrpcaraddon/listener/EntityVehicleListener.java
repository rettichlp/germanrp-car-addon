package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.GermanRPCarAddonConfiguration;
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
 * Listener for handling car enter events. This listener detects when the player enters a vehicle and fires a corresponding event.
 *
 * @author RettichLP
 * @see VehicleEvent
 */
@RequiredArgsConstructor
public class EntityVehicleListener {

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
}
