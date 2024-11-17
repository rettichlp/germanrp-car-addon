package de.rettichlp.germanrpcaraddon.controllers;

import net.labymod.api.reference.annotation.Referenceable;

/**
 * This interface defines the contract for controlling car-related logic in various Minecraft versions. Implementations must provide
 * the specific logic for each Minecraft version the addon is for, ensuring compatibility and consistent behavior across different game
 * versions.
 *
 * <p>Each method corresponds to a fundamental car-related action or state check, allowing the system to determine the player's
 * interaction with cars within the game environment.
 *
 * <p>Implementations of this interface should be version-specific and handle any differences in Minecraft's API or mechanics for car
 * entities.
 *
 * <p>Marked as {@link Referenceable}, implementations of this interface can be dynamically resolved and used within the system to
 * ensure compatibility with different Minecraft versions.
 *
 * @author RettichLP
 */
@Referenceable
public interface CarController {

    /**
     * Checks if the player is currently inside a car.
     *
     * <p>This method determines whether the player is occupying a car entity in the current Minecraft session. It is
     * version-dependent and must be implemented according to the specific version's API.
     *
     * @return {@code true} if the player is in a car, {@code false} otherwise
     */
    boolean isInCar();

    /**
     * Checks if the car the player is currently in is running.
     *
     * <p>This method determines the operational state of the car. The specific conditions for a car being "running" depend on the
     * implementation and may vary across different Minecraft versions.
     *
     * @return {@code true} if the car is running, {@code false} otherwise
     */
    boolean isRunning();
}
