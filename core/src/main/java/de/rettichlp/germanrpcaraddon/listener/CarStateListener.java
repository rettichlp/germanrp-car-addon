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

@RequiredArgsConstructor
public class CarStateListener {

    private final static Pattern EMERGENCY_LIGHTS_PATTERN = compile("^► Du fährst jetzt (?<state>mit|wieder ohne) Sondersignal\\.$");
    private final static Pattern ENGINE_PATTERN = compile("^► Du hast den Motor (?<state>gestartet|abgestellt)\\.$");
    private final static Pattern ACTION_BAR_PATTERN = compile("► §7Gang§8: §f(?<gear>[PDR]) §8×");

    private final GermanRPCarAddon addon;

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
            }
        }, () -> {});
    }

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
