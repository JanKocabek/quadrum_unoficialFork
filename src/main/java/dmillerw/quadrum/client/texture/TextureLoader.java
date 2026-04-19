package dmillerw.quadrum.client.texture;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dmillerw.quadrum.Quadrum;
import dmillerw.quadrum.common.block.data.BlockData;
import dmillerw.quadrum.common.block.data.BlockLoader;
import dmillerw.quadrum.common.item.data.ItemData;
import dmillerw.quadrum.common.item.data.ItemLoader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent.Post;
import net.minecraftforge.client.event.TextureStitchEvent.Pre;
import org.apache.logging.log4j.Level;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureLoader {
   public static final TextureLoader INSTANCE = new TextureLoader();
   private TextureMap blockMap;
   private TextureMap itemMap;
   private Map<String, CustomAtlasSprite> blockMapping;
   private Map<String, CustomAtlasSprite> itemMapping;

   public TextureLoader() {
   }

   public static IIcon getBlockIcon(BlockData blockData, String side) {
      IIcon icon = internalGetBlockIcon(blockData, side);
      return icon != null ? icon : missing();
   }

   public static IIcon getItemIcon(ItemData itemData) {
      IIcon icon = internalGetItemIcon(itemData);
      return icon != null ? icon : missing();
   }

   public static IIcon internalGetBlockIcon(BlockData data, String side) {
      if (side.equalsIgnoreCase("default")) {
         return INSTANCE.getBlockIcon(data.defaultTexture);
      } else {
         return data.textureInfo.containsKey(side) ? INSTANCE.getBlockIcon(data.textureInfo.get(side)) : INSTANCE.getBlockIcon(data.defaultTexture);
      }
   }

   public static IIcon internalGetItemIcon(ItemData data) {
      return INSTANCE.getItemIcon(data.texture);
   }

   private static IIcon missing() {
      return ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
   }

   private void registerBlockIcon(String string) {
      if (this.blockMap.getTextureExtry("quadrum:" + string) == null && !this.blockMapping.containsKey(string)) {
         CustomAtlasSprite icon = new CustomAtlasSprite(string, true);
         this.blockMap.setTextureEntry("quadrum:" + string, icon);
         this.blockMapping.put(string, icon);
      }
   }

   private void registerItemIcon(String string) {
      if (this.itemMap.getTextureExtry("quadrum:" + string) == null && !this.itemMapping.containsKey(string)) {
         CustomAtlasSprite icon = new CustomAtlasSprite(string, false);
         this.itemMap.setTextureEntry("quadrum:" + string, icon);
         this.itemMapping.put(string, icon);
      }
   }

   public IIcon getBlockIcon(String name) {
      return (IIcon)this.blockMapping.get(name);
   }

   public IIcon getItemIcon(String name) {
      return (IIcon)this.itemMapping.get(name);
   }

   public void removeBlockIcon(String name) {
      this.blockMap.setTextureEntry("quadrum:" + name, null);
      this.blockMapping.remove(name);
   }

   public void removeItemIcon(String name) {
      this.itemMap.setTextureEntry("quadrum:" + name, null);
      this.itemMapping.remove(name);
   }

   @SubscribeEvent
   public void onTextureStich(Pre event) {
      if (event.map.getTextureType() == 0) {
         this.blockMap = event.map;
         this.blockMapping = Maps.newHashMap();

         for (BlockData block : BlockLoader.blockDataMap.values()) {
            this.registerBlockIcon(block.defaultTexture);

            for (String string : block.textureInfo.values()) {
               this.registerBlockIcon(string);
            }
         }
      } else if (event.map.getTextureType() == 1) {
         this.itemMap = event.map;
         this.itemMapping = Maps.newHashMap();

         for (ItemData item : ItemLoader.itemDataMap.values()) {
            this.registerItemIcon(item.texture);
         }
      }
   }

   @SubscribeEvent
   public void onTextureStitched(Post event) {
      if (event.map.getTextureType() == 0) {
         for (CustomAtlasSprite customAtlasSprite : this.blockMapping.values()) {
            customAtlasSprite.restore();
         }

         if (Quadrum.dumpBlockMap) {
            File file = new File(Quadrum.configDir, "block_out.png");
            this.dumpTexture(file);
            Quadrum.log(Level.INFO, "Dumping block texture map to " + file.getAbsolutePath());
         }
      } else if (event.map.getTextureType() == 1) {
         for (CustomAtlasSprite customAtlasSprite : this.itemMapping.values()) {
            customAtlasSprite.restore();
         }

         if (Quadrum.dumpItemMap) {
            File file = new File(Quadrum.configDir, "item_out.png");
            this.dumpTexture(file);
            Quadrum.log(Level.INFO, "Dumping item texture map to " + file.getAbsolutePath());
         }
      }
   }

   private void dumpTexture(File file) {
      int format = GL11.glGetTexLevelParameteri(3553, 0, 4099);
      int width = GL11.glGetTexLevelParameteri(3553, 0, 4096);
      int height = GL11.glGetTexLevelParameteri(3553, 0, 4097);
      int channels = 0;
      int byteCount = 0;
      byte var17;
      switch (format) {
         case 6407:
            var17 = 3;
            break;
         case 6408:
         default:
            var17 = 4;
      }

      byteCount = width * height * var17;
      ByteBuffer bytes = BufferUtils.createByteBuffer(byteCount);
      BufferedImage image = new BufferedImage(width, height, 1);
      GL11.glGetTexImage(3553, 0, format, 5121, bytes);
      String ext = "PNG";

      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            int i = (x + width * y) * var17;
            int r = bytes.get(i) & 255;
            int g = bytes.get(i + 1) & 255;
            int b = bytes.get(i + 2) & 255;
            image.setRGB(x, height - (y + 1), 0xFF000000 | r << 16 | g << 8 | b);
         }
      }

      try {
         ImageIO.write(image, "PNG", file);
      } catch (IOException var16) {
         var16.printStackTrace();
      }
   }
}
