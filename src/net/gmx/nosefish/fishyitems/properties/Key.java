package net.gmx.nosefish.fishyitems.properties;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import net.gmx.nosefish.fishylib.properties.PropertyKey;
import net.gmx.nosefish.fishylib.properties.ValueType;

/**
 * Enum of known property keys, along with the string used in the properties
 * file, the type of their value, and the default value.
 * 
 * @author Stefan Steinheimer
 * 
 */
public enum Key implements PropertyKey{
	ITEM_ENABLE("item.protection-enable",
			ValueType.BOOLEAN, false),
	ITEM_USE_BLACKLIST("item.use-blacklist",
			ValueType.CSV_INT, new int[] {-1}),
	ITEM_USE_PERM("item.use-override-permissions",
			ValueType.CSV_PERMISSIONS, new String[]{"NOBODY"}),
	ITEM_USE_MESSAGE("item.use-message",
			ValueType.STRING, "You do not have permission to use this item."),
	ITEM_DROP_BLACKLIST("item.drop-blacklist",
			ValueType.CSV_INT, new int[] {-1}),
	ITEM_DROP_PERM("item.drop-override-permissions",
			ValueType.CSV_PERMISSIONS, new String[]{"NOBODY"}),
	ITEM_DROP_MESSAGE("item.drop-message",
			ValueType.STRING, "You do not have permission to drop this item."),
	ITEM_PICKUP_BLACKLIST("item.pickup-blacklist",
			ValueType.CSV_INT, new int[] {-1}),
	ITEM_PICKUP_PERM("item.pickup-override-permissions",
			ValueType.CSV_PERMISSIONS, new String[]{"NOBODY"}),
	ITEM_PICKUP_MESSAGE("item.pickup-message",
			ValueType.STRING, "You do not have permission to pick up this item");
	
	private static Map<String, PropertyKey> map;
	private String propertyName;
	private Object defaultValue;
	private ValueType propertyType;

	private Key(String propertyName, ValueType type, Object defaultValue) {
		this.propertyName = propertyName;
		this.propertyType = type;
		this.defaultValue = defaultValue;
		addToMap(propertyName, this);
	}

	/**
	 * Gets the name of the property key that is used in the properties file
	 * 
	 * @return property key as it appears in the file
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Gets the <code>ValueType</code> of this property. Used to determine how
	 * to load this property
	 * 
	 * @return the <code>ValueType</code> associated with this property
	 */
	public ValueType getType() {
		return propertyType;
	}

	/**
	 * Gets the default value of this property. This value will be applied if
	 * the property is missing from the properties file and will be written back
	 * to the file.
	 * 
	 * @return the default value
	 */
	public Object getDefault() {
		return defaultValue;
	}

	/**
	 * Gets all known keys. Used to find keys that are missing from the
	 * propertied file
	 * 
	 * @return the set of known keys
	 */
	public static Collection<PropertyKey> getAllKeys() {
		return map.values();
	}

	/**
	 * Gets the <code>Key</code> associated with a property string.
	 * 
	 * @param propertyName
	 *            the key string of the property as it appears in the properties
	 *            file
	 * @return the <code>Key</code> for the given property name
	 */
	public static PropertyKey getKey(String propertyName) {
		return map.get(propertyName);
	}

	/**
	 * Adds a property name/<code>Key</code> pair to the internal Map. Used by
	 * the constructor to make <code>getKey</code> possible.
	 * 
	 * @param propertyName
	 * @param k
	 */
	private static void addToMap(String propertyName, PropertyKey k) {
		if (map == null) {
			map = new TreeMap<String, PropertyKey>();
		}
		map.put(propertyName, k);
	}
}