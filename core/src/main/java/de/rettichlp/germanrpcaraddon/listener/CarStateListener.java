package de.rettichlp.germanrpcaraddon.listener;

import de.rettichlp.germanrpcaraddon.GermanRPCarAddon;
import lombok.RequiredArgsConstructor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

@RequiredArgsConstructor
public class CarStateListener {

    private final static Pattern EMERGENCY_LIGHTS_PATTERN = compile("^► Du fährst jetzt (?<state>mit|wieder ohne) Sondersignal\\.$");
    private final static Pattern ENGINE_PATTERN = compile("^► Du hast den Motor (?<state>gestartet|abgestellt)\\.$");

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
}
