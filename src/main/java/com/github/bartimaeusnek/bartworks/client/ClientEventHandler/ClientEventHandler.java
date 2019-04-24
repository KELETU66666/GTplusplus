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

package com.github.bartimaeusnek.bartworks.client.ClientEventHandler;

import com.github.bartimaeusnek.bartworks.API.BioVatLogicAdder;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_Blocks;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictHandler;
import com.github.bartimaeusnek.bartworks.util.BW_ColorUtil;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import com.github.bartimaeusnek.crossmod.BartWorksCrossmod;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    HashMap<ItemStack, List<String>> cache = new HashMap<>();

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void getTooltip(ItemTooltipEvent event) {
        if (event.itemStack == null || event.itemStack.getItem() == null || Block.getBlockFromItem(event.itemStack.getItem()) == null)
            return;

        if (!cache.containsKey(event.itemStack)) {
            List<String> tooAdd = new ArrayList<>();

            for (ItemStack stack : OreDictHandler.getCache().values()) {
                if (GT_Utility.areStacksEqual(event.itemStack, stack)) {
                    GameRegistry.UniqueIdentifier UI = GameRegistry.findUniqueIdentifierFor(stack.getItem());
                    if (UI == null)
                        UI = GameRegistry.findUniqueIdentifierFor(Block.getBlockFromItem(stack.getItem()));
                    if (UI != null) {
                        for (ModContainer modContainer : Loader.instance().getModList()) {
                            if (UI.modId.equals(MainMod.MOD_ID) || UI.modId.equals(BartWorksCrossmod.MOD_ID) || UI.modId.equals("BWCore"))
                               break;
                            if (UI.modId.equals(modContainer.getModId())) {
                                tooAdd.add("Shared ItemStack between " + ChatColorHelper.DARKGREEN + "BartWorks" + ChatColorHelper.GRAY + " and " + ChatColorHelper.RED + modContainer.getName());
                            }
                        }
                    } else
                        tooAdd.add("Shared ItemStack between " + ChatColorHelper.DARKGREEN + "BartWorks" + ChatColorHelper.GRAY + " and another Mod, that doesn't use the ModContainer propperly!");
                }
            }

                final Block BLOCK = Block.getBlockFromItem(event.itemStack.getItem());
                if (BLOCK instanceof BW_Blocks) {
                    cache.put(event.itemStack, tooAdd);
                    return;
                }
                final BioVatLogicAdder.BlockMetaPair PAIR = new BioVatLogicAdder.BlockMetaPair(BLOCK, (byte) event.itemStack.getItemDamage());
                final HashMap<BioVatLogicAdder.BlockMetaPair, Byte> GLASSMAP = BioVatLogicAdder.BioVatGlass.getGlassMap();
                if (GLASSMAP.containsKey(PAIR)) {
                    int tier = GLASSMAP.get(PAIR);
                    tooAdd.add(
                            StatCollector.translateToLocal("tooltip.glas.0.name") +
                                    " "
                                    + BW_ColorUtil.getColorForTier(tier) + GT_Values.VN[tier] + ChatColorHelper.RESET);
                } else if (BLOCK.getMaterial().equals(Material.glass)) {
                    tooAdd.add(StatCollector.translateToLocal("tooltip.glas.0.name") +
                            " "
                            + BW_ColorUtil.getColorForTier(3) + GT_Values.VN[3] + ChatColorHelper.RESET);
                }
                    cache.put(event.itemStack, tooAdd);
                    event.toolTip.addAll(tooAdd);
        } else {
            event.toolTip.addAll(cache.get(event.itemStack));
        }
    }
}