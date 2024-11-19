package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import lombok.RequiredArgsConstructor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ActionBarReceiveEvent;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.DRIVE;
import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.PARK;
import static de.rettichlp.germanrpcaraddon.base.services.CarService.Car.Gear.REVERSE;
import static java.util.regex.Pattern.compile;
import static net.labymod.api.client.component.serializer.plain.PlainTextComponentSerializer.plainUrl;

/**
 * Listener for handling events related to the car's state, including updates to the car's emergency lights, engine status, and gear
 * settings. This listener processes incoming chat messages and action bar updates to keep the car's state in sync with the game's
 * current state.
 *
 * <p>Main responsibilities include:
 * <ul>
 *     <li>Parsing chat messages to detect changes in emergency lights and engine state.</li>
 *     <li>Interpreting action bar messages to update the car's gear and engine state accordingly.</li>
 *     <li>Ensuring seamless synchronization of in-game car actions with the addon logic.</li>
 * </ul>
 *
 * <p>The class uses regex patterns to identify and extract relevant information from the game messages, enabling accurate updates to
 * the associated car entity.
 *
 * @see ChatReceiveEvent
 * @see ActionBarReceiveEvent
 */
@RequiredArgsConstructor
public class CarStateListener {

    /**
     * Regex pattern for detecting emergency lights status from chat messages. Matches messages indicating whether emergency lights are
     * on or off.
     */
    private final static Pattern EMERGENCY_LIGHTS_PATTERN = compile("^► Du fährst jetzt (?<state>mit|wieder ohne) Sondersignal\\.$");

    /**
     * Regex pattern for detecting engine status from chat messages. Matches messages indicating whether the engine is started or
     * stopped.
     */
    private final static Pattern ENGINE_PATTERN = compile("^► Du hast den Motor (?<state>gestartet|abgestellt)\\.$");

    /**
     * Regex pattern for parsing the gear state from action bar messages. Matches messages showing the current gear (P, D, or R).
     */
    private final static Pattern ACTION_BAR_PATTERN = compile("► §7Gang§8: §f(?<gear>[PDR]) §8×");

    private final GermanRPCarAddon addon;

    /**
     * Processes incoming chat messages to update the state of the car's emergency lights and engine.
     *
     * <p>The method parses specific chat messages using {@link #EMERGENCY_LIGHTS_PATTERN} and {@link #ENGINE_PATTERN} to determine
     * and update the state of the emergency lights and engine.
     *
     * @param event the event containing the received chat message
     */
    @Subscribe
    public void onChatReceive(ChatReceiveEvent event) {
        String message = event.chatMessage().getPlainText();

        this.addon.carService().executeOnCar(car -> {
            Matcher emergencyLightsPatternMatcher = EMERGENCY_LIGHTS_PATTERN.matcher(message);
            if (emergencyLightsPatternMatcher.matches()) {
                boolean emergencyLightsEnabled = emergencyLightsPatternMatcher.group("state").equals("mit");
                car.setEmergencyLights(true);
                car.setEmergencyLightsEnabled(emergencyLightsEnabled);
                return;
            }

            Matcher enginePatternMatcher = ENGINE_PATTERN.matcher(message);
            if (enginePatternMatcher.matches()) {
                boolean engineRunning = enginePatternMatcher.group("state").equals("gestartet");
                car.setEngineRunning(engineRunning);

                if (!engineRunning) {
                    car.setGear(PARK);
                }
            }
        }, () -> {});
    }

    /**
     * Processes action bar updates to update the gear and engine state of the car.
     *
     * <p>The method uses {@link #ACTION_BAR_PATTERN} to extract the current gear from the action bar message and updates the car's
     * gear state accordingly. If the gear is set to "D" (Drive) or "R" (Reverse), the engine state is also updated to running.
     *
     * @param event the event containing the received action bar message
     */
    @Subscribe
    public void onActionBarReceive(ActionBarReceiveEvent event) {
        String message = plainUrl().serialize(event.getMessage());

        Matcher actionBarPatternMatcher = ACTION_BAR_PATTERN.matcher(message);
        if (actionBarPatternMatcher.find()) {
            String gear = actionBarPatternMatcher.group("gear");

            this.addon.carService().executeOnCar(car -> {
                switch (gear) {
                    case "P" -> car.setGear(PARK); // in PARK state, there is no safe clarification of engine state
                    case "D" -> {
                        car.setGear(DRIVE);
                        car.setEngineRunning(true);
                    }
                    case "R" -> {
                        car.setGear(REVERSE);
                        car.setEngineRunning(true);
                    }
                }
            }, () -> {});
        }
    }
}
