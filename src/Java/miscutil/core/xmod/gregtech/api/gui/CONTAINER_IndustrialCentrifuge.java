package miscutil.core.xmod.gregtech.api.gui;

import gregtech.api.gui.GT_ContainerMetaTile_Machine;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * The Container I use for all my Basic Machines
 */
public class CONTAINER_IndustrialCentrifuge extends GT_ContainerMetaTile_Machine {
	
    public CONTAINER_IndustrialCentrifuge(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity) {
    	
        super(aInventoryPlayer, aTileEntity);
    }

    public CONTAINER_IndustrialCentrifuge(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, boolean bindInventory) {
        super(aInventoryPlayer, aTileEntity, bindInventory);
    }

    @Override
    public void addSlots(InventoryPlayer aInventoryPlayer) {
        addSlotToContainer(new Slot(mTileEntity, 1, 154, 42));
    }

    @Override
    public int getSlotCount() {
        return 1;
    }

    @Override
    public int getShiftClickSlotCount() {
        return 1;
    }
}
