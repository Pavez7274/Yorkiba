package me.pavez.yorkiba.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

import me.pavez.yorkiba.Main;
import java.io.File;

public class AcceptSync implements CommandExecutor {

    private final Main plugin;

    public AcceptSync(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player target = (Player) sender;
            String TargetUUID = target.getUniqueId().toString();
            String ExecutorUUID = (String) plugin.cache.get(TargetUUID);
            Player executor = Bukkit.getPlayer(ExecutorUUID);

            if (executor == null || !(executor instanceof Player)) {
                target.sendMessage("El jugador con el que intentas sincronizar esta offline.");
            }

            // Verificar si hay solicitud pendiente para este jugador en la database
            if (plugin.cache.has(TargetUUID) && executor != null) {
                // Si hay una solicitud pendiente, aceptarla y actualizar la database
                plugin.getDatabase().put(ExecutorUUID, TargetUUID);
                plugin.saveDatabase(new File(plugin.getDataFolder(), "cross_progression.json"));

                // Notificar al ejecutor y objetivo que la sincronización ha sido aceptada
                executor.sendMessage("Sincronización exitosa. Estás ahora sincronizado con " + target.getName() + ".");
                target.sendMessage("Sincronización exitosa. Estás ahora sincronizado con " + executor.getName() + ".");
            } else {
                sender.sendMessage(
                        "No tienes solicitudes de sincronización pendientes o no se ha podido realizar la sincrionización.");
            }
            return true;
        } else {
            sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
        }
        return false;
    }
}
