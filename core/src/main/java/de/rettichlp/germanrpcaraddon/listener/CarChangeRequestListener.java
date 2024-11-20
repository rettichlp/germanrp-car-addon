package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.events.keypress.AbstractDoubleKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.DoubleBackKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.DoubleForwardKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.DoubleJumpKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.SneakKeyPressEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.event.Subscribe;

import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.DRIVE;
import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.REVERSE;

/**
 * Listener for handling double key press events relevant to car actions. This listener processes specific keys ({@code W}, {@code S},
 * {@code SPACE}) to schedule gear or siren changes for a car when the player is inside one.
 *
 * <p>The logic checks:
 * <ul>
 *     <li>Whether the player is currently in a car, determined by the {@link de.rettichlp.germanrpcaraddon.base.services.CarService}.</li>
 * </ul>
 * Based on the key pressed (example with default settings):
 * <ul>
 *     <li>{@code W} - Schedules a gear change to {@code DRIVE} if not already in {@code DRIVE}.</li>
 *     <li>{@code S} - Schedules a gear change to {@code REVERSE} if not already in {@code REVERSE}.</li>
 *     <li>{@code SPACE} - Schedules a siren state change.</li>
 * </ul>
 * After handling the key action, the "swap offhand" key is pressed to open the car inventory.
 *
 * @author RettichLP
 * @see AbstractDoubleKeyPressEvent
 */
@RequiredArgsConstructor
public class CarChangeRequestListener {

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
            car.setScheduledSirenChange(true);

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
}
