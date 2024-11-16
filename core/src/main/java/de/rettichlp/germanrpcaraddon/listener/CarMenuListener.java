package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.events.ScreenUpdateEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.event.Subscribe;

import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.DRIVE;
import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.REVERSE;

/**
 * Listener for handling screen updates related to the car inventory menu. This listener detects when the player interacts with the
 * "Fahrzeugsteuerung" menu and processes car-related actions such as gear or siren changes based on the car's current state.
 *
 * <p>Main responsibilities include:
 * <ul>
 *     <li>Checking if the current container title matches the expected car control menu.</li>
 *     <li>Managing gear changes, ensuring proper steps such as starting the engine if necessary.</li>
 *     <li>Handling siren toggles based on whether the car engine is running or not.</li>
 * </ul>
 *
 * @author RettichLP
 * @see ScreenUpdateEvent
 */
@RequiredArgsConstructor
public class CarMenuListener {

    private final GermanRPCarAddon addon;

    /**
     * Handles updates to the screen, specifically detecting interactions with the car control menu.
     *
     * @param event the screen update event containing details about the updated screen
     */
    @Subscribe
    public void onScreenUpdate(ScreenUpdateEvent event) {
        // Check if the container title contains "Fahrzeugsteuerung", this listener should only work in the car inventory
        String containerTitle = this.addon.minecraftController().getContainerTitle();
        if (!containerTitle.contains("Fahrzeugsteuerung")) {
            return;
        }

        this.addon.carService().executeOnCar(car -> {
            this.addon.debug("Car menu detected of car: " + car.toString());

            // Check if the gear should be changed
            if (car.getScheduledGearChange() != null) {
                // If this field is empty, close the container screen and reset the last double click action key without any further logic
                if (car.getScheduledGearChange().isEmpty()) {
                    car.setScheduledGearChange(null);
                    this.addon.minecraftController().closeContainerScreen();
                    return;
                }

                // Check if the car is running
                if (!car.isEngineRunning()) {
                    // If the car is not running, start the engine first
                    this.addon.minecraftController().inventoryClick(13);
                    return;
                }

                // Get the gear slot and click it, then reset the last double click action key
                int slot = getGearSlot(car.getScheduledGearChange());
                this.addon.debug("Gear slot: " + slot);
                car.setScheduledGearChange(""); // empty instead of null, because the container needs to be closed one more time
                this.addon.minecraftController().inventoryClick(slot);
                car.setGear(slot == 39 ? DRIVE : REVERSE);
                return;
            }

            // Check if the siren should be changed
            if (car.isScheduledSirenChange()) {
                // The siren can be changed if the car is running and also if not - the slot is different for both cases
                boolean running = car.isEngineRunning();
                int slot = running ? 24 : 15;

                // Click the slot and reset the change siren variable
                car.setScheduledSirenChange(false);
                this.addon.minecraftController().inventoryClick(slot);
            }
        }, () -> {});
    }

    /**
     * Retrieves the appropriate gear slot based on the provided key name. This method maps specific keys to their respective inventory
     * slots for controlling the car's direction in the game.
     * <p>
     * For example, pressing {@code W} will map to slot 39 (for moving forward), while {@code S} maps to slot 41 (for moving in
     * reverse). If the key does not match any predefined control keys, it defaults to slot 0, indicating no action should be taken.
     *
     * @param keyName The name of the key that was double-clicked (e.g., {@code W} or {@code S}).
     *
     * @return The corresponding gear slot in the inventory; defaults to 0 if no match is found.
     */
    private int getGearSlot(String keyName) {
        // slot 39 = forward, slot 41 = reverse
        return switch (keyName) {
            case "W" -> 39;
            case "S" -> 41;
            default -> 0;
        };
    }
}
