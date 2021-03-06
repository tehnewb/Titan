package org.maxgamer.rs.events.mob;

import org.maxgamer.rs.event.Cancellable;
import org.maxgamer.rs.model.entity.mob.Mob;
import org.maxgamer.rs.model.item.ground.GroundItemStack;

public class MobUseGroundItemEvent extends MobEvent implements Cancellable {
	private GroundItemStack item;
	private int option;
	
	private boolean cancel;
	
	public MobUseGroundItemEvent(Mob mob, GroundItemStack item, int option) {
		super(mob);
		
		this.item = item;
		this.option = option;
	}
	
	public String getOption(){
		return this.getItem().getItem().getGroundOption(this.option);
	}
	
	public GroundItemStack getItem(){
		return this.item;
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}
	
	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
}
