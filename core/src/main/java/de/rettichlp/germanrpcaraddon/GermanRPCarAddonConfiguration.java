package de.rettichlp.germanrpcaraddon;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

/**
 * The configuration settings for the GermanRP Car Addon.
 *
 * @author RettichLP
 */
@ConfigName("settings")
@Getter
@Accessors(fluent = true)
public class GermanRPCarAddonConfiguration extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SettingSection("comfort-systems")
    @SwitchSetting
    private final ConfigProperty<Boolean> pressToStart = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> automaticGearbox = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> remoteEngineStart = new ConfigProperty<>(true);

    @SettingSection("assistance-systems")
    //@SwitchSetting
    private final ConfigProperty<Boolean> automaticEmergencyBraking = new ConfigProperty<>(true);

    @SettingSection("development")
    @SwitchSetting
    private final ConfigProperty<Boolean> debug = new ConfigProperty<>(false);
}
