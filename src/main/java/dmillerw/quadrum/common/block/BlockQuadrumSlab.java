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
import net.minecraft.block.BlockSlab;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockQuadrumSlab extends BlockSlab implements IQuadrumBlock {
   public final String name;

   public BlockQuadrumSlab(BlockData data) {
      super(false, data.getBlockMaterial());
      this.name = data.name;
      this.setStepSound(data.getBlockSound());
      this.setHardness(data.hardness);
      this.setResistance(data.resistance);
      this.setBlockName(data.name);
      this.setCreativeTab(TabQuadrum.BLOCK);
      this.setLightOpacity(0);
      if (data.requiresTool) {
         this.setHarvestLevel(data.getHarvestTool(), data.miningLevel);
      }
   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return BlockLoader.blockDataMap.get(this.name).transparent ? 1 : 0;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public void registerBlockIcons(IIconRegister p_149651_1_) {
   }

   public IIcon getIcon(int side, int meta) {
      return TextureLoader.getBlockIcon(BlockLoader.blockDataMap.get(this.name), "default");
   }

   public String func_150002_b(int p_150002_1_) {
      return "";
   }

   @SideOnly(Side.CLIENT)
   public Item getItem(World world, int x, int y, int z) {
      return Item.getItemFromBlock(this);
   }

   public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      return BlockStaticMethodHandler.getDrops(this, BlockLoader.blockDataMap.get(this.name), world, x, y, z, metadata, fortune);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return BlockLoader.blockDataMap.get(this.name).collision ? super.getCollisionBoundingBoxFromPool(world, x, y, z) : null;
   }

   @Override
   public String getName() {
      return this.name;
   }
}
