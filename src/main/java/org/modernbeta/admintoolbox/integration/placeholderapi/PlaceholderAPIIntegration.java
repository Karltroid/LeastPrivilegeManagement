package org.modernbeta.admintoolbox.integration.placeholderapi;

import org.modernbeta.admintoolbox.AdminToolboxPlugin;
import org.modernbeta.admintoolbox.integration.placeholderapi.expansion.StreamerModePlaceholder;
import org.modernbeta.admintoolbox.integration.placeholderapi.expansion.StreamerModePlaceholderCache;

public class PlaceholderAPIIntegration {
	private final AdminToolboxPlugin plugin;

	private final StreamerModePlaceholder streamerModePlaceholder;

	public PlaceholderAPIIntegration(AdminToolboxPlugin plugin, StreamerModePlaceholderCache cache) {
		this.plugin = plugin;
		this.streamerModePlaceholder = new StreamerModePlaceholder(plugin, cache);
	}

	public boolean registerPlaceholders() {
		return this.streamerModePlaceholder.register();
	}
}
