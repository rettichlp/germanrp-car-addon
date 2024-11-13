package de.rettichlp.germanrpcaraddon;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@ConfigName("settings")
@Getter
@Accessors(fluent = true)
public class GermanRPCarAddonConfiguration extends AddonConfig {

    @SwitchSetting
    private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

    @SwitchSetting
    private final ConfigProperty<Boolean> debug = new ConfigProperty<>(false);
}
