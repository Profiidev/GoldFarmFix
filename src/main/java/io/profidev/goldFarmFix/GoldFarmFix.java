package io.profidev.goldFarmFix;

import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Reverts the MC-56653 fix: an angered zombified piglin that dies from any damage
 * counts as killed by the player it is angry at, so it drops XP and player-kill loot again.
 */
public final class GoldFarmFix extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    // Damage fires before death, so XP and loot are computed with the killer set.
    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof PigZombie piglin
                && piglin.isAngry()
                && piglin.getTarget() instanceof Player player) {
            piglin.setKiller(player);
        }
    }
}
