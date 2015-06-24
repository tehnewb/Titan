package inventory_action;

import java.util.Map;

import org.maxgamer.rs.model.entity.mob.InventoryHolder;
import org.maxgamer.rs.model.entity.mob.Mob;
import org.maxgamer.rs.model.item.ItemStack;
import org.maxgamer.rs.model.item.ground.GroundItemStack;
import org.maxgamer.rs.script.ActionHandler;

public class Drop extends ActionHandler{

	public void run(Mob m, Map<String, Object> args){
		ItemStack item = (ItemStack) args.get("item");
		int slot = (int) args.get("slot");
		
		((InventoryHolder) m).getInventory().remove(slot, item);
		GroundItemStack ground = new GroundItemStack(item, m, 30, 180);
		ground.setLocation(m.getLocation());
		yield();
	}
}