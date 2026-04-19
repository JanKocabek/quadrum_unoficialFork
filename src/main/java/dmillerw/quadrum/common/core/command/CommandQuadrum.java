package dmillerw.quadrum.common.core.command;

import dmillerw.quadrum.common.block.data.BlockData;
import dmillerw.quadrum.common.block.data.BlockLoader;
import dmillerw.quadrum.common.item.data.ItemData;
import dmillerw.quadrum.common.item.data.ItemLoader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandQuadrum extends CommandBase {
   public CommandQuadrum() {
   }

   public String getCommandName() {
      return "quadrum";
   }

   public String getCommandUsage(ICommandSender sender) {
      return "/quadrum reload";
   }

   public int getRequiredPermissionLevel() {
      return 3;
   }

   public void processCommand(ICommandSender sender, String[] args) {
      if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
         BlockLoader.initialize();

         for (BlockData blockData : BlockLoader.blockDataMap.values()) {
            blockData.reload(BlockLoader.blockMap.get(blockData.name));
         }

         ItemLoader.initialize();

         for (ItemData itemData : ItemLoader.itemDataMap.values()) {
            itemData.reload(ItemLoader.itemMap.get(itemData.name));
         }
      } else {
         throw new WrongUsageException(this.getCommandUsage(sender), new Object[0]);
      }
   }
}
