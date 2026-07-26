package org.modernbeta.admintoolbox.integration.placeholderapi.expansion;

import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.modernbeta.admintoolbox.AdminToolboxPlugin;
import org.modernbeta.admintoolbox.integration.luckperms.LuckPermsIntegration;
import org.modernbeta.admintoolbox.managers.StreamerModeManager;

/**
 * Listens for LuckPerms {@link UserDataRecalculateEvent} and refreshes the
 * {@link StreamerModePlaceholderCache} with updated permission and streamer mode
 * active state.
 * <p>
 * When LuckPerms recalculates a user's data (permission/meta changes), this
 * listener schedules a cache refresh on the player's region thread to re-read
 * permissions and streamer mode metadata safely.
 */
public class StreamerModePlaceholderLuckPermsListener {

	private final StreamerModePlaceholderCache cache;
	private final AdminToolboxPlugin plugin;
	private final LuckPermsIntegration luckPerms;
	private final StreamerModeManager streamerModeManager;

	public StreamerModePlaceholderLuckPermsListener(
		StreamerModePlaceholderCache cache,
		AdminToolboxPlugin plugin,
		LuckPermsIntegration luckPerms,
		StreamerModeManager streamerModeManager
	) {
		this.cache = cache;
		this.plugin = plugin;
		this.luckPerms = luckPerms;
		this.streamerModeManager = streamerModeManager;
	}

	/**
	 * Subscribes to the LuckPerms event bus for {@link UserDataRecalculateEvent}.
	 * Should be called during plugin enable after LuckPerms is available.
	 */
	public void register() {
		luckPerms.api().getEventBus().subscribe(plugin, UserDataRecalculateEvent.class, this::onUserDataRecalculate);
	}

	private void onUserDataRecalculate(UserDataRecalculateEvent event) {
		Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
		if (player == null || !player.isOnline()) return;

		// Schedule permission and state re-read on the player's region thread (Folia-compatible)
		player.getScheduler().run(plugin, scheduledTask -> {
			cache.updatePermissions(player);
			boolean active = streamerModeManager.isActive(player);
			cache.updateStreamerModeActive(player.getUniqueId(), active);
		}, null);
	}
}
