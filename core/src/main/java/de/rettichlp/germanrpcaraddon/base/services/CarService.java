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

/**
 * Service for managing car-related actions and data. This service provides methods to execute functions on the player's current car
 *
 * @author RettichLP
 */
@Data
public class CarService {

    private final GermanRPCarAddon addon;

    /**
     * The list of cars the player was at least once inside during the current session.
     */
    private Set<Car> cars = new HashSet<>();

    /**
     * Executes a given function on the player's current car if they are in one. If the player is not in a car, a fallback runnable is
     * executed instead.
     *
     * @param onCarFunction the function to execute when the player is in a car; receives the current car as a parameter
     * @param elseRunnable  the fallback action to execute when the player is not in a car
     *
     * @throws NullPointerException if {@code onCarFunction} or {@code elseRunnable} is {@code null}
     */
    public void executeOnCar(Function<Car> onCarFunction, Runnable elseRunnable) {
        if (!isInCar()) {
            elseRunnable.run();
            return;
        }

        Car currentCar = getCurrentCar();
        onCarFunction.apply(currentCar);
    }

    /**
     * Checks if the player is currently inside a car.
     *
     * @return {@code true} if the player is in a car, {@code false} otherwise
     */
    private boolean isInCar() {
        return labyAPI().minecraft().isIngame() && this.addon.carController().isInCar();
    }

    /**
     * Retrieves the car entity the player is currently inside. If the car is not yet registered in the system, it creates a new
     * {@link Car} instance and adds it to the managed list of cars.
     *
     * @return the {@link Car} instance representing the current vehicle the player is in
     *
     * @throws IllegalArgumentException if the player is not in a car
     * @throws NullPointerException     if the player's vehicle or unique ID cannot be retrieved
     */
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

    /**
     * Functional interface representing an operation to be performed on a {@link Car}.
     *
     * @param <Car> the type of car the function operates on
     */
    public interface Function<Car> {

        /**
         * Applies an operation to the given car.
         *
         * @param car the car to process
         */
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
         * Indicates whether the car's engine should be turned off.
         */
        private boolean scheduledEngineTurnOff;

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
