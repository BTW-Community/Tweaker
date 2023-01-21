package issame.tweaker;

import btw.AddonHandler;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class PreLaunchInitializer implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        TweakerAddon.getInstance();
        AddonHandler.registerCommand(new TweakerCommand(), false);
    }
}
