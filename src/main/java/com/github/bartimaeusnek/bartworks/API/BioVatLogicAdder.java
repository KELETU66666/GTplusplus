/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.API;

import com.github.bartimaeusnek.bartworks.MainMod;
import gregtech.api.enums.Materials;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import static cpw.mods.fml.common.registry.GameRegistry.findBlock;

public final class BioVatLogicAdder {


    public static class RadioHatch {

        private static final HashSet<BioVatLogicAdder.MaterialSvPair> MaSv = new HashSet<>();
        private static final HashMap<ItemStack, Integer> IsSv = new HashMap<>();

        public static HashSet<BioVatLogicAdder.MaterialSvPair> getMaSv() {
            return RadioHatch.MaSv;
        }

        public static HashMap<ItemStack, Integer> getIsSv() {
            return RadioHatch.IsSv;
        }

        public static void setOverrideSvForMaterial(Materials m, Integer sv) {
            MaSv.add(new BioVatLogicAdder.MaterialSvPair(m, sv));
        }

        public static void giveItemStackRadioHatchAbilites(ItemStack stack, Integer sv) {
            IsSv.put(stack, sv);
        }

        public static int getMaxSv() {
            int ret = 150;
            Iterator it = BioVatLogicAdder.RadioHatch.getMaSv().iterator();
            while (it.hasNext()) {
                BioVatLogicAdder.MaterialSvPair pair = (BioVatLogicAdder.MaterialSvPair) it.next();
                if (pair.getSievert() > ret)
                    ret = pair.getSievert();
            }
            for (ItemStack is : RadioHatch.IsSv.keySet()) {
                if (RadioHatch.IsSv.get(is) > ret)
                    ret = RadioHatch.IsSv.get(is);
            }
            return ret;
        }

    }

    public static class BioVatGlass {

        private static final HashMap<BlockMetaPair, Byte> glasses = new HashMap<>();

        /**
         * @param sModname        The modid owning the block
         * @param sUnlocBlockName The name of the block itself
         * @param meta            The meta of the block
         * @param tier            the glasses Tier = Voltage tier (MIN 3)
         * @return if the block was found in the Block registry
         */
        public static boolean addCustomGlass(String sModname, String sUnlocBlockName, int meta, int tier) {
            Block block = findBlock(sModname, sUnlocBlockName);
            boolean ret = block != null;
            if (ret)
                BioVatGlass.glasses.put(new BlockMetaPair(block, (byte) meta), (byte) tier);
            else
                MainMod.LOGGER.warn("Block: " + sUnlocBlockName + " of the Mod: " + sModname + " was NOT found!");
            block = null;
            return ret;
        }

        /**
         * @param block the block to add
         * @param meta  the meta of the block (0-15)
         * @param tier  the glasses Tier = Voltage tier (MIN 3)
         */
        public static void addCustomGlass(@Nonnull Block block, @Nonnegative int meta, @Nonnegative int tier) {
            BioVatGlass.glasses.put(new BlockMetaPair(block, (byte) meta), (byte) tier);
        }

        /**
         * @param block the block to add
         * @param tier  the glasses Tier = Voltage tier (MIN 3)
         */
        public static void addCustomGlass(@Nonnull Block block, @Nonnegative int tier) {
            BioVatGlass.glasses.put(new BlockMetaPair(block, (byte) 0), (byte) tier);
        }

        /**
         * @param blockBytePair the block to add and its meta as a javafx.util Pair
         * @param tier          the glasses Tier = Voltage tier (MIN 3)
         */
        public static void addCustomGlass(@Nonnull BlockMetaPair blockBytePair, @Nonnegative byte tier) {
            BioVatGlass.glasses.put(blockBytePair, tier);
        }

        public static HashMap<BlockMetaPair, Byte> getGlassMap() {
            return BioVatGlass.glasses;
        }
    }

    public static class MaterialSvPair {
        final Materials materials;
        final Integer sievert;

        public MaterialSvPair(Materials materials, Integer sievert) {
            this.materials = materials;
            this.sievert = sievert;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            BioVatLogicAdder.MaterialSvPair that = (BioVatLogicAdder.MaterialSvPair) o;
            return Objects.equals(this.getMaterials(), that.getMaterials()) &&
                    Objects.equals(this.getSievert(), that.getSievert());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getMaterials(), this.getSievert());
        }

        public Materials getMaterials() {
            return this.materials;
        }

        public Integer getSievert() {
            return this.sievert;
        }


    }

    public static class BlockMetaPair {
        final Block block;
        final Byte aByte;

        public BlockMetaPair(Block block, Byte aByte) {
            this.block = block;
            this.aByte = aByte;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            BioVatLogicAdder.BlockMetaPair that = (BioVatLogicAdder.BlockMetaPair) o;
            return Objects.equals(this.getBlock(), that.getBlock()) &&
                    Objects.equals(this.getaByte(), that.getaByte());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getBlock(), this.getaByte());
        }

        public Block getBlock() {
            return this.block;
        }

        public Byte getaByte() {
            return this.aByte;
        }
    }
}