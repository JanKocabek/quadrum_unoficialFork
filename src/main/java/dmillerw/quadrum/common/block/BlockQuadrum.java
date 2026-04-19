package dmillerw.quadrum.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dmillerw.quadrum.client.texture.TextureLoader;
import dmillerw.quadrum.common.block.data.BlockData;
import dmillerw.quadrum.common.block.data.BlockLoader;
import dmillerw.quadrum.common.lib.BlockStaticMethodHandler;
import dmillerw.quadrum.common.lib.IQuadrumBlock;
import dmillerw.quadrum.common.lib.TabQuadrum;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockQuadrum extends Block implements IQuadrumBlock {
   public final String name;
   public final BlockData data;
   public BlockQuadrum(BlockData data) {
      super(data.getBlockMaterial());
      this.data=data;
      this.name = data.name;
      this.setStepSound(data.getBlockSound());
      this.setLightLevel(data.lightLevel / 15.0F);
      this.setHardness(data.hardness);
      this.setResistance(data.resistance);
      this.setBlockName(data.name);
      this.setCreativeTab(TabQuadrum.BLOCK);
      this.opaque = !data.transparent;
      this.lightOpacity = !data.transparent ? 255 : 0;
      if (data.requiresTool) {
         this.setHarvestLevel(data.getHarvestTool(), data.miningLevel);
      }
   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return BlockLoader.blockDataMap.get(this.name).transparent ? 1 : 0;
   }

   public void registerBlockIcons(IIconRegister p_149651_1_) {
   }

   public IIcon getIcon(int side, int meta) {
      ForgeDirection forgeSide = ForgeDirection.getOrientation(side);
      ForgeDirection front = ForgeDirection.getOrientation(meta);
      if (meta == 0) {
         front = ForgeDirection.SOUTH;
      }

      BlockData data = BlockLoader.blockDataMap.get(this.name);
      if (forgeSide == front) {
         return TextureLoader.getBlockIcon(data, "front");
      } else if (forgeSide == front.getRotation(ForgeDirection.UP)) {
         return TextureLoader.getBlockIcon(data, "left");
      } else if (forgeSide == front.getRotation(ForgeDirection.UP).getOpposite()) {
         return TextureLoader.getBlockIcon(data, "right");
      } else if (forgeSide == front.getOpposite()) {
         return TextureLoader.getBlockIcon(data, "back");
      } else if (side == 0) {
         return TextureLoader.getBlockIcon(data, "bottom");
      } else {
         return side == 1 ? TextureLoader.getBlockIcon(data, "top") : TextureLoader.getBlockIcon(data, "default");
      }
   }

   public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
      return this.getIcon(side, world.getBlockMetadata(x, y, z));
   }

   public boolean renderAsNormalBlock() {
      return !BlockLoader.blockDataMap.get(this.name).transparent;
   }

   public boolean isOpaqueCube() {
      return BlockLoader.blockDataMap.get(this.name) != null && !BlockLoader.blockDataMap.get(this.name).transparent;
   }

   public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      return BlockStaticMethodHandler.getDrops(this, BlockLoader.blockDataMap.get(this.name), world, x, y, z, metadata, fortune);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return BlockLoader.blockDataMap.get(this.name).collision ? super.getCollisionBoundingBoxFromPool(world, x, y, z) : null;
   }

   public boolean canProvidePower() {
      return BlockLoader.blockDataMap.get(this.name).redstoneLevel > 0;
   }

   public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
      return BlockLoader.blockDataMap.get(this.name).redstoneLevel;
   }

   public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      return BlockLoader.blockDataMap.get(this.name).flammable;
   }

   public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
      return BlockLoader.blockDataMap.get(this.name).soil;
   }

   @Override
   public String getName() {
      return this.name;
   }
}
