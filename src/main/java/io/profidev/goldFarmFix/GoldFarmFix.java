package io.profidev.goldFarmFix;

import java.util.UUID;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Reverts the MC-56653 fix: an angered zombified piglin that dies from any damage
 * counts as killed by the last player it targeted, so it drops XP and player-kill loot again.
 */
public final class GoldFarmFix extends JavaPlugin implements Listener {

    private final NamespacedKey lastPlayerKey = new NamespacedKey(this, "last_player");

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    // Like pre-1.21.5 setTarget: remember the player, even after the target is lost.
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (event.getEntity() instanceof PigZombie piglin && event.getTarget() instanceof Player player) {
            piglin.getPersistentDataContainer().set(lastPlayerKey, PersistentDataType.STRING, player.getUniqueId().toString());
        }
    }

    // Damage fires before death, so XP and loot are computed with the killer set.
    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof PigZombie piglin) || !piglin.isAngry()) return;
        String uuid = piglin.getPersistentDataContainer().get(lastPlayerKey, PersistentDataType.STRING);
        Player player = uuid == null ? null : getServer().getPlayer(UUID.fromString(uuid));
        if (player != null) piglin.setKiller(player);
    }
}
