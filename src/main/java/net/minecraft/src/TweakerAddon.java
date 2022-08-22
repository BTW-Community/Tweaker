package net.minecraft.src;

public class TweakerAddon extends FCAddOn {
    private static TweakerAddon instance;

    private TweakerAddon() {
        super("Tweaker", "1.3.0", "tweaker");
    }

    @Override
    public void Initialize() {
        FCAddOnHandler.LogMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    public static TweakerAddon getInstance() {
        if (instance == null)
            instance = new TweakerAddon();
        return instance;
    }
}
