package de.rettichlp.germanrpcaraddon.v1_21_3;

import de.rettichlp.germanrpcaraddon.controllers.CarController;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;

import javax.inject.Singleton;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * @author RettichLP
 */
@Singleton
@Implements(CarController.class)
public class VersionedCarController implements CarController {

    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public boolean isInCar() {
        // Check if the player is null
        if (isNull(mc.player)) {
            return false;
        }

        // Check if vehicle entity is a car seat
        Entity vehicle = mc.player.getVehicle();
        return vehicle instanceof ArmorStand as && nonNull(as.getDisplayName()) && as.getDisplayName().toString().contains("[SEAT]");
    }

    @Override
    public boolean isRunning() {
        // Check if the car is running by checking the slot count of the car inventory (6 rows = running, 3 rows = not running)
        int slotCount = mc.screen instanceof ContainerScreen containerScreen ? containerScreen.getMenu().getRowCount() : 0;
        return slotCount == 6;
    }
}
