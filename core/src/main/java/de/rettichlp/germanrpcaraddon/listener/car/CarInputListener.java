package de.rettichlp.germanrpcaraddon.listener.car;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.GermanRPCarAddonConfiguration;
import de.rettichlp.germanrpcaraddon.events.VehicleEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.AbstractDoubleKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.DoubleBackKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.DoubleForwardKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.DoubleJumpKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.SneakKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.SprintKeyPressEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.event.Subscribe;

import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.DRIVE;
import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.REVERSE;
import static de.rettichlp.germanrpcaraddon.events.VehicleEvent.Phase.ENTER;

/**
 * Listener for handling actions regarding the car by the player. This listener detects key presses (e.g., {@code W}, {@code S}, or {@code SPACE}) to schedule gear or siren changes for a car when the player is inside one. Also handles vehicle enter and leave events to manage car-related actions.
 * <p>After handling the actions, almost always the "swap offhand" key is pressed to open the car inventory.
 *
 * @author RettichLP
 * @see AbstractDoubleKeyPressEvent
 */
@RequiredArgsConstructor
public class CarInputListener {

    private final GermanRPCarAddon addon;

    /**
     * Handles double back key press to manage car-related actions.
     *
     * @param event the double back key press event containing the key pressed and interval details
     */
    @Subscribe
    public void onDoubleBackKeyPress(DoubleBackKeyPressEvent event) {
        // Check if the player is in a car
        this.addon.carService().executeOnCar(car -> {
            if (car.getGear() == REVERSE) {
                return;
            }

            car.setScheduledEngineTurnOn(true);
            car.setScheduledGearChange(REVERSE);

            // Press the key to swap the offhand, it is the key to open the car inventory
            this.addon.minecraftController().pressSwapOffhandKey();
        }, () -> {});
    }

    /**
     * Handles double forward key press to manage car-related actions.
     *
     * @param event the double forward key press event containing the key pressed and interval details
     */
    @Subscribe
    public void onDoubleForwardKeyPress(DoubleForwardKeyPressEvent event) {
        // Check if the player is in a car
        this.addon.carService().executeOnCar(car -> {
            if (car.getGear() == DRIVE) {
                return;
            }

            car.setScheduledEngineTurnOn(true);
            car.setScheduledGearChange(DRIVE);

            // Press the key to swap the offhand, it is the key to open the car inventory
            this.addon.minecraftController().pressSwapOffhandKey();
        }, () -> {});
    }

    /**
     * Handles double jump key press to manage car-related actions.
     *
     * @param event the double jump key press event containing the key pressed and interval details
     */
    @Subscribe
    public void onDoubleJumpKeyPress(DoubleJumpKeyPressEvent event) {
        // Check if the player is in a car
        this.addon.carService().executeOnCar(car -> {
            car.setScheduledBlueLightChange(true);

            // Press the key to swap the offhand, it is the key to open the car inventory
            this.addon.minecraftController().pressSwapOffhandKey();
        }, () -> {});
    }

    /**
     * Handles sneak key press to manage car-related actions.
     *
     * @param event the sneak key press event containing the key pressed
     */
    @Subscribe
    public void onSneakKeyPress(SneakKeyPressEvent event) {
        // Check if the player is in a car
        this.addon.carService().executeOnCar(car -> {
            if (!car.isEngineRunning()) {
                return;
            }

            // Cancel the event to prevent the player from leaving the car before the engine is turned off
            event.setCancelled(true);
            car.setScheduledEngineTurnOff(true);

            // Press the key to swap the offhand, it is the key to open the car inventory
            this.addon.minecraftController().pressSwapOffhandKey();
        }, () -> {});
    }

    /**
     * Handles sprint key press to manage car-related actions.
     *
     * @param event the sprint key press event containing the key pressed
     */
    @Subscribe
    public void onSprintKeyPress(SprintKeyPressEvent event) {
        // Check if the player is in a car
        this.addon.carService().executeOnCar(car -> {
            if (!car.isEngineRunning() || !car.isEmergencyLights() || !car.isEmergencyLightsEnabled()) {
                return;
            }

            car.setScheduledSirenChange(true);

            // Press the key to swap the offhand, it is the key to open the car inventory
            this.addon.minecraftController().pressSwapOffhandKey();
        }, () -> {});
    }

    /**
     * Handles vehicle events to manage car-related actions.
     *
     * @param event the vehicle event containing the vehicle entity and phase
     */
    @Subscribe
    public void onVehicleEvent(VehicleEvent event) {
        // Only handle vehicle enter events
        GermanRPCarAddonConfiguration configuration = this.addon.configuration();
        if (configuration.remoteEngineStart().get() && event.phase() == ENTER) {
            this.addon.carService().executeOnCar(car -> {
                // Schedule actions for the car when the player enters it

                // Turn on the engine if the player enters the car
                if (configuration.pressToStart().get()) {
                    car.setScheduledEngineTurnOn(true);
                }

                // Set the gear to drive if the automatic gearbox is enabled (and the engine is running)
                if (configuration.automaticGearbox().get()) {
                    car.setScheduledGearChange(DRIVE);
                }

                //TODO enable blue light and siren if the player has an active emergency mission

                // Press the key to swap the offhand, it is the key to open the car inventory
                this.addon.minecraftController().pressSwapOffhandKey();
            }, () -> {});
        }
    }
}
