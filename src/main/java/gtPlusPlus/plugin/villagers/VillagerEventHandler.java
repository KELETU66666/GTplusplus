package gtPlusPlus.plugin.villagers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.core.util.Utils;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class VillagerEventHandler {

    private static final VillagerEventHandler mInstance;

    static {
        mInstance = new VillagerEventHandler();
        Utils.registerEvent(mInstance);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {

        /*try {
        	if (event.entity != null && event.entity instanceof EntityLivingBase && event.entity instanceof EntityVillager){
        		EntityVillager entity = (EntityVillager) event.entity;
        		World world = entity.worldObj;
        		int profession = entity.getProfession();
        		if (world != null && (profession >= 7735 && profession <= 7737)){
        			EntityBaseVillager mNew = new EntityBaseVillager(world, profession);
        			mNew.copyLocationAndAnglesFrom(entity);
        			if (mNew != null) {
        				world.removeEntity(entity);
        				world.spawnEntityInWorld(mNew);
        			}
        		}

        	}
        }
        catch (Throwable t) {
        	t.printStackTrace();
        	return;
        }*/

    }
}
