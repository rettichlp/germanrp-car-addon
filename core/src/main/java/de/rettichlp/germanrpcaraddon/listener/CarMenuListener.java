package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import de.rettichlp.germanrpcaraddon.base.services.CarService;
import de.rettichlp.germanrpcaraddon.events.ScreenUpdateEvent;
import lombok.RequiredArgsConstructor;
import net.labymod.api.event.Subscribe;

import java.util.concurrent.BlockingQueue;

import static org.spongepowered.include.com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor
public class CarMenuListener {

    private final GermanRPCarAddon addon;

    @Subscribe
    public void onScreenUpdate(ScreenUpdateEvent event) {
        // Check if the container title contains "Fahrzeugsteuerung", this listener should only work in the car inventory
        String containerTitle = this.addon.minecraftController().getContainerTitle();
        if (!containerTitle.contains("Fahrzeugsteuerung")) {
            return;
        }

        this.addon.carService().executeOnCar(car -> {
            BlockingQueue<CarService.Car.CarTask> scheduledCarTasks = car.getScheduledCarTasks();

            CarService.Car.CarTask carTask = scheduledCarTasks.poll();
            switch (carTask) {
                case TOGGLE_ENGINE -> {
                    this.addon.debug("Toggling engine");
                    this.addon.minecraftController().inventoryClick(13);

                    if (scheduledCarTasks.isEmpty()) {
                        this.addon.minecraftController().closeContainerScreen();
                    }
                }
                case CHANGE_GEAR_TO_DRIVE -> {
                    this.addon.debug("Changing gear to drive");
                    checkArgument(car.isEngineRunning(), "Car is not running");
                    this.addon.minecraftController().inventoryClick(39);

                    if (scheduledCarTasks.isEmpty()) {
                        this.addon.minecraftController().closeContainerScreen();
                    }
                }
                case CHANGE_GEAR_TO_REVERSE -> {
                    this.addon.debug("Changing gear to reverse");
                    checkArgument(car.isEngineRunning(), "Car is not running");
                    this.addon.minecraftController().inventoryClick(41);

                    if (scheduledCarTasks.isEmpty()) {
                        this.addon.minecraftController().closeContainerScreen();
                    }
                }
                case TOGGLE_EMERGENCY_LIGHTS -> this.addon.minecraftController().inventoryClick(car.isEngineRunning() ? 24 : 15);
                case null -> {
                }
            }
        }, () -> {});
    }
}
