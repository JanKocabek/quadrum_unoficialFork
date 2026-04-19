package dmillerw.quadrum.common.lib.data;

import dmillerw.quadrum.Quadrum;
import net.minecraft.potion.Potion;
import org.apache.logging.log4j.Level;

public class Effect {
   private Potion potion;
   private boolean failed = false;
   public int id;
   public float probability;
   public int duration;
   public int amplifier;

   public Effect() {
   }

   public Potion getPotionEffect() {
      if (this.potion == null && !this.failed) {
         if (this.id >= 0 && this.id < Potion.potionTypes.length) {
            Potion potion1 = Potion.potionTypes[this.id];
            if (potion1 == null) {
               Quadrum.log(Level.WARN, "%s is an invalid potion id", this.id);
               return null;
            }

            this.potion = Potion.potionTypes[this.id];
         } else {
            Quadrum.log(Level.WARN, "%s is an invalid potion id", this.id);
            this.failed = true;
         }
      }

      return this.potion;
   }
}
