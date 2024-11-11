plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "de.rettichlp.germanrpcaraddon"
version = providers.environmentVariable("VERSION").getOrElse("1.0.0")

labyMod {
    defaultPackageName = "de.rettichlp.germanrpcaraddon" //change this to your main package name (used by all modules)

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    // devLogin = true
                }
            }
        }
    }

    addonInfo {
        namespace = "germanrp-car-addon"
        displayName = "GermanRP Car Addon"
        author = "RettichLP"
        description = "Assistance systems and other nice-to-have features for the cars on GermanRP"
        minecraftVersion = "1.21.1"
        version = rootProject.version.toString()
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}
