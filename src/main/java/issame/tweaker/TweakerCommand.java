package issame.tweaker;

import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.WrongUsageException;

import java.util.List;

public class TweakerCommand extends CommandBase {
    private static final TweakerAddon tweaker = TweakerAddon.getInstance();

    @Override
    public String getCommandName() {
        return tweaker.getPrefix();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        switch (args.length) {
            case 0:
                processCommand(commandSender);
                break;
            case 1:
                processCommand(commandSender, args[0]);
                break;
            case 2:
                processCommand(commandSender, args[0], args[1]);
                break;
            default:
                throw new WrongUsageException(getCommandName());
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, Config.getKeys().toArray(new String[0]));
        }
        return super.addTabCompletionOptions(commandSender, args);
    }

    private void processCommand(ICommandSender commandSender) {
        commandSender.sendChatToPlayer(joinNiceString(Config.getKeys().toArray(new String[0])));
    }

    private void processCommand(ICommandSender commandSender, String key) {
        commandSender.sendChatToPlayer(key + " = " + Config.getValueString(key));
    }

    private void processCommand(ICommandSender commandSender, String key, String value) {
        Config.putValue(key, value, true);
        notifyAdmins(commandSender, key + " set to " + value, key, value);
    }
}
