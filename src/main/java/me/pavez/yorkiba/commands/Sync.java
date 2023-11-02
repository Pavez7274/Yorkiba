package me.pavez.yorkiba.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import org.bukkit.Bukkit;

import me.pavez.yorkiba.Main;

public class Sync implements CommandExecutor {

    private final Main plugin;

    public Sync(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                Player executor = (Player) sender;
                Player target = Bukkit.getPlayer(args[0]);

                if (target != null) {
                    // Guarda en cache la ultima solicutud de sincronización
                    // { "TARGET_UUID": "EXECUTOR_UUID" }
                    plugin.cache.put(target.getUniqueId().toString(), executor.getUniqueId().toString());

                    // Envía un mensaje al objetivo con la solicitud de sincronización
                    target.sendMessage("¡Has recibido una solicitud de sincronización de " + executor.getName()
                            + "! Usa /accept-sync para aceptar.");
                    executor.sendMessage("Solicitud de sincronización enviada a " + target.getName() + ".");

                    return true;
                } else {
                    sender.sendMessage("El jugador objetivo no está en línea.");
                }
            } else {
                sender.sendMessage("Uso incorrecto. Usa /sync <jugador>");
            }
        } else {
            sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
        }
        return false;
    }
}
