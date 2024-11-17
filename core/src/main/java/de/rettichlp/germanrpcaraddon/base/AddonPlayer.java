package de.rettichlp.germanrpcaraddon.base;

import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.world.ClientWorld;
import net.labymod.api.util.math.vector.FloatVector3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author RettichLP
 */
public interface AddonPlayer {

    @Nullable
    ClientPlayer getPlayer();

    String getName();

    UUID getUniqueId();

    String getShortUniqueId();

    @Nullable
    Float getHealth();

    @Nullable
    FloatVector3 getLocation();

    void sendMessage(String message);

    void sendMessage(Component component);

    void sendEmptyMessage();

    //void sendErrorMessage(String message);

    //void sendInfoMessage(String message);

    //void sendSyntaxMessage(String message);

    void sendServerMessage(String message);

    ClientWorld getWorld();

    Scoreboard getScoreboard();

    @Nullable
    Entity getVehicle();

    void copyToClipboard(String string);
}
