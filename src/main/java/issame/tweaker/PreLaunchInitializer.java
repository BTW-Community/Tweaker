package issame.tweaker;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.src.ServerCommandManager;

public class PreLaunchInitializer implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        TweakerAddon.getInstance();
        ServerCommandManager.registerAddonCommand(new TweakerCommand());
    }
}
