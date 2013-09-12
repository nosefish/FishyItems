package net.gmx.nosefish.fishyitems;



import net.gmx.nosefish.fishylib.properties.Properties;

import net.canarymod.Canary;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;
import net.canarymod.tasks.TaskOwner;
import net.gmx.nosefish.fishyitems.listeners.ItemListener;
import net.gmx.nosefish.fishyitems.properties.Key;

/**
 * A CanaryRecode plugin that restricts item usage.
 * <p>
 * prevent players from using, dropping, or picking up certain items,
 * for example to prevent players from obtaining bedrock dropped by admins by
 * accident or on death, or to restrict item use to certain player groups.
 * Protections can be configured globally and per world.
 * 
 * @author Stefan Steinheimer (nosefish)
 * 
 */
public class FishyItems extends Plugin implements TaskOwner {
	public static Logman logger;
	public static Properties properties;


	@Override
	public void disable() {
	}

	@Override
	public boolean enable() {
		logger = getLogman();
		properties = new Properties(this);
		properties.addMissingKeys(Key.getAllKeys());
		registerListeners();
		return true;
	}

	
	public Properties getProperties() {
		return properties;
	}

	// ----------------------------------------------------------------------
	// private methods
	// ----------------------------------------------------------------------
	/**
	 * Registers all PluginListeners with the CanaryMod hook system
	 */
	private void registerListeners() {
		// items
		Canary.hooks().registerListener(new ItemListener(), this);
	}
}
