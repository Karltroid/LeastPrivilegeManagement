package org.modernbeta.admintoolbox.integration.placeholderapi.expansion;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;
import org.modernbeta.admintoolbox.AdminToolboxPlugin;

/**
 * Event listener that keeps the {@link StreamerModePlaceholderCache} populated.
 * <p>
 * On player join, schedules cache population on the player's region thread
 * (Folia-compatible via {@code player.getScheduler().run()}).
 * On player quit, invalidates the cache entry immediately (thread-safe).
 */
public class StreamerModePlaceholderCacheListener implements Listener {

	private final StreamerModePlaceholderCache cache;
	private final AdminToolboxPlugin plugin;

	public StreamerModePlaceholderCacheListener(StreamerModePlaceholderCache cache, AdminToolboxPlugin plugin) {
		this.cache = cache;
		this.plugin = plugin;
	}

	/**
	 * On player join, schedule cache population on the player's region thread.
	 * Uses MONITOR priority to ensure permissions are fully loaded by other plugins
	 * before we read them.
	 * <p>
	 * Initial {@code isStreamerModeActive} is set to {@code false} — it will be
	 * updated by the LuckPerms {@code UserDataRecalculateEvent} listener if needed.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		// Schedule on the player's owning region thread (Folia-compatible)
		player.getScheduler().run(plugin, scheduledTask -> {
			cache.updatePermissions(player);
		}, null);
	}

	/**
	 * On player quit, invalidate the cache entry immediately.
	 * {@code cache.invalidate()} is thread-safe via ConcurrentHashMap.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		cache.invalidate(event.getPlayer().getUniqueId());
	}
}
