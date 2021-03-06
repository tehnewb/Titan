package org.maxgamer.rs.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.maxgamer.rs.model.entity.mob.persona.player.Player;

/**
 * @author netherfoam
 */
public abstract class Pane extends Window {
	/**
	 * Key is interface ID
	 */
	private HashMap<Integer, Interface> interfaces = new HashMap<Integer, Interface>(16);
	
	/**
	 * Represents an interface for a single player.
	 * @param p The player
	 * @param childId The unique id for this interface, this is actually unique.
	 */
	public Pane(Player p, int childId) {
		super(p);
		setChildId(childId);
	}
	
	/**
	 * Adds the given child to this Pane. The child then has open() called on
	 * it.
	 * @param child the child to add and open
	 * @return true if the child replaced a previously existing child
	 */
	public void open(Interface iface) {
		if (this.isVisible() == false) {
			throw new IllegalStateException("The given pane is not visible.");
		}
		
		if (iface.getParent().isVisible() == false) {
			throw new IllegalArgumentException("The parent to that interface is not open.");
		}
		
		Integer key = Integer.valueOf(iface.getChildId());
		int pos = iface.childPos;
		
		for (Interface i : getInterfaces()) {
			if (i.parent == iface.parent && i.childPos == pos) {
				i.onClose();
				interfaces.remove(Integer.valueOf(i.getChildId()));
			}
		}
		
		//This interface is already open
		Interface old = interfaces.get(key);
		if (old != null) {
			if (old == iface) {
				throw new IllegalArgumentException("That interface is already open! " + iface);
			}
			old.onClose();
			//No need to send the close request to the client. It will be overridden
			interfaces.remove(key);
		}
		
		interfaces.put(key, iface);
		
		iface.onOpen();
		player.getProtocol().sendInterface(iface.isServerSidedClose(), iface.getParent().getChildId(), iface.childPos, iface.getChildId());
	}
	
	/**
	 * Removes the given child from this Pane. The child then has close() called
	 * on it. If the child was not found, it is not closed.
	 * @param child The child to remove
	 * @return true if the child was removed and closed, else false.
	 */
	public void close(Interface iface) {
		for (Interface i : getInterfaces()) {
			if (i.getParent() == iface) {
				close(i); //Recursive
			}
		}
		
		Integer key = Integer.valueOf(iface.getChildId());
		if (interfaces.get(key) != iface) {//TODO: this won't work with adding and deleting friends in the clan interface!
			throw new IllegalArgumentException("That interface is not opened! " + iface);
		}
		else {
			interfaces.remove(key);
		}
		
		player.getProtocol().sendCloseInterface(iface.getParent().getChildId(), iface.childPos);//childPos was childId
		iface.onClose();
	}
	
	/**
	 * Returns a copy of the currently open child interfaces
	 * @return a copy of the currently open child interfaces
	 */
	public Collection<Interface> getInterfaces() {
		return new ArrayList<Interface>(interfaces.values());
	}
	
	/**
	 * Returns true if the given interface is a direct child of this Pane.
	 * @param child the child interface
	 * @return true if it is a direct child, false if it is not
	 */
	public boolean isOpen(Interface iface) {
		Integer key = Integer.valueOf(iface.getChildId());
		if (interfaces.get(key) == iface) {
			return true;
		}
		return false;
	}
	
	/**
	 * Fetches the interface for the given ID
	 * @param id the interface id
	 * @return the interface or null if not found.
	 */
	public Interface getInterface(int id) {
		return interfaces.get(Integer.valueOf(id));
	}
	
	/**
	 * Fetches the owner of this Pane, as given in the constructor. Not null.
	 * @return the owner of this Pane, as given in the constructor. Not null.
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	public void onOpen() {
		
	}
	
	public void onClose() {
		
	}
	
	public boolean isVisible() {
		return getPlayer().getPanes().getActive() == this;
	}
}