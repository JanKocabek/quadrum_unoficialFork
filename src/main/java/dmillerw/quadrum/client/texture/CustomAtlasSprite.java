package dmillerw.quadrum.client.texture;

import dmillerw.quadrum.Quadrum;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

public class CustomAtlasSprite extends TextureAtlasSprite {
   private int lastMapWidth;
   private int lastMapHeight;
   private int lastWidth;
   private int lastHeight;
   private int lastX;
   private int lastY;
   private boolean block;

   protected CustomAtlasSprite(String name, boolean block) {
      super(name);
      this.block = block;
   }

   public void initSprite(int mapWidth, int mapHeight, int originX, int originY, boolean rotated) {
      super.initSprite(mapWidth, mapHeight, originX, originY, rotated);
      this.lastMapWidth = mapWidth;
      this.lastMapHeight = mapHeight;
      this.lastX = originX;
      this.lastY = originY;
   }

   public void restore() {
      this.width = this.lastWidth;
      this.height = this.lastHeight;
      this.initSprite(this.lastMapWidth, this.lastMapHeight, this.lastX, this.lastY, false);
   }

   public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
      return true;
   }

   public boolean load(IResourceManager manager, ResourceLocation location) {
      BufferedImage image = null;

      try {
         if (this.block) {
            image = ImageIO.read(new File(Quadrum.blockTextureDir, location.getResourcePath() + ".png"));
         } else {
            image = ImageIO.read(new File(Quadrum.itemTextureDir, location.getResourcePath() + ".png"));
         }
      } catch (IOException var6) {
         Quadrum.log(
            Level.WARN, "Failed to load " + (this.block ? "block" : "item") + " texture %s. Reason: %s", location.getResourcePath() + ".png", var6.getMessage()
         );
         if (Quadrum.textureStackTrace) {
            var6.printStackTrace();
         }

         if (this.block) {
            TextureLoader.INSTANCE.removeBlockIcon(this.getIconName());
         } else {
            TextureLoader.INSTANCE.removeItemIcon(this.getIconName());
         }

         return true;
      }

      if (image != null) {
         this.lastWidth = image.getWidth();
         this.lastHeight = image.getHeight();
         GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
         BufferedImage[] array = new BufferedImage[1 + gameSettings.mipmapLevels];
         array[0] = image;
         this.loadSprite(array, null, gameSettings.anisotropicFiltering > 1.0F);
         return false;
      } else {
         return true;
      }
   }
}
