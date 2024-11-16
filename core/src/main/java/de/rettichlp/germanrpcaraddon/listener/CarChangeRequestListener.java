package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.events.DoubleKeyPressEvent;
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
 *     <li>Whether the pressed key is relevant for car controls, using {@link DoubleKeyPressEvent#isCarRelevantKey()}.</li>
 *     <li>Whether the player is currently in a car, determined by the {@link de.rettichlp.germanrpcaraddon.base.services.CarService}.</li>
 * </ul>
 * Based on the key pressed:
 * <ul>
 *     <li>{@code W} - Schedules a gear change to {@code DRIVE} if not already in {@code DRIVE}.</li>
 *     <li>{@code S} - Schedules a gear change to {@code REVERSE} if not already in {@code REVERSE}.</li>
 *     <li>{@code SPACE} - Schedules a siren state change.</li>
 * </ul>
 * After handling the key action, the "swap offhand" key is pressed to open the car inventory.
 *
 * @author RettichLP
 * @see DoubleKeyPressEvent
 */
@RequiredArgsConstructor
public class CarChangeRequestListener {

    private final GermanRPCarAddon addon;

    /**
     * Handles double key press events to manage car-related actions.
     *
     * @param event the double key press event containing the key pressed and interval details
     */
    @Subscribe
    public void onDoubleKey(DoubleKeyPressEvent event) {
        // Check if the key is relevant for the car
        if (!event.isCarRelevantKey()) {
            return;
        }

        // Check if the player is in a car
        this.addon.carService().executeOnCar(car -> {
            String keyName = event.key().getName();
            switch (keyName) {
                case "W" -> {
                    if (car.getGear() != DRIVE) {
                        car.setScheduledGearChange(keyName);
                    }
                }
                case "S" -> {
                    if (car.getGear() != REVERSE) {
                        car.setScheduledGearChange(keyName);
                    }
                }
                case "SPACE" -> car.setScheduledSirenChange(true);
            }

            // Press the key to swap the offhand, it is the key to open the car inventory
            this.addon.minecraftController().pressSwapOffhandKey();
        }, () -> {});
    }
}
