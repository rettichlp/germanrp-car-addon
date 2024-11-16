package de.rettichlp.germanrpcaraddon.events;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.event.Event;

import static net.labymod.api.client.gui.screen.key.Key.S;
import static net.labymod.api.client.gui.screen.key.Key.SPACE;
import static net.labymod.api.client.gui.screen.key.Key.W;

@Data
@RequiredArgsConstructor
@Accessors(fluent = true)
public class DoubleKeyPressEvent implements Event {

    private final Key key;

    public boolean isCarRelevantKey() {
        return key == W || key == S || key == SPACE;
    }
}
