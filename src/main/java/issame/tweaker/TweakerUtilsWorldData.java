package issame.tweaker;

import net.minecraft.src.FCAddOnUtilsWorldData;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldServer;

public class TweakerUtilsWorldData implements FCAddOnUtilsWorldData {
    private static final String NAME = TweakerAddon.getInstance().getName();
    private static String currentWorld;

    @Override
    public void saveWorldDataToNBT(WorldServer world, NBTTagCompound tag) {
    }

    @Override
    public void loadWorldDataFromNBT(WorldServer world, NBTTagCompound tag) {
    }

    @Override
    public void saveGlobalDataToNBT(WorldInfo info, NBTTagCompound tag) {
        // When a new world is created, saveGlobalDataToNBT is called before loadGlobalDataFromNBT.
        // We need to check if the stored config is for this current world,
        // otherwise it would save the config from the last loaded world.
        if (!info.getWorldName().equals(currentWorld)) {
            Config.resetConfig();
        }
        tag.setTag(NAME, Config.toNBT());
    }

    @Override
    public void loadGlobalDataFromNBT(WorldInfo info, NBTTagCompound tag) {
        currentWorld = info.getWorldName();
        Config.resetConfig();
        if (tag.hasKey(NAME)) {
            Config.loadNBT(tag.getCompoundTag(NAME));
        }
    }

    @Override
    public void copyGlobalData(WorldInfo oldInfo, WorldInfo newInfo) {
    }

    @Override
    public String getFilename() {
        return null;
    }
}
