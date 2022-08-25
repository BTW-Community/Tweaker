package issame.tweaker;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class PreLaunchInitializer implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        TweakerAddon.getInstance();
    }
}
