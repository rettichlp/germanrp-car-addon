package de.rettichlp.germanrpcaraddon.controllers;

import net.labymod.api.client.gui.screen.key.Key;
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

    /**
     * Retrieves the key for the mapped key option name. Example minecraft mapping:
     * <table>
     *     <tr><td>key.attack</td><td>key.keyboard.0</tr>
     *     <tr><td>key.use</td><td>key.keyboard.1</tr>
     *     <tr><td>key.forward</td><td>key.keyboard.w</tr>
     *     <tr><td>key.left</td><td>key.keyboard.a</tr>
     *     <tr><td>key.back</td><td>key.keyboard.s</tr>
     *     <tr><td>key.right</td><td>key.keyboard.d</tr>
     *     <tr><td>key.jump</td><td>key.keyboard.space</tr>
     *     <tr><td>key.sneak</td><td>key.keyboard.left.shift</tr>
     *     <tr><td>key.sprint</td><td>key.keyboard.f</tr>
     *     <tr><td>key.drop</td><td>key.keyboard.q</tr>
     *     <tr><td>key.inventory</td><td>key.keyboard.e</tr>
     *     <tr><td>key.chat</td><td>key.keyboard.t</tr>
     *     <tr><td>key.playerlist</td><td>key.keyboard.tab</tr>
     *     <tr><td>key.pickItem</td><td>key.keyboard.2</tr>
     *     <tr><td>key.command</td><td>key.keyboard.backslash</tr>
     *     <tr><td>key.socialInteractions</td><td>key.keyboard.p</tr>
     *     <tr><td>key.screenshot</td><td>key.keyboard.f2</tr>
     *     <tr><td>key.togglePerspective</td><td>key.keyboard.f5</tr>
     *     <tr><td>key.smoothCamera</td><td>scancode.-1</tr>
     *     <tr><td>key.fullscreen</td><td>key.keyboard.f11</tr>
     *     <tr><td>key.spectatorOutlines</td><td>scancode.-1</tr>
     *     <tr><td>key.swapOffhand</td><td>key.keyboard.r</tr>
     *     <tr><td>key.saveToolbarActivator</td><td>key.keyboard.c</tr>
     *     <tr><td>key.loadToolbarActivator</td><td>key.keyboard.x</tr>
     *     <tr><td>key.advancements</td><td>key.keyboard.l</tr>
     *     <tr><td>key.hotbar.1</td><td>key.keyboard.1</tr>
     *     <tr><td>key.hotbar.2</td><td>key.keyboard.2</tr>
     *     <tr><td>key.hotbar.3</td><td>key.keyboard.3</tr>
     *     <tr><td>key.hotbar.4</td><td>key.keyboard.4</tr>
     *     <tr><td>key.hotbar.5</td><td>key.keyboard.5</tr>
     *     <tr><td>key.hotbar.6</td><td>key.keyboard.6</tr>
     *     <tr><td>key.hotbar.7</td><td>key.keyboard.7</tr>
     *     <tr><td>key.hotbar.8</td><td>key.keyboard.8</tr>
     *     <tr><td>key.hotbar.9</td><td>key.keyboard.9</tr>
     * </table>
     *
     * @param optionName the name of the key option to retrieve the mapping for, e.g. "swapOffhand"
     *
     * @return the key for the mapped option name
     */
    Key getKeyMapping(String optionName) throws IllegalArgumentException;
}
