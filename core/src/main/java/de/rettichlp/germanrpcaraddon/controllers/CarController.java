package de.rettichlp.germanrpcaraddon.controllers;

import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Nullable
@Referenceable
public interface CarController {

    boolean isInCar();

    boolean isRunning();
}
