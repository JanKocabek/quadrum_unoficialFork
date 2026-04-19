
package dmillerw.quadrum.common.block.data;

import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import dmillerw.quadrum.common.lib.Required;
import dmillerw.quadrum.common.lib.TypeSpecific;
import dmillerw.quadrum.common.lib.TypeSpecific.Type;
import dmillerw.quadrum.common.lib.data.Drop;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class BlockData {
    private transient Material blockMaterial;
    private transient Block.SoundType blockSound;
    @Required
    public String name = "";
    @Required
    @SerializedName("default-texture")
    public String defaultTexture = "";
    @Required
    public String material = "";
    public String type = "block";
    @TypeSpecific({Type.BLOCK})
    @SerializedName("texture-info")
    public Map<String, String> textureInfo = Maps.newHashMap();
    @SerializedName("mob-drops")
    public Map<String, Float> mobDrops = Maps.newHashMap();
    @SerializedName("ore-dictionary")
    public String[] oreDictionary = new String[0];
    public Drop[] drops = new Drop[0];
    public float hardness = 2.0F;
    public float resistance = 2.0F;
    @TypeSpecific({Type.BLOCK})
    @SerializedName("light-level")
    public int lightLevel = 0;
    @TypeSpecific({Type.BLOCK})
    @SerializedName("redstone-level")
    public int redstoneLevel = 0;
    @SerializedName("burn-time")
    public int burnTime = 0;
    @SerializedName("max-stack-size")
    public int maxStackSize = 64;
    @SerializedName("mining-level")
    public int miningLevel = 0;
    public boolean transparent = false;
    public boolean collision = true;
    @TypeSpecific({Type.BLOCK})
    public boolean flammable = false;
    @TypeSpecific({Type.BLOCK})
    public boolean soil = false;
    @SerializedName("require-tool")
    public boolean requiresTool = true;
    @SerializedName("drops-self")
    public boolean dropsSelf = true;

    public BlockData() {
    }

    public TypeSpecific.Type getBlockType() {
        return Type.fromString(this.type, Type.BLOCK);
    }

    public Material getBlockMaterial() {
        if (this.blockMaterial == null) {
            if (!this.material.equalsIgnoreCase("stone") && !this.material.equalsIgnoreCase("rock")) {
                if (this.material.equalsIgnoreCase("wood")) {
                    this.blockMaterial = Material.wood;
                } else if (!this.material.equalsIgnoreCase("ground") && !this.material.equalsIgnoreCase("dirt") && !this.material.equalsIgnoreCase("grass")) {
                    if (!this.material.equalsIgnoreCase("iron") && !this.material.equalsIgnoreCase("metal")) {
                        this.blockMaterial = Material.rock;
                    } else {
                        this.blockMaterial = Material.iron;
                    }
                } else {
                    this.blockMaterial = Material.ground;
                }
            } else {
                this.blockMaterial = Material.rock;
            }
        }

        return this.blockMaterial;
    }

    public Block getSimilarBlock() {
        Material material1 = this.getBlockMaterial();
        if (material1 == Material.rock) {
            return Blocks.stone;
        } else if (material1 == Material.wood) {
            return Blocks.planks;
        } else if (material1 == Material.ground) {
            return Blocks.dirt;
        } else {
            return material1 == Material.iron ? Blocks.iron_block : Blocks.stone;
        }
    }

    public Block.SoundType getBlockSound() {
        if (this.blockSound == null) {
            Material material1 = this.getBlockMaterial();
            if (material1 == Material.rock) {
                this.blockSound = Block.soundTypeStone;
            } else if (material1 == Material.wood) {
                this.blockSound = Block.soundTypeWood;
            } else if (material1 == Material.ground) {
                this.blockSound = Block.soundTypeGrass;
            } else if (material1 == Material.iron) {
                this.blockSound = Block.soundTypeMetal;
            }
        }

        return this.blockSound;
    }

    public String getHarvestTool() {
        Material material1 = this.getBlockMaterial();
        if (material1 == Material.rock) {
            return "pickaxe";
        } else if (material1 == Material.wood) {
            return "axe";
        } else if (material1 == Material.ground) {
            return "shovel";
        } else {
            return material1 == Material.iron ? "pickaxe" : "pickaxe";
        }
    }

    public void reload(Block parent) {
    }
}
