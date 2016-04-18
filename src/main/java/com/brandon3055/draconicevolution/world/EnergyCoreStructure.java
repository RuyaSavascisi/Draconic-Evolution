package com.brandon3055.draconicevolution.world;

import com.brandon3055.brandonscore.blocks.TileBCBase;
import com.brandon3055.brandonscore.lib.MultiBlockStorage;
import com.brandon3055.brandonscore.utills.ModelUtills;
import com.brandon3055.brandonscore.utills.MultiBlockHelper;
import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.blocks.tileentity.TileEnergyStorageCore;
import com.brandon3055.draconicevolution.blocks.tileentity.TileInvisECoreBlock;
import com.brandon3055.draconicevolution.utills.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by brandon3055 on 1/4/2016.
 */
public class EnergyCoreStructure extends MultiBlockHelper {
    private final int FLAG_RENDER = 0;
    private final int FLAG_FORME = 1;
    private final int FLAG_REVERT = 2;
    private MultiBlockStorage[] structureTiers = new MultiBlockStorage[8];
    private TileEnergyStorageCore core;

    public EnergyCoreStructure initialize(TileEnergyStorageCore core) {
        this.core = core;
        structureTiers[0] = buildTier1();
        structureTiers[1] = buildTier2();
        structureTiers[2] = buildTier3();
        structureTiers[3] = buildTier4();
        structureTiers[4] = buildTier5();
        structureTiers[5] = buildTier6();
        structureTiers[6] = buildTier7();
        structureTiers[7] = buildTierOMG();
        return this;
    }

    public boolean checkTier(int tier) {
        BlockPos offset = getCoreOffset(tier);

        switch (tier) {
            case 1:
                return structureTiers[0].checkStructure(core.getWorld(), core.getPos().add(offset));
            case 2:
                return structureTiers[1].checkStructure(core.getWorld(), core.getPos().add(offset));
            case 3:
                return structureTiers[2].checkStructure(core.getWorld(), core.getPos().add(offset));
            case 4:
                return structureTiers[3].checkStructure(core.getWorld(), core.getPos().add(offset));
            case 5:
                return structureTiers[4].checkStructure(core.getWorld(), core.getPos().add(offset));
            case 6:
                return structureTiers[5].checkStructure(core.getWorld(), core.getPos().add(offset));
            case 7:
                return structureTiers[6].checkStructure(core.getWorld(), core.getPos().add(offset));
            case 8:
                return structureTiers[7].checkStructure(core.getWorld(), core.getPos().add(offset));
        }
        if (tier <= 0) {
            LogHelper.error("[EnergyCoreStructure] Tier value to small. As far as TileEnergyStorageCore is concerned the tiers now start at 1 not 0. This class automatically handles the conversion now");
        } else {
            LogHelper.error("[EnergyCoreStructure#checkTeir] What exactly were you expecting after Tier 8? Infinity.MAX_VALUE?");
        }


        return false;
    }

    public void placeTier(int tier) {
        BlockPos offset = getCoreOffset(tier);

        switch (tier) {
            case 1:
                structureTiers[0].placeStructure(core.getWorld(), core.getPos().add(offset));
                return;
            case 2:
                structureTiers[1].placeStructure(core.getWorld(), core.getPos().add(offset));
                return;
            case 3:
                structureTiers[2].placeStructure(core.getWorld(), core.getPos().add(offset));
                return;
            case 4:
                structureTiers[3].placeStructure(core.getWorld(), core.getPos().add(offset));
                return;
            case 5:
                structureTiers[4].placeStructure(core.getWorld(), core.getPos().add(offset));
                return;
            case 6:
                structureTiers[5].placeStructure(core.getWorld(), core.getPos().add(offset));
                return;
            case 7:
                structureTiers[6].placeStructure(core.getWorld(), core.getPos().add(offset));
                return;
            case 8:
                structureTiers[7].placeStructure(core.getWorld(), core.getPos().add(offset));
                return;
        }
        if (tier <= 0) {
            LogHelper.error("[EnergyCoreStructure] Tier value to small. As far as TileEnergyStorageCore is concerned the tiers now start at 1 not 0. This class automatically handles the conversion now");
        } else {
            LogHelper.error("[EnergyCoreStructure#placeTier] What exactly were you expecting after Tier 8? Infinity.MAX_VALUE?");
        }
    }

    public void renderTier(int tier) {
        forTier(tier, FLAG_RENDER);
    }

    public void formTier(int tier) {
        forTier(tier, FLAG_FORME);
    }

    public void revertTier(int tier) {
        forTier(tier, FLAG_REVERT);
    }

    private void forTier(int tier, int flag) {
        tier -= 1;
        if (tier < 0) {
            LogHelper.error("[EnergyCoreStructure] Tier value to small. As far as TileEnergyStorageCore is concerned the tiers now start at 1 not 0. This class automatically handles the conversion now");
        } else if (tier >= structureTiers.length) {
            LogHelper.error("[EnergyCoreStructure#placeTier] What exactly were you expecting after Tier 8? Infinity.MAX_VALUE?");
        } else {
            structureTiers[tier].forEachInStructure(core.getWorld(), core.getPos().add(getCoreOffset(tier + 1)), flag);
        }
    }

