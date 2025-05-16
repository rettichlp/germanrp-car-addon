package de.rettichlp.germanrpcaraddon.v1_21_5;

import de.rettichlp.germanrpcaraddon.controllers.MinecraftController;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.options.MinecraftInputMapping;
import net.labymod.api.models.Implements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.HashedPatchMap;
import net.minecraft.network.HashedStack;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.item.ItemStack;

import javax.inject.Singleton;
import java.util.List;

import static com.mojang.blaze3d.platform.InputConstants.getKey;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static net.minecraft.client.KeyMapping.click;
import static net.minecraft.world.inventory.ClickType.PICKUP;

/**
 * @author RettichLP
 */
@Singleton
@Implements(MinecraftController.class)
public class VersionedMinecraftController implements MinecraftController {

    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public String getContainerTitle() {
        return ofNullable(mc.screen)
                .map(screen -> screen.getTitle().getString())
                .orElse("");
    }

    @Override
    public int getContainerId() {
        return mc.screen instanceof ContainerScreen containerScreen ? containerScreen.getMenu().containerId : 0;
    }

    @Override
    public List<Integer> getContainerSlotIndexesOf(String material) {
        if (!(mc.screen instanceof ContainerScreen containerScreen)) {
            return emptyList();
        }

        return containerScreen.getMenu().slots.stream()
                .filter(slot -> slot.getItem().getItem().toString().equals(material))
                .map(slot -> slot.index)
                .toList();
    }

    @Override
    public void inventoryClick(int slotNumber) {
        inventoryClick(slotNumber, 0);
    }

    @Override
    public void inventoryClick(int slotNumber, int button) {
        int containerId = getContainerId();
        if (containerId == 0) {
            return;
        }

        assert mc.player != null;
        ItemStack clickedItem = mc.player.containerMenu.getSlot(slotNumber).getItem();
        HashedPatchMap.HashGenerator hashGenerator = requireNonNull(mc.getConnection()).decoratedHashOpsGenenerator();
        HashedStack carried = HashedStack.create(clickedItem, hashGenerator);

        ServerboundContainerClickPacket packet = new ServerboundContainerClickPacket(
                containerId,
                mc.player.containerMenu.getStateId(),
                (short) slotNumber,
                (byte) button,
                PICKUP,
                new Int2ObjectOpenHashMap<>(),
                carried
        );

        mc.player.connection.send(packet);
    }

    @Override
    public void pressSwapOffhandKey() {
        int keyCode = ((MinecraftInputMapping) mc.options.keySwapOffhand).getKeyCode();
        click(getKey(keyCode, keyCode));
    }

    @Override
    public void closeContainerScreen() {
        assert mc.player != null;
        mc.player.closeContainer();
    }

    @Override
    public Key getKeyMapping(String optionName) throws IllegalArgumentException {
        return stream(mc.options.keyMappings)
                .filter(keyMapping -> keyMapping.getName().equals("key." + optionName))
                .findAny()
                .map(keyMapping -> ((MinecraftInputMapping) keyMapping))
                .map(MinecraftInputMapping::getKeyCode)
                .map(Key::get)
                .orElseThrow(() -> new IllegalArgumentException("Key mapping not found"));
    }
}
