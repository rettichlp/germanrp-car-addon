package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.controllers.MinecraftController;
import de.rettichlp.germanrpcaraddon.events.keypress.AbstractDoubleKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.DoubleBackKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.DoubleForwardKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.DoubleJumpKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.SneakKeyPressEvent;
import de.rettichlp.germanrpcaraddon.events.keypress.SprintKeyPressEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.util.Pair;

import java.util.Objects;

import static java.lang.System.currentTimeMillis;
import static java.util.Optional.ofNullable;
import static net.labymod.api.Laby.fireEvent;
import static net.labymod.api.Laby.references;
import static net.labymod.api.event.client.input.KeyEvent.State.PRESS;

/**
 * Listens for key presses and handles double key presses within a specific timeframe. This listener is responsible for detecting
 * double key presses and firing a {@link AbstractDoubleKeyPressEvent} if the condition is met.
 *
 * <p>The listener ensures that the action only proceeds if the key press state matches the expected {@code PRESS} state and the chat
 * is not currently open. It also manages the timing of key presses using an internal {@code lastPress} variable.
 *
 * @author RettichLP
 * @see KeyEvent
 * @see AbstractDoubleKeyPressEvent
 */
@RequiredArgsConstructor
public class KeyPressListener {

    private final GermanRPCarAddon addon;

    /**
     * Stores the last pressed key and the timestamp of the key press in milliseconds.
     */
    private Pair<Key, Long> lastPress = Pair.of(null, 0L);

    /**
     * Handles the {@link KeyEvent} triggered when a key is pressed. This method checks for a double key press within a 500-millisecond
     * timeframe and fires a {@link AbstractDoubleKeyPressEvent} if the condition is met.
     *
     * <p>The method ensures that the action only proceeds if:
     * <ul>
     *     <li>The key press state matches the expected {@code PRESS} state.</li>
     *     <li>The chat is not currently open, as determined by {@code chatAccessor().isChatOpen()}.</li>
     * </ul>
     * Additionally, it manages the timing of key presses using an internal {@code lastPress} variable for double presses.
     *
     * @param event the key event containing details of the key interaction
     */
    @Subscribe
    public void onKey(KeyEvent event) {
        // Check the key press state and if the chat is not open
        if (event.state() != PRESS || references().chatAccessor().isChatOpen()) {
            return;
        }

        Key key = event.key();
        MinecraftController minecraftController = this.addon.minecraftController();

        // Check for sneak key press
        if (key.equals(minecraftController.getKeyMapping("sneak"))) {
            SneakKeyPressEvent sneakKeyPressEvent = fireEvent(new SneakKeyPressEvent(key));
            event.setCancelled(sneakKeyPressEvent.isCancelled());
        }

        // Check for sprint key press
        if (key.equals(minecraftController.getKeyMapping("sprint"))) {
            SprintKeyPressEvent sprintKeyPressEvent = fireEvent(new SprintKeyPressEvent(key));
            event.setCancelled(sprintKeyPressEvent.isCancelled());
        }

        // Reset the last press to the current key
        Key lastPressKey = this.lastPress.getFirst();
        Long lastPressTimestamp = ofNullable(this.lastPress.getSecond()).orElse(0L);
        this.lastPress = Pair.of(key, currentTimeMillis());

        // Check if the last key is the same as the current key (with additional null check)
        if (!Objects.equals(lastPressKey, key)) {
            return;
        }

        // Check for double press and return if the interval is too long
        long sinceLastPress = currentTimeMillis() - lastPressTimestamp;
        if (sinceLastPress > 500) {
            return;
        }

        this.addon.debug("Key double pressed: " + key.getName());

        // Fire events for double presses
        if (key.equals(minecraftController.getKeyMapping("back"))) {
            fireEvent(new DoubleBackKeyPressEvent(key, sinceLastPress));
        } else if (key.equals(minecraftController.getKeyMapping("forward"))) {
            fireEvent(new DoubleForwardKeyPressEvent(key, sinceLastPress));
        } else if (key.equals(minecraftController.getKeyMapping("jump"))) {
            fireEvent(new DoubleJumpKeyPressEvent(key, sinceLastPress));
        }
    }
}
