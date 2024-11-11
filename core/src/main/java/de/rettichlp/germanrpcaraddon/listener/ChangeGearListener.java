package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.events.DoubleKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.ScreenUpdateEvent;
import net.labymod.api.event.Subscribe;
import org.jetbrains.annotations.Nullable;

import static net.labymod.api.Laby.labyAPI;

/**
 * Depending on whether the W or S button was pressed twice, the car should automatically switch to forward or reverse gear.
 * <p>
 * If the car is turned off, it should turn on first and then select the appropriate gear.
 *
 * @author RettichLP
 */
public class ChangeGearListener {

    private final GermanRPCarAddon addon;

    @Nullable
    private String lastDoubleClickActionKey;

    public ChangeGearListener(GermanRPCarAddon addon) {
        this.addon = addon;
    }

    /**
     * Event handler for handling double key presses. This method listens for {@code DoubleKeyPressEvent} events and performs specific
     * actions when certain conditions are met.
     *
     * <p>When this method is triggered by a double key press event, it first checks
     * if the player is currently in the game environment (ingame) and is seated in a car. If both conditions are satisfied, it stores
     * the name of the double-pressed key and simulates the action of pressing the "swap offhand" key, which effectively opens the
     * car's inventory interface in the game.
     *
     * @param event The {@link DoubleKeyPressEvent} containing the details of the key that was double-pressed by the user.
     */
    @Subscribe
    public void onDoubleKey(DoubleKeyPressEvent event) {
        // Check if the player is ingame and in a car
        boolean ingame = labyAPI().minecraft().isIngame();
        if (ingame && this.addon.carController().isInCar()) {
            // Save key for later use
            this.lastDoubleClickActionKey = event.key().getName();
            // Press the key to swap the offhand, it is the key to open the car inventory
            this.addon.minecraftController().pressSwapOffhandKey();
        }
    }

    /**
     * Event handler for updating the screen when the {@link ScreenUpdateEvent} is triggered. This method listens for screen updates
     * and performs specific actions based on the last recorded double-click action key.
     *
     * <p>The method first checks if a double-click action key is present and needs processing.
     * If the key is either null or empty, no further action is required, or it will simply close the container screen and reset the
     * action key without additional logic.
     *
     * <p>If the last double-click action key is valid, the method then verifies if the current
     * container title includes "Fahrzeugsteuerung" (indicating the player is in the car inventory) and proceeds to perform a set of
     * checks to ensure the car is running.
     *
     * <p>Once verified, the method retrieves the appropriate gear slot based on the stored
     * action key, simulates a click on that slot, and finally resets the action key to an empty string. This setup ensures that the
     * inventory screen is closed in a subsequent screen update.
     *
     * @param event The {@link ScreenUpdateEvent} representing the update of the current screen.
     */
    @Subscribe
    public void onScreenUpdate(ScreenUpdateEvent event) {
        // Check that the last double click action key is not null, if it is null, no double click action has to be performed
        if (this.lastDoubleClickActionKey == null) {
            return;
        }

        // If this field is empty, close the container screen and reset the last double click action key without any further logic
        if (this.lastDoubleClickActionKey.isEmpty()) {
            this.lastDoubleClickActionKey = null;
            this.addon.minecraftController().closeContainerScreen();
            return;
        }

        // Check if the container title contains "Fahrzeugsteuerung", this listener should only work in the car inventory
        String containerTitle = this.addon.minecraftController().getContainerTitle();
        if (!containerTitle.contains("Fahrzeugsteuerung")) {
            return;
        }

        // Check if the car is running
        if (!verifyCarIsRunning()) {
            return;
        }

        // Get the gear slot and click it, then reset the last double click action key
        int slot = getGearSlot(this.lastDoubleClickActionKey);
        this.addon.debug("Gear slot: " + slot);
        this.lastDoubleClickActionKey = ""; // empty instead of null, because the container needs to be closed one more time
        this.addon.minecraftController().inventoryClick(slot);
    }

    /**
     * Checks if the car is currently running and starts it if not. This method interacts with the car controller to verify the running
     * state and attempts to start the car if it is found to be stopped.
     *
     * <p>If the car is not running, the method simulates an inventory click on slot 13,
     * which corresponds to the action to start the car in the game. It then returns {@code false} to indicate that the car was
     * initially stopped. If the car is already running, it simply returns {@code true}.
     *
     * @return {@code true} if the car is running; {@code false} if the car needed to be started.
     */
    private boolean verifyCarIsRunning() {
        boolean running = this.addon.carController().isRunning();
        this.addon.debug("Car is running: " + running);

        if (!running) {
            // Click the slot 13 to start the car
            this.addon.minecraftController().inventoryClick(13);
            return false;
        }

        return true;
    }

    /**
     * Retrieves the appropriate gear slot based on the provided key name. This method maps specific keys to their respective inventory
     * slots for controlling the car's direction in the game.
     *
     * <p>For example, pressing {@code W} will map to slot 39 (for moving forward),
     * while {@code S} maps to slot 41 (for moving in reverse). If the key does not match any predefined control keys, it defaults to
     * slot 0, indicating no action should be taken.
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
