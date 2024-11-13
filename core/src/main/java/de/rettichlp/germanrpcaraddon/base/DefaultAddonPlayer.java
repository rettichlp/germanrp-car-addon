package de.rettichlp.germanrpcaraddon.base;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import lombok.RequiredArgsConstructor;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.Entity;
import net.labymod.api.client.entity.player.ClientPlayer;
import net.labymod.api.client.scoreboard.Scoreboard;
import net.labymod.api.client.world.ClientWorld;
import net.labymod.api.util.math.vector.FloatVector3;

import java.util.UUID;

import static net.labymod.api.Laby.labyAPI;

/**
 * @author RettichLP
 */
@RequiredArgsConstructor
public class DefaultAddonPlayer implements AddonPlayer {

    private final GermanRPCarAddon addon;

    @Override
    public ClientPlayer getPlayer() {
        return labyAPI().minecraft().getClientPlayer();
    }

    @Override
    public String getName() {
        return labyAPI().getName();
    }

    @Override
    public UUID getUniqueId() {
        return labyAPI().getUniqueId();
    }

    @Override
    public String getShortUniqueId() {
        return getUniqueId().toString().replace("-", "");
    }

    @Override
    public Float getHealth() {
        return getPlayer() != null ? getPlayer().getHealth() : null;
    }

    @Override
    public FloatVector3 getLocation() {
        return getPlayer() != null ? getPlayer().position() : null;
    }

    @Override
    public void sendMessage(String message) {
        this.addon.displayMessage(message);
    }

    @Override
    public void sendMessage(Component component) {
        this.addon.displayMessage(component);
    }

    @Override
    public void sendEmptyMessage() {
        sendMessage("");
    }

//    @Override
//    public void sendErrorMessage(String message) {
//        this.addon.displayMessage(Message.getBuilder()
//                .error().space()
//                .of(message).color(ColorCode.GRAY).advance()
//                .createComponent());
//    }

//    @Override
//    public void sendInfoMessage(String message) {
//        this.addon.displayMessage(Message.getBuilder()
//                .info().space()
//                .of(message).color(ColorCode.WHITE).advance()
//                .createComponent());
//    }

//    @Override
//    public void sendSyntaxMessage(String message) {
//        sendErrorMessage("Syntax: " + message);
//    }

    @Override
    public void sendServerMessage(String message) {
        this.addon.sendMessage(message);
        this.addon.logger().info("AddonPlayer sent chat message: " + message);
    }

    @Override
    public ClientWorld getWorld() {
        return labyAPI().minecraft().clientWorld();
    }

    @Override
    public Scoreboard getScoreboard() {
        return labyAPI().minecraft().getScoreboard();
    }

    @Override
    public Entity getVehicle() {
        return getPlayer() != null ? getPlayer().getVehicle() : null;
    }

    @Override
    public void copyToClipboard(String string) {
        labyAPI().minecraft().setClipboard(string);
    }
}
