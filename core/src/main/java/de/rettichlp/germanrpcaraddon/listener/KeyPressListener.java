package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.events.DoubleKeyPressEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;

import java.util.HashMap;

import static java.lang.System.currentTimeMillis;
import static net.labymod.api.Laby.fireEvent;
import static net.labymod.api.Laby.references;
import static net.labymod.api.event.client.input.KeyEvent.State.PRESS;

@RequiredArgsConstructor
public class KeyPressListener {

    private final GermanRPCarAddon addon;

    private final HashMap<Key, Long> lastPress = new HashMap<>();

    @Subscribe
    public void onKey(KeyEvent event) {
        Key key = event.key();

        // Check if the key is being observed (to save performance) and if the chat is not open
        if (event.state() != PRESS || references().chatAccessor().isChatOpen()) {
            return;
        }

        // Check for double press, because single press is already used by the server
        if (currentTimeMillis() - this.lastPress.getOrDefault(key, 0L) < 500) {
            this.addon.debug("Key double pressed: " + key.getName());

            // Reset the time of the last press
            this.lastPress.remove(key);

            // Fire own event for double press
            fireEvent(new DoubleKeyPressEvent(key));
        } else {
            // Update the time of the last press
            this.lastPress.put(key, currentTimeMillis());
        }
    }
}
