package org.maxgamer.rs.model.item.inventory;

import org.maxgamer.rs.model.item.ItemStack;

/**
 * Represents a container at its current state.
 * @author netherfoam
 */
public class ContainerState extends Container {
	/** the container we're updating */
	private Container c;
	/**
	 * The items this state represents. It is created with the contents of the
	 * container, and modified as necessary
	 */
	private ItemStack[] items;
	/**
	 * Corresponds to the items array, true if an update has been made at the
	 * given slot, false otherwise
	 */
	private boolean[] updates;
	
	/**
	 * Creates a new container state for the given container.
	 * @param c the container
	 */
	public ContainerState(Container c) {
		super(c.getStackType());
		this.c = c;
		this.items = c.getItems();
		this.updates = new boolean[items.length];
	}
	
	/**
	 * The container that this state was created for.
	 * @return the internal container
	 */
	public Container getContainer() {
		return c;
	}
	
	/**
	 * Applies all changes made by this container state to the container, and
	 * clears the internal set of updates.
	 */
	public synchronized void apply() {
		for (int i = 0; i < updates.length; i++) {
			if (updates[i]) {
				c.set(i, items[i]);
				updates[i] = false;
			}
		}
	}
	
	@Override
	protected void setItem(int slot, ItemStack item) {
		items[slot] = item;
		updates[slot] = true;
	}
	
	@Override
	public ItemStack get(int slot) {
		return items[slot];
	}
	
	@Override
	public int getSize() {
		return c.getSize();
	}
}