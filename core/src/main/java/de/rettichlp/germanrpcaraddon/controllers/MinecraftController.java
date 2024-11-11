package de.rettichlp.germanrpcaraddon.controllers;

import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Nullable
@Referenceable
public interface MinecraftController {

    String getContainerTitle();

    int getContainerId();

    List<Integer> getContainerSlotIndexesOf(String material);

    void inventoryClick(int slotNumber);

    void inventoryClick(int slotNumber, int button);

    void pressSwapOffhandKey();

    void closeContainerScreen();
}
