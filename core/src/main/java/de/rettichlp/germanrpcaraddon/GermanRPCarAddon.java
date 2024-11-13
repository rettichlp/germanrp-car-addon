package de.rettichlp.germanrpcaraddon;

import de.rettichlp.germanrpcaraddon.base.AddonPlayer;
import de.rettichlp.germanrpcaraddon.base.DefaultAddonPlayer;
import de.rettichlp.germanrpcaraddon.controllers.CarController;
import de.rettichlp.germanrpcaraddon.controllers.MinecraftController;
import de.rettichlp.germanrpcaraddon.core.generated.DefaultReferenceStorage;
import de.rettichlp.germanrpcaraddon.listener.ChangeGearListener;
import de.rettichlp.germanrpcaraddon.listener.ChangeSirenListener;
import de.rettichlp.germanrpcaraddon.listener.DoubleKeyPressListener;
import de.rettichlp.germanrpcaraddon.listener.ScreenUpdateListener;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

import static net.labymod.api.client.component.Component.empty;
import static net.labymod.api.client.component.Component.text;
import static net.labymod.api.client.component.format.NamedTextColor.DARK_GRAY;
import static net.labymod.api.client.component.format.NamedTextColor.GOLD;
import static net.labymod.api.client.component.format.NamedTextColor.YELLOW;

@AddonMain
@Getter
@Accessors(fluent = true)
public class GermanRPCarAddon extends LabyAddon<GermanRPCarAddonConfiguration> {

    private AddonPlayer player;
    private CarController carController;
    private MinecraftController minecraftController;

    /**
     * Outputs a debug message to the logger and optionally displays it in-game if debugging is enabled in the configuration settings.
     *
     * <p>The method first logs the provided message using the addon’s logger. If the
     * debug mode is active (as determined by the configuration), it formats the message with a "[Debug]" label and displays it in-game
     * for easy visibility.
     *
     * @param message The debug message to log and display.
     */
    public void debug(String message) {
        this.logger().debug(message);

        if (this.configuration().debug().get()) {
            this.displayMessage(empty()
                    .append(text("[", DARK_GRAY))
                    .append(text("Debug", GOLD))
                    .append(text("] ", DARK_GRAY))
                    .append(text(message, YELLOW)));
        }
    }

    @Override
    protected void load() {
        this.player = new DefaultAddonPlayer(this);

        this.logger().info("Enabled germanrp-car-addon");
    }

    @Override
    protected void enable() {
        this.carController = ((DefaultReferenceStorage) this.referenceStorageAccessor()).getCarController();
        this.minecraftController = ((DefaultReferenceStorage) this.referenceStorageAccessor()).getMinecraftController();

        this.registerSettingCategory();

        this.registerListener(new ChangeGearListener(this));
        this.registerListener(new ChangeSirenListener(this));
        this.registerListener(new DoubleKeyPressListener(this));
        this.registerListener(new ScreenUpdateListener(this));

        this.logger().info("Enabled germanrp-car-addon");
    }

    @Override
    protected Class<GermanRPCarAddonConfiguration> configurationClass() {
        return GermanRPCarAddonConfiguration.class;
    }
}
