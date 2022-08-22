package issame.tweaker;

import com.prupe.mcpatcher.MCLogger;
import net.fabricmc.api.ModInitializer;

public class TweakerMod implements ModInitializer {
    public static final MCLogger LOGGER = MCLogger.getLogger("tweaker");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing...");
    }
}
