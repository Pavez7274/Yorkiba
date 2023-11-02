package me.pavez.yorkiba;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

import me.pavez.yorkiba.commands.AcceptSync;
import me.pavez.yorkiba.commands.Carft;
import me.pavez.yorkiba.commands.Sync;
import me.pavez.yorkiba.events.PlayerJoin;
import me.pavez.yorkiba.events.PlayerQuit;

public class Main extends JavaPlugin {

    public void onEnable() {
        Main.instance = this;
        loadDatabase();

        System.out.println("\n===================================================");
        System.out.println("\u001B[34m\r\n" + //
                "= __     __        _    _ _           \r\n" + //
                "= \\ \\   / /       | |  (_) |          \r\n" + //
                "=  \\ \\_/ /__  _ __| | ___| |__   __ _ \r\n" + //
                "=   \\   / _ \\| '__| |/ / | '_ \\ / _` |\r\n" + //
                "=    | | (_) | |  |   <| | |_) | (_| |\r\n" + //
                "=    |_|\\___/|_|  |_|\\_\\_|_.__/ \\__,_|\r\n");
        System.out.println("===================================================\n\u001B[0m");

        getCommand("accept-sync").setExecutor(new AcceptSync(instance));
        getCommand("sync").setExecutor(new Sync(instance));
        getCommand("craft").setExecutor(new Carft());
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(instance), instance);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(instance), instance);
    }

    public JSONObject cache = new JSONObject();
    private static Main instance;
    private JSONObject database;

    public JSONObject getDatabase() {
        return database;
    }

    public static Main getInstance() {
        return instance;
    }

    private void loadDatabase() {
        File file = new File(getDataFolder(), "cross_progression.json");

        if (!file.exists()) {
            // Si el archivo no existe, crea un nuevo objeto JSON vacío
            database = new JSONObject();
            saveDatabase(file);
        } else {
            // Si el archivo existe, carga su contenido en un objeto JSON
            try {
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                database = new JSONObject(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveDatabase(File file) {
        try {
            // Guarda el objeto JSON en el archivo
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(database.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void syncPlayerData(World world, String executorUUID, String targetUUID) {
        // Obtener el archivo de datos del jugador objetivo
        File playerDataFile = new File(world.getWorldFolder(), "playerdata/" + targetUUID + ".dat");

        if (playerDataFile.exists()) {
            try {
                // Leer los datos del jugador objetivo
                FileInputStream input = new FileInputStream(playerDataFile);
                byte[] data = Files.readAllBytes(playerDataFile.toPath());
                input.close();

                // Obtener el archivo de datos del jugador executor
                File executorPlayerDataFile = new File(world.getWorldFolder(), "playerdata/" + executorUUID + ".dat");

                // Escribir los datos del jugador objetivo en el archivo del jugador executor
                FileOutputStream output = new FileOutputStream(executorPlayerDataFile);
                output.write(data);
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}