package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.base.services.CarService;
import de.rettichlp.germanrpcaraddon.events.ScreenUpdateEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.event.Subscribe;

import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.NONE;

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
            // Retrieve engine state through the size of the inventory (running = 54, off = 27)
            car.setEngineRunning(this.addon.carController().isRunning());

            // If the car should be turned on and the car is running, reset the scheduled turn-on variable because the car is already running
            if (car.isEngineRunning() && car.isScheduledEngineTurnOn()) {
                car.setScheduledEngineTurnOn(false);
                // Do not return here, because the gear might need to be changed
            }

            // If the car should be turned off and the car is running, turn off the engine. Reset the scheduled turn-off variable afterwards.
            if (car.isScheduledEngineTurnOff()) {
                if (car.isEngineRunning()) {
                    this.addon.minecraftController().inventoryClick(13);
                }

                car.setScheduledEngineTurnOff(false);
                return;
            }

            // Check if the car should be turned on and the car is not running
            if (this.addon.configuration().pressToStart().get() && car.isScheduledEngineTurnOn() && !car.isEngineRunning()) {
                // Start the engine
                this.addon.minecraftController().inventoryClick(13);
                return;
            }

            // If the car is running and the automatic gearbox is enabled, change the gear
            CarService.Car.Gear scheduledGearChange = car.getScheduledGearChange();
            if (this.addon.configuration().automaticGearbox().get() && scheduledGearChange != null && car.isEngineRunning()) {
                // If this field is NONE, close the container screen and reset the scheduled gear change variable without any further logic
                if (scheduledGearChange == NONE) {
                    car.setScheduledGearChange(null);
                    this.addon.minecraftController().closeContainerScreen();
                    return;
                }

                // Get the gear slot and click it, then reset the scheduled gear change variable
                car.setScheduledGearChange(NONE); // NONE instead of null, because the container needs to be closed one more time
                this.addon.minecraftController().inventoryClick(scheduledGearChange.getSlot());
                car.setGear(scheduledGearChange);
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
}
