package org.modernbeta.admintoolbox.integration.placeholderapi.expansion;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

/**
 * Thread-safe cache for streamer mode placeholder resolution.
 * <p>
 * Stores per-player permission and streamer-mode-active state in a
 * {@link ConcurrentHashMap}, updated exclusively via events on the player's
 * owning region thread, and read atomically during placeholder resolution.
 * <p>
 * All public query methods return {@code false} on cache miss (fail-closed behavior):
 * if there is no entry for a player, the icon will not be shown.
 */
public class StreamerModePlaceholderCache {

	private static final String SM_VIEW_PERMISSION = "admintoolbox.streamermode.placeholder.view";
	private static final String SM_WEAR_PERMISSION = "admintoolbox.streamermode.placeholder.wear";

	/**
	 * Immutable snapshot of a player's cached state for placeholder resolution.
	 * Because records are immutable, a reference read from the ConcurrentHashMap
	 * is atomic — no partial reads are possible.
	 */
	public record PlayerCacheEntry(
		boolean hasViewPermission,
		boolean hasWearPermission,
		boolean isStreamerModeActive
	) {
	}

	private final ConcurrentHashMap<UUID, PlayerCacheEntry> cache =
		new ConcurrentHashMap<>();

	/**
	 * Returns the cached entry for the given player UUID, or empty if no entry
	 * exists (fail-closed).
	 *
	 * @param uuid the player's UUID
	 * @return an Optional containing the cache entry, or empty on cache miss
	 */
	public Optional<PlayerCacheEntry> get(UUID uuid) {
		return Optional.ofNullable(cache.get(uuid));
	}

	/**
	 * Returns whether the player has the view permission cached as {@code true}.
	 * Returns {@code false} on cache miss (fail-closed).
	 *
	 * @param uuid the player's UUID
	 * @return {@code true} only if the cache contains an entry with view
	 *         permission granted
	 */
	public boolean hasViewPermission(UUID uuid) {
		PlayerCacheEntry entry = cache.get(uuid);
		return entry != null && entry.hasViewPermission();
	}

	/**
	 * Returns whether the player has the wear permission cached as {@code true}.
	 * Returns {@code false} on cache miss (fail-closed).
	 *
	 * @param uuid the player's UUID
	 * @return {@code true} only if the cache contains an entry with wear
	 *         permission granted
	 */
	public boolean hasWearPermission(UUID uuid) {
		PlayerCacheEntry entry = cache.get(uuid);
		return entry != null && entry.hasWearPermission();
	}

	/**
	 * Returns whether streamer mode is active for the player.
	 * Returns {@code false} on cache miss (fail-closed).
	 *
	 * @param uuid the player's UUID
	 * @return {@code true} only if the cache contains an entry with streamer
	 *         mode active
	 */
	public boolean isStreamerModeActive(UUID uuid) {
		PlayerCacheEntry entry = cache.get(uuid);
		return entry != null && entry.isStreamerModeActive();
	}

	/**
	 * Updates the permission state for a player, preserving the current
	 * streamer mode active state. If no entry exists, creates one with
	 * {@code isStreamerModeActive = false}.
	 * <p>
	 * Must be called on the player's region thread.
	 *
	 * @param uuid              the player's UUID
	 * @param hasViewPermission whether the player has the view permission
	 * @param hasWearPermission whether the player has the wear permission
	 */
	public void updatePermissions(UUID uuid,
	                              boolean hasViewPermission,
	                              boolean hasWearPermission) {
		cache.compute(uuid, (key, existing) -> {
			boolean active = existing != null && existing.isStreamerModeActive();
			return new PlayerCacheEntry(hasViewPermission, hasWearPermission, active);
		});
	}

	/**
	 * Updates the permission state for a player by reading permissions directly
	 * from the {@link Player} object, preserving the current streamer mode active
	 * state. If no entry exists, creates one with {@code isStreamerModeActive = false}.
	 * <p>
	 * Must be called on the player's region thread.
	 *
	 * @param player the online player whose permissions should be read
	 */
	public void updatePermissions(Player player) {
		boolean hasView = player.hasPermission(SM_VIEW_PERMISSION);
		boolean hasWear = player.hasPermission(SM_WEAR_PERMISSION);
		updatePermissions(player.getUniqueId(), hasView, hasWear);
	}

	/**
	 * Updates the streamer mode active state for a player, preserving the
	 * current permission state. If no entry exists, creates one with both
	 * permissions set to {@code false} (fail-closed).
	 *
	 * @param uuid   the player's UUID
	 * @param active whether streamer mode is active
	 */
	public void updateStreamerModeActive(UUID uuid, boolean active) {
		cache.compute(uuid, (key, existing) -> {
			boolean viewPerm = existing != null && existing.hasViewPermission();
			boolean wearPerm = existing != null && existing.hasWearPermission();
			return new PlayerCacheEntry(viewPerm, wearPerm, active);
		});
	}

	/**
	 * Removes the cache entry for a player. Should be called on player quit.
	 *
	 * @param uuid the player's UUID
	 */
	public void invalidate(UUID uuid) {
		cache.remove(uuid);
	}
}
