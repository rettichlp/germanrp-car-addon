package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.events.ScreenUpdateEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.ScreenRenderEvent;

import static net.labymod.api.Laby.fireEvent;

/**
 * Listener for monitoring screen updates within the GermanRPCarAddon. This class listens for screen render events and checks for
 * changes in the container ID to determine if the screen has been updated.
 *
 * <p>When a change in the container ID is detected, the listener triggers a {@link ScreenUpdateEvent} to notify other components of
 * the screen update.
 *
 * @author RettichLP
 */
@RequiredArgsConstructor
public class ScreenUpdateListener {

    private final GermanRPCarAddon addon;

    /**
     * Stores the previous container ID to track changes and detect when a new screen or container is rendered.
     */
    private int oldContainerId;

    /**
     * Handles the {@link ScreenRenderEvent} to monitor and respond to screen updates.
     *
     * <p>This method checks if the container ID has changed since the last render. If a change is detected, it updates the stored
     * container ID and fires a {@link ScreenUpdateEvent} with the new container ID.
     *
     * @param e The {@link ScreenRenderEvent} that occurs each time the screen is rendered.
     */
    @Subscribe
    public void onScreenRender(ScreenRenderEvent e) {
        int containerId = this.addon.minecraftController().getContainerId();

        if (this.oldContainerId != containerId) {
            this.oldContainerId = containerId;
            fireEvent(new ScreenUpdateEvent(containerId));
        }
    }
}
