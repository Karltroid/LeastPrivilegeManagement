package org.modernbeta.admintoolbox.integration.placeholderapi.expansion;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.modernbeta.admintoolbox.AdminToolboxPlugin;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;

public class StreamerModePlaceholder extends PlaceholderExpansion implements Relational {
	private final AdminToolboxPlugin plugin;
	private final StreamerModePlaceholderCache cache;

	public StreamerModePlaceholder(AdminToolboxPlugin plugin, StreamerModePlaceholderCache cache) {
		this.plugin = plugin;
		this.cache = cache;
	}

	@Override
	public @NotNull String getIdentifier() {
		return "streamermode";
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public @NotNull String getAuthor() {
		return String.join(", ", plugin.getPluginMeta().getAuthors());
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public @NotNull String getVersion() {
		return plugin.getPluginMeta().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onPlaceholderRequest(Player viewer, Player wearer, String identifier) {
		if (viewer == null || wearer == null) return "";

		// hide placeholder from players currently in streamer mode
		if (!plugin.getConfig().getBoolean("streamermode.show-indicators-when-active", false)
			&& cache.isStreamerModeActive(viewer.getUniqueId())) return "";

		if (!cache.hasViewPermission(viewer.getUniqueId())) return "";
		if (!cache.hasWearPermission(wearer.getUniqueId())) return "";

		if (!cache.isStreamerModeActive(wearer.getUniqueId())) return "";

		String tag = ChatColor.RED + "⬤";
		return switch (identifier.toLowerCase()) {
			case "prefix" -> tag + " ";
			case "suffix" -> " " + tag;
			case "tag" -> tag;
			default -> null;
		};
	}
}
