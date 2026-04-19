package dmillerw.quadrum.common.lib.data;

import cpw.mods.fml.common.registry.GameData;
import java.util.Random;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;

public class Drop {
   private static Random random = new Random();
   private Item dropItem;
   public String item;
   public int damage = 0;
   public String amount = "1";

   public Drop() {
   }

   public Item getDrop() {
      if (this.dropItem == null) {
         this.dropItem = (Item)GameData.getItemRegistry().getRaw(this.item);
      }

      return this.dropItem;
   }

   public int getDropAmount() {
      if (!this.amount.contains("-")) {
         return Integer.parseInt(this.amount);
      } else {
         int low = Integer.parseInt(this.amount.substring(0, this.amount.indexOf("-")));
         int high = Integer.parseInt(this.amount.substring(this.amount.indexOf("-") + 1, this.amount.length()));
         return MathHelper.getRandomIntegerInRange(random, low, high);
      }
   }
}
