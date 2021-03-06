package org.maxgamer.rs.events.mob;

import org.maxgamer.rs.event.Cancellable;
import org.maxgamer.rs.model.entity.mob.Mob;
import org.maxgamer.rs.model.entity.mob.npc.NPC;

public class MobUseNPCEvent extends MobEvent implements Cancellable {
	private NPC target;
	private int option;
	private boolean cancel;
	
	public MobUseNPCEvent(Mob mob, NPC target, int option) {
		super(mob);
		
		this.target = target;
		this.option = option;
	}
	
	public NPC getTarget() {
		return this.target;
	}
	
	public int getOptionNumber(){
		return this.option;
	}
	
	public String getOption(){
		return target.getDefinition().getInteraction(this.option);
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
