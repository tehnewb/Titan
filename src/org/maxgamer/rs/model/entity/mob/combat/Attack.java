package org.maxgamer.rs.model.entity.mob.combat;

import java.util.Iterator;
import java.util.LinkedList;

import org.maxgamer.rs.events.mob.MobAttackEvent;
import org.maxgamer.rs.lib.log.Log;
import org.maxgamer.rs.model.entity.mob.Animation;
import org.maxgamer.rs.model.entity.mob.Graphics;
import org.maxgamer.rs.model.entity.mob.Mob;
import org.maxgamer.rs.model.entity.mob.persona.Persona;
import org.maxgamer.rs.model.skill.SkillType;

/**
 * @author netherfoam
 */
public abstract class Attack {
	/** The mob who is performing this attack */
	final protected Mob attacker;
	/** The emote to perform when activating this attack */
	protected Animation emote;
	/** The graphics (Eg, spell graphics) to perform when activating this attack */
	protected Graphics graphics;
	protected AttackStyle style;
	
	public Attack(Mob attacker, int emote, int graphics) {
		this(attacker, new Animation(emote), new Graphics(graphics));
	}
	
	public Attack(Mob attacker, Animation emote, Graphics graphics) {
		if (attacker == null) {
			throw new NullPointerException("Attacker may not be null");
		}
		this.attacker = attacker;
		this.emote = emote;
		this.graphics = graphics;
		this.style = attacker.getAttackStyle();
	}
	
	/**
	 * The attack style used to generate this attack.
	 * @return The attack style used to generate this attack.
	 */
	public AttackStyle getStyle() {
		return style;
	}
	
	/**
	 * Called once per attack
	 * @return true if successful, false if not
	 */
	public boolean run(Mob target) {
		AttackResult damage = new AttackResult();
		
		try {
			if (this.prepare(target, damage) == false) {
				return false; //Can't prepare, no attack.
			}
			
			if (this.takeConsumables() == false) {
				return false; //No consumables, no attack.
			}
		}
		catch (Exception e) {
			Log.warning("Exception processing Attack.prepare() and takeConsumables() for " + this + ", attacker " + attacker + ", target " + target);
			e.printStackTrace();
			return false;
		}
		
		//Throw mob attack event here
		Iterator<Damage> adit = new LinkedList<Damage>(damage.getDamages()).iterator();
		while (adit.hasNext()) {
			Damage d = adit.next();
			MobAttackEvent act = new MobAttackEvent(attacker, this, d, d.getTarget());
			act.call();
			if (act.isCancelled()) {
				//adit.remove();
				damage.getDamages().remove(d);
			}
			else if(d != act.getDamage()){
				//Our damage was changed during the event3
				damage.remove(d);
				damage.add(act.getDamage());
			}
		}
		
		if (emote != null && emote.getId() >= 0) {
			attacker.getUpdateMask().setAnimation(emote, 20);
		}
		
		if (graphics != null && graphics.getId() >= 0) {
			attacker.getUpdateMask().setGraphics(graphics);
		}
		
		this.perform(target, damage);
		
		return true;
	}
	
	/**
	 * Performs this attack, once.
	 * @param target The mob who should be attacked
	 * @param data The damage that should be dealt
	 */
	public void perform(Mob target, AttackResult data) {
		data.apply(attacker);
		
		if (attacker instanceof Persona) {
			Persona p = (Persona) attacker;
			//TODO: These experience formulas won't be correct, perhaps we should add
			//these formulas to the config as well.
			for (Damage d : data) {
				double exp = d.getHit();
				
				p.getSkills().addExp(SkillType.CONSTITUTION, exp / 3);
				
				AttackStyle style = this.getStyle();
				SkillType[] skills = style.getSkills();
				if (skills.length == 0) return;
				
				exp = exp / skills.length;
				for (SkillType t : skills) {
					p.getSkills().addExp(t, exp);
				}
			}
		}
	}
	
	/**
	 * Prepares to perform this attack on the given target. If the attack can't
	 * be executed at the moment, this should return null. If the attack can
	 * never be executed, you should call attacker.stopAttack();.
	 * @param target The target to attack
	 * @param damage The damage which we should edit.
	 * @return True if the attack can be executed, false if it cannot
	 */
	public abstract boolean prepare(Mob target, AttackResult damage);
	
	/**
	 * Should take all the consumables from the player, such as runes, special
	 * attack bar, arrows and more.
	 * @return true if the attack can proceed, false if the consumables aren't
	 *         there and must be removed.
	 */
	public abstract boolean takeConsumables();
	
	/**
	 * The maximum distance this attack can be performed at. '1' representing
	 * next to, '0' representing on top of.
	 * @return the maximum range this attack can be performed from
	 */
	public abstract int getMaxDistance();
	
	/**
	 * The number of ticks this attack has for its warmup (Eg before it can be
	 * run)
	 * @return The number of ticks this attack has for its warmup.
	 */
	public abstract int getWarmupTicks();
}