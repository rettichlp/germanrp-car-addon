package de.rettichlp.germanrpcaraddon.base.services;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import lombok.Builder;
import lombok.Data;
import net.labymod.api.client.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.PARK;
import static java.util.Objects.requireNonNull;
import static net.labymod.api.Laby.labyAPI;
import static org.spongepowered.include.com.google.common.base.Preconditions.checkArgument;

@Data
public class CarService {

    private final GermanRPCarAddon addon;

    private Set<Car> cars = new HashSet<>();

    private boolean isInCar() {
        return labyAPI().minecraft().isIngame() && this.addon.carController().isInCar();
    }

    private Car getCurrentCar() {
        checkArgument(isInCar(), "Player is not in a car.");

        Entity vehicle = requireNonNull(labyAPI().minecraft().getClientPlayer()).getVehicle();
        UUID uniqueId = vehicle.getUniqueId();

        return this.cars.stream()
                .filter(car -> car.getUniqueId().equals(uniqueId))
                .findFirst()
                .orElseGet(() -> {
                    Car car = Car.builder()
                            .uniqueId(uniqueId)
                            .build();

                    this.cars.add(car);
                    return car;
                });
    }

    public void executeOnCar(Function<Car> onCarFunction, Runnable elseRunnable) {
        if (!isInCar()) {
            elseRunnable.run();
            return;
        }

        Car currentCar = getCurrentCar();
        onCarFunction.apply(currentCar);
    }

    public interface Function<Car> {

        void apply(Car car);
    }

    @Data
    @Builder
    public static class Car {

        /**
         * The unique identifier of the driver seat armorstand of the car.
         */
        private final UUID uniqueId;

        /**
         * The last known gear the car is currently in or defaults to {@link Gear#PARK}.
         */
        @Builder.Default
        private Gear gear = PARK;

        /**
         * Indicates whether the car has emergency lights.
         */
        @Builder.Default
        private boolean emergencyLights = false;

        /**
         * Indicates whether the emergency lights are enabled.
         */
        @Builder.Default
        private boolean emergencyLightsEnabled = false;

        /**
         * Indicates whether the car's engine is running.
         */
        @Builder.Default
        private boolean engineRunning = false;

        /**
         * Indicates whether the car's siren should be changed.
         */
        private boolean scheduledSirenChange;

        /**
         * The key that was double-pressed to schedule a gear change.
         */
        private String scheduledGearChange;

        /**
         * The gears of the car.
         */
        public enum Gear {
            PARK,
            DRIVE,
            REVERSE
        }
    }
}
