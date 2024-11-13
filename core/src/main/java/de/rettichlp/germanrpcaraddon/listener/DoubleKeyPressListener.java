package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.events.DoubleKeyPressEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static net.labymod.api.Laby.fireEvent;
import static net.labymod.api.client.gui.screen.key.Key.S;
import static net.labymod.api.client.gui.screen.key.Key.SPACE;
import static net.labymod.api.client.gui.screen.key.Key.W;
import static net.labymod.api.event.client.input.KeyEvent.State.PRESS;

/**
 * Listener for detecting double key presses on specific keys, designed for use with the GermanRPCarAddon. This listener checks key
 * press events and triggers a double-click event when a key is pressed twice within a short interval.
 *
 * <p>The class maintains a record of the last press times for specific keys
 * ({@code W}, {@code S}, and {@code SPACE}) to detect double presses. If a key is pressed twice within 500 milliseconds, it considers
 * it a double press, logs this action for debugging purposes, and fires a custom {@link DoubleKeyPressEvent}.
 */
@RequiredArgsConstructor
public class DoubleKeyPressListener {

    private final GermanRPCarAddon addon;

    /**
     * A map holding the keys that are monitored for double-press actions, along with the timestamp of their last press event.
     */
    private final HashMap<Key, Long> lastPress = new HashMap<>(Map.of(
            W, 0L,
            S, 0L,
            SPACE, 0L
    ));

    /**
     * Handles {@link KeyEvent} to detect and respond to double key presses.
     *
     * <p>On receiving a key press event, the method first verifies if the key
     * is being observed. If the key has been pressed twice within a set time window (500 milliseconds), it triggers a
     * {@link DoubleKeyPressEvent} and resets the recorded press time for that key.
     *
     * @param event The {@link KeyEvent} representing a key press event, containing the key and its press state.
     */
    @Subscribe
    public void onKey(KeyEvent event) {
        Key key = event.key();

        // Check if the key is being observed. If not, return to save performance
        if (!this.lastPress.containsKey(key) || event.state() != PRESS) {
            return;
        }

        // Check for double press, because single press is already used by the server
        if (currentTimeMillis() - this.lastPress.get(key) < 500) {
            this.addon.debug("Key double pressed: " + key.getName());

            // Reset the time of the last press
            this.lastPress.put(key, 0L);

            // Fire own event for double press
            fireEvent(new DoubleKeyPressEvent(key));
        }

        // Update the time of the last press
        this.lastPress.put(key, currentTimeMillis());
    }
}
