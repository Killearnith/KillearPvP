package killearzone.killearpvp;

import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class KillearPvP extends JavaPlugin implements Listener{
    private final Map<UUID, Long> cooldowns = new HashMap<>();  //Hashmap para el cooldown de cada player
    private long duracion;
    private String mensaje;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        duracion = getConfig().getInt("duracion") * 1000;
        mensaje = getConfig().getString("mensaje");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[KillearPvP]: esta activo!");

        // Plugin startup logic
        //this.getCommand("KPvP").setExecutor(new CommandKPvP());   [NO FUNCIONA]
        //activación del comando

    }
    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[KillearPvP]: esta desactivado!");
        // Plugin shutdown logic
    }

    @EventHandler(ignoreCancelled = true)
    void onTiroEnderPearl(ProjectileLaunchEvent event) {

        if (!(event.getEntity() instanceof EnderPearl) || !(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity().getShooter();
        if (player.hasPermission("kpvp.enderpearlcooldown.bypass")) {
            return;
        }
        Long cooldown = cooldowns.get(player.getUniqueId());
        if (cooldown != null && cooldown > System.currentTimeMillis()) {
            event.setCancelled(true);
            textoCooldown(player, cooldown);
        } else {
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + duracion);
        }
    }


    private void textoCooldown(Player player, long cooldown) {
            player.playSound(player.getLocation(), "entity.player.breath"	, SoundCategory.MASTER, 1, 1);

            if(!mensaje.isEmpty()){
                player.sendMessage(mensaje.replace("%tiempo%",String.valueOf(Math.floorDiv(cooldown - System.currentTimeMillis(), 1000)+ 1)));
            }
    }
}
