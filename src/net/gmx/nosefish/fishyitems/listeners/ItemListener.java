package net.gmx.nosefish.fishyitems.listeners;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import net.gmx.nosefish.fishylib.properties.Properties;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.chat.Colors;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.BlockPlaceHook;
import net.canarymod.hook.player.ItemDropHook;
import net.canarymod.hook.player.ItemPickupHook;
import net.canarymod.hook.player.ItemUseHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.tasks.ServerTask;
import net.canarymod.tasks.ServerTaskManager;
import net.canarymod.tasks.TaskOwner;
import net.gmx.nosefish.fishyitems.FishyItems;
import net.gmx.nosefish.fishyitems.properties.Key;

/**
 * A <code>PluginListener</code> that handles item related permissions.
 * 
 * @author Stefan Steinheimer (nosefish)
 * 
 */
public class ItemListener implements PluginListener, TaskOwner {
	private Properties properties;
	private Set<Player> noSpam;
	private final long NOSPAMTIMEOUTMILLIS = 1000;
	
	/**
	 * Constructor
	 * 
	 * @param fishyShield
	 *            the plugin instantiating this <code>PluginListener</code>
	 */
	public ItemListener() {
		this.properties = FishyItems.properties;
		this.noSpam = Collections.newSetFromMap(new WeakHashMap<Player, Boolean>());
	}

	@HookHandler
	public void onItemUse(ItemUseHook hook) {
		Player player = hook.getPlayer();
		int itemInHand = hook.getItem().getId();
		if (itemUseForbidden(player, itemInHand)) {
			FishyItems.logger.info(player.getName() + " tried to use forbidden item " + itemInHand);
			hook.setCanceled();
		}
	}
	
	@HookHandler
    public void onBlockPlace(BlockPlaceHook hook) {
		Player player = hook.getPlayer();
		int itemInHand = hook.getBlockPlaced().getTypeId();
		if (itemUseForbidden(player, itemInHand)) {
			FishyItems.logger.info(player.getName() + " tried to use forbidden item " + itemInHand);
			hook.setCanceled();
		}
    }
    
    
	private boolean itemUseForbidden(Player player, int id) {
		World world = player.getWorld();
		if (!properties.getBoolean(world, Key.ITEM_ENABLE)) {
			return false;
		}
		boolean deny = (properties.containsInteger(world,
				Key.ITEM_USE_BLACKLIST, id))
				&& !properties.hasPermission(Key.ITEM_USE_PERM, player, id);
		if (deny) {
			messagePlayer(player,Colors.RED
					+ properties.getString(world, Key.ITEM_USE_MESSAGE));
		}
		return deny;
	}

	@HookHandler
	public void onItemDrop(ItemDropHook hook) {
		Player player = hook.getPlayer();
		World world = player.getWorld();
		if (!properties.getBoolean(world, Key.ITEM_ENABLE)) {
			return;
		}
		int id = hook.getItem().getItem().getId();
		FishyItems.logger.debug("onItemDrop " + id);
		boolean deny = (properties.containsInteger(world,
				Key.ITEM_DROP_BLACKLIST, id))
				&& !properties.hasPermission(Key.ITEM_DROP_PERM, player, id);
		if (deny) {
			FishyItems.logger.info(player.getName() + " tried to drop forpidden item " + id);
			messagePlayer(player,Colors.RED
					+ properties.getString(world, Key.ITEM_DROP_MESSAGE));
			hook.setCanceled();
		}
	}

	@HookHandler
	public void onItemPickUp(ItemPickupHook hook) {
		Player player = hook.getPlayer();
		World world = player.getWorld();
		if (!properties.getBoolean(world, Key.ITEM_ENABLE)) {
			return;
		}
		int id = hook.getItem().getItem().getId();
		FishyItems.logger.debug("onItemPickUp " + id);
		boolean deny = (properties.containsInteger(world,
				Key.ITEM_PICKUP_BLACKLIST, id))
				&& !properties.hasPermission(Key.ITEM_PICKUP_PERM, player, id);
		if (deny) {
			messagePlayer(player, Colors.RED
					+ properties.getString(world, Key.ITEM_PICKUP_MESSAGE));
			hook.setCanceled();
		}
	}
	
	/**
	 * Message a player, but don't spam them.
	 * @param player
	 * @param message
	 */
	private void messagePlayer(final Player player, String message) {
		if (! noSpam.contains(player)) {
			noSpam.add(player);
			player.message(message);
			// remove player from nospam list after timeout
			ServerTask expire = new ServerTask(this, NOSPAMTIMEOUTMILLIS) {
				@Override
				public void run() {
						noSpam.remove(player);
				}
			};
			ServerTaskManager.addTask(expire);
		}
	}
}
