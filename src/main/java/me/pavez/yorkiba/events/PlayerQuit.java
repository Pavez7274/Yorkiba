package me.pavez.yorkiba.events;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;

import me.pavez.yorkiba.Main;

public class PlayerQuit implements Listener {

    private final Main plugin;

    public PlayerQuit(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.getDatabase().has(player.getUniqueId().toString())) {
            // Obtener el jugador objetivo
            String target = (String) plugin.getDatabase().get(player.getUniqueId().toString());

            if (target instanceof String) {
                // Sincronizar los datos del jugador con los del objetivo
                plugin.syncPlayerData(player.getWorld(), target, player.getUniqueId().toString());
                player.sendMessage("Tus datos han sido sincronizados.");
            } else {
                player.sendMessage("No se pudo encontrar al jugador objetivo para sincronizar los datos.");
            }
        }
    }
}