    @Override
    public void forBlock(String name, World world, BlockPos pos, BlockPos startPos, int flag) {
        if (name.isEmpty() || name.equals("draconicevolution:energyStorageCore")) return;

        //region Render Build Guide

        if (flag == FLAG_RENDER) {//todo find a way to render these from the center out (Maby try rendering them relative to haw far from the player they are)
            Block block = Block.blockRegistry.getObject(new ResourceLocation(name));

            if (block == null || name.equals("") || name.equals("air")) {
                return;
            }

            BlockPos translation = new BlockPos(pos.getX() - startPos.getX(), pos.getY() - startPos.getY(), pos.getZ() - startPos.getZ());
            translation = translation.add(getCoreOffset(core.tier.value));

            IBlockState state = block.getDefaultState();
            Tessellator tessellator = Tessellator.getInstance();

            GlStateManager.pushMatrix();
            GlStateManager.translate(translation.getX(), translation.getY(), translation.getZ());
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.scale(0.9, 0.9, 0.9);
            GlStateManager.translate(0.05, 0.05, 0.05);
            float brightnessX = OpenGlHelper.lastBrightnessX;
            float brightnessY = OpenGlHelper.lastBrightnessY;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 150f, 150f);

            List<BakedQuad> blockQuads = ModelUtills.getModelQuads(state);
            ModelUtills.renderQuadsARGB(tessellator, blockQuads, 0x80FFFFFF);

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightnessX, brightnessY);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        //endregion

        //region Activate

        else if (flag == FLAG_FORME) {
            world.setBlockState(pos, DEFeatures.invisECoreBlock.getDefaultState());
            TileInvisECoreBlock tile = TileBCBase.getCastTileAt(world, pos, TileInvisECoreBlock.class);
            if (tile != null) {
                tile.blockName = name;
                tile.setCore(core);
            }
        }

        //endregion

        //region Deactivate

        else if (flag == FLAG_REVERT) {
            TileInvisECoreBlock tile = TileBCBase.getCastTileAt(world, pos, TileInvisECoreBlock.class);
            if (tile != null) {
                tile.revert();
            }
        }

