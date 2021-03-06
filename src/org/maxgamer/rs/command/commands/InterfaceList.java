package org.maxgamer.rs.command.commands;

import org.maxgamer.rs.command.PlayerCommand;
import org.maxgamer.rs.interfaces.Interface;
import org.maxgamer.rs.model.entity.mob.persona.player.Player;
import org.maxgamer.rs.model.entity.mob.persona.player.Rights;

/**
 * @author netherfoam
 */
public class InterfaceList implements PlayerCommand {
	@Override
	public void execute(Player p, String[] args) {
		p.sendMessage("-- Active Interfaces --");
		for (Interface i : p.getWindow().getInterfaces()) {
			p.sendMessage(i.getClass().getSimpleName() + " (" + i.getChildId() + ")");
		}
	}
	
	@Override
	public int getRankRequired() {
		return Rights.MOD;
	}
}