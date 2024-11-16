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

    private Pair<Key, Long> lastPress = Pair.of(null, 0L);

    @Subscribe
    public void onKey(KeyEvent event) {
        Key key = event.key();

        // Check the key press state and if the chat is not open
        if (event.state() != PRESS || references().chatAccessor().isChatOpen()) {
            return;
        }

        Key lastPressedKey = this.lastPress.getFirst();
        if (isNull(lastPressedKey) || !lastPressedKey.equals(key)) {
            this.lastPress = Pair.of(key, currentTimeMillis());
            return;
        }

        // The last press can't be null, because it is checked before via the key
        assert nonNull(this.lastPress.getSecond());
        // Check for double press, because single press is already used by the server
        long sinceLastPress = currentTimeMillis() - this.lastPress.getSecond();
        if (sinceLastPress < 500) {
            this.addon.debug("Key double pressed: " + key.getName());

            // Reset the last pressed key
            this.lastPress = Pair.of(null, 0L);

            // Fire own event for double press
            fireEvent(new DoubleKeyPressEvent(key, sinceLastPress));
        }
    }
}