        //endregion
    }

    //region Structure Builders

    private MultiBlockStorage buildTier1() {
        MultiBlockStorage storage = new MultiBlockStorage(1, this);
        String X = "draconicevolution:energyStorageCore";

        storage.addRow(X);

        return storage;
    }

    private MultiBlockStorage buildTier2() {
        MultiBlockStorage storage = new MultiBlockStorage(3, this);
        String e = "";
        String X = "draconicevolution:energyStorageCore";
        String D = "draconicevolution:draconiumBlock";

        storage.addRow(e, e, e);
        storage.addRow(e, D, e);
        storage.addRow(e, e, e);

        storage.newLayer();
        storage.addRow(e, D, e);
        storage.addRow(D, X, D);
        storage.addRow(e, D, e);

        storage.newLayer();
        storage.addRow(e, e, e);
        storage.addRow(e, D, e);
        storage.addRow(e, e, e);

        return storage;
    }

    private MultiBlockStorage buildTier3() {
        MultiBlockStorage storage = new MultiBlockStorage(3, this);
        String X = "draconicevolution:energyStorageCore";
        String D = "draconicevolution:draconiumBlock";

        storage.addRow(D, D, D);
        storage.addRow(D, D, D);
        storage.addRow(D, D, D);

        storage.newLayer();
        storage.addRow(D, D, D);
        storage.addRow(D, X, D);
        storage.addRow(D, D, D);

        storage.newLayer();
        storage.addRow(D, D, D);
        storage.addRow(D, D, D);
        storage.addRow(D, D, D);

        return storage;
    }

    private MultiBlockStorage buildTier4() {
        MultiBlockStorage storage = new MultiBlockStorage(5, this);
        String e = "";
        String X = "draconicevolution:energyStorageCore";
        String D = "draconicevolution:draconiumBlock";
        String R = "minecraft:redstone_block";

        storage.addRow(e, e, e, e, e);
        storage.addRow(e, D, D, D, e);
        storage.addRow(e, D, D, D, e);
        storage.addRow(e, D, D, D, e);
        storage.addRow(e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, D, D, D, e);
        storage.addRow(D, R, R, R, D);
        storage.addRow(D, R, R, R, D);
        storage.addRow(D, R, R, R, D);
        storage.addRow(e, D, D, D, e);

        storage.newLayer();
        storage.addRow(e, D, D, D, e);
        storage.addRow(D, R, R, R, D);
        storage.addRow(D, R, X, R, D);
        storage.addRow(D, R, R, R, D);
        storage.addRow(e, D, D, D, e);

        storage.newLayer();
        storage.addRow(e, D, D, D, e);
        storage.addRow(D, R, R, R, D);
        storage.addRow(D, R, R, R, D);
        storage.addRow(D, R, R, R, D);
        storage.addRow(e, D, D, D, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e);
        storage.addRow(e, D, D, D, e);
        storage.addRow(e, D, D, D, e);
        storage.addRow(e, D, D, D, e);
        storage.addRow(e, e, e, e, e);

        return storage;
    }

    private MultiBlockStorage buildTier5() {
        MultiBlockStorage storage = new MultiBlockStorage(7, this);
        String e = "";
        String X = "draconicevolution:energyStorageCore";
        String D = "draconicevolution:draconiumBlock";
        String R = "minecraft:redstone_block";

        storage.addRow(e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(D, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, D);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(e, e, D, D, D, e, e);

        storage.newLayer();
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(D, R, R, R, R, R, D);
        storage.addRow(D, R, R, X, R, R, D);
        storage.addRow(D, R, R, R, R, R, D);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(e, e, D, D, D, e, e);

        storage.newLayer();
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(D, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, D);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(e, e, D, D, D, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, D, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, e, D, D, D, e, e);
        storage.addRow(e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e);

        return storage;
    }

    private MultiBlockStorage buildTier6() {
        MultiBlockStorage storage = new MultiBlockStorage(9, this);
        String e = "";
        String X = "draconicevolution:energyStorageCore";
        String D = "draconicevolution:draconiumBlock";
        String R = "minecraft:redstone_block";

        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, D, D, D, e, e, e);
        storage.addRow(e, e, e, D, D, D, e, e, e);
        storage.addRow(e, e, e, D, D, D, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, D, D, D, D, D, e, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, e, D, D, D, D, D, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, D, D, D, D, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, D, D, D, D, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, D, D, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(D, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, D);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, D, D, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, D, D, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(D, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, X, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, D);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, D, D, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, D, D, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(D, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, D);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, D, D, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, D, D, D, D, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, D, D, D, D, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, D, D, D, D, D, e, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, D, e, e);
        storage.addRow(e, e, D, D, D, D, D, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, D, D, D, e, e, e);
        storage.addRow(e, e, e, D, D, D, e, e, e);
        storage.addRow(e, e, e, D, D, D, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e);

        return storage;
    }

    private MultiBlockStorage buildTier7() {
        MultiBlockStorage storage = new MultiBlockStorage(11, this);
        String e = "";
        String X = "draconicevolution:energyStorageCore";
        String D = "draconicevolution:draconiumBlock";
        String R = "minecraft:redstone_block";


        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(D, R, R, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, R, R, D);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);

        //Centre
        storage.newLayer();
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(D, R, R, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, X, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, R, R, D);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(D, R, R, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, R, R, D);
        storage.addRow(D, R, R, R, R, R, R, R, R, R, D);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, D, R, R, R, R, R, R, R, D, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, D, R, R, R, R, R, D, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, e, D, R, R, R, D, e, e, e);
        storage.addRow(e, e, e, D, D, D, D, D, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);
        storage.addRow(e, e, e, e, D, D, D, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e);

        return storage;
    }

    private MultiBlockStorage buildTierOMG() {
        MultiBlockStorage storage = new MultiBlockStorage(13, this);
        String e = "";
        String X = "draconicevolution:energyStorageCore";
        String A = "draconicevolution:draconicBlock";
        String D = "draconicevolution:draconicBlock";

        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, A, A, A, A, A, A, A, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, A, A, A, A, A, A, A, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, A, A, A, A, A, A, A, A, A, e, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, e, A, A, A, A, A, A, A, A, A, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);

        //Centre
        storage.newLayer();
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, X, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(A, A, D, D, D, D, D, D, D, D, D, A, A);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, A, A, A, A, A, A, A, A, A, e, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, A, A, D, D, D, D, D, D, D, A, A, e);
        storage.addRow(e, e, A, A, A, A, A, A, A, A, A, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, A, A, A, A, A, A, A, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, D, D, D, D, D, A, A, e, e);
        storage.addRow(e, e, A, A, A, A, A, A, A, A, A, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, A, A, A, A, A, A, A, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);

        storage.newLayer();
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, A, A, A, A, A, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);
        storage.addRow(e, e, e, e, e, e, e, e, e, e, e, e, e);

        return storage;
    }

    //endregion

    @Override
    public boolean checkBlock(String name, World world, BlockPos pos) {
        TileInvisECoreBlock tile = TileBCBase.getCastTileAt(world, pos, TileInvisECoreBlock.class);
        if (tile != null && tile.blockName.equals(name)) {
            return true;
        } else {
            return super.checkBlock(name, world, pos); //TODO check for the "place holder" blocks when core is active
        }
    }

    private BlockPos getCoreOffset(int tier) {
        int offset = tier == 1 ? 0 : tier == 2 || tier == 3 ? -1 : -(tier - 2);
        return new BlockPos(offset, offset, offset);
    }

    @Override
    public void setBlock(String name, World world, BlockPos pos) {
        if (!name.equals("draconicevolution:energyStorageCore") && name.length() > 0) {
            super.setBlock(name, world, pos);
        }
    }
}