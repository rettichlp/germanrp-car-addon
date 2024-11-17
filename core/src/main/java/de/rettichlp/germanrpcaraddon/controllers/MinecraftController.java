package de.rettichlp.germanrpcaraddon.controllers;

import net.labymod.api.reference.annotation.Referenceable;

import java.util.List;

/**
 * This interface defines the contract for controlling various Minecraft inventory and interaction mechanics. Implementations must
 * provide version-specific logic to handle differences in the Minecraft API across versions.
 *
 * <p>The methods in this interface allow for interaction with containers, inventory management, and keypress actions. These
 * operations are essential for managing the player's inventory and interacting with the game's UI.
 *
 * <p>Marked as {@link Referenceable}, implementations of this interface can be dynamically resolved and used within the system to
 * ensure compatibility with different Minecraft versions.
 *
 * @author RettichLP
 */
@Referenceable
public interface MinecraftController {

    /**
     * Retrieves the title of the currently opened container.
     *
     * <p>The container title typically represents the name of the inventory UI (e.g., "Chest", "Furnace"). Implementations should
     * fetch the title according to the specific version's API.
     *
     * @return the title of the opened container, or an empty string if no container is open
     */
    String getContainerTitle();

    /**
     * Retrieves the unique identifier of the currently opened container.
     *
     * <p>The container ID is used internally by Minecraft to track open containers during interactions.
     *
     * @return the unique ID of the opened container, or {@code 0} if no container is open
     */
    int getContainerId();

    /**
     * Retrieves the slot indexes in the current container that contain a specific material.
     *
     * <p>This method allows for material-based filtering in containers, enabling targeted inventory interactions.
     *
     * @param material the name of the material to search for
     *
     * @return a list of slot indexes containing the specified material, or an empty list if none are found
     */
    List<Integer> getContainerSlotIndexesOf(String material);

    /**
     * Simulates a click on a specific slot in the player's inventory or container.
     *
     * <p>Uses the default click button (typically left-click) to perform the interaction.
     *
     * @param slotNumber the index of the slot to click
     */
    void inventoryClick(int slotNumber);

    /**
     * Simulates a click on a specific slot in the player's inventory or container, using the specified mouse button.
     *
     * @param slotNumber the index of the slot to click
     * @param button     the mouse button to use for the click (e.g., 0 for left-click, 1 for right-click)
     */
    void inventoryClick(int slotNumber, int button);

    /**
     * Simulates pressing the key to swap the item in the main hand with the item in the offhand.
     *
     * <p>This action corresponds to the "Swap Offhand" keybinding in Minecraft.
     */
    void pressSwapOffhandKey();

    /**
     * Closes the currently opened container screen.
     *
     * <p>This method simulates the player closing the inventory or container UI, typically by pressing the "Escape" key.
     */
    void closeContainerScreen();
}
