package killearzone.killearpvp;

import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
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
    private final Map<UUID, Long> cooldowngapple = new HashMap<>();
    private long duracion;
    private long duraciongapple;
    private String mensaje;
    private String mensajegapple;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        duracion = getConfig().getInt("duracion") * 1000;
        duraciongapple = getConfig().getInt("duraciongapple") * 1000;
        mensaje = getConfig().getString("mensaje");
        mensajegapple = getConfig().getString("mensajegapple");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[KillearPvP]: esta activo!");

        // Plugin startup logic

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
    @EventHandler(ignoreCancelled = true)
    private void onComoManzanaDorada(PlayerItemConsumeEvent e){
        ItemStack gapple= e.getItem();

        if(gapple.getType().equals(Material.ENCHANTED_GOLDEN_APPLE)){
            Player p= e.getPlayer();
            if (p.hasPermission("kpvp.gapplecooldown.bypass")) {
                return;
            }
            Long cd= cooldowngapple.get(p.getUniqueId());
            if (cd != null && cd > System.currentTimeMillis()) {
                e.setCancelled(true);
                textoCooldownGapple(p, cd);
            } else {
                cooldowngapple.put(p.getUniqueId(), System.currentTimeMillis() + duraciongapple);
            }
        }

    }

    private void textoCooldown(Player player, long cooldown) {
            player.playSound(player.getLocation(), "entity.player.breath"	, SoundCategory.MASTER, 1, 1);

            if(!mensaje.isEmpty()){
                player.sendMessage(mensaje.replace("%tiempo%",String.valueOf(Math.floorDiv(cooldown - System.currentTimeMillis(), 1000)+ 1)));
            }
    }
    private void textoCooldownGapple(Player player, long cd) {
        player.playSound(player.getLocation(), "entity.player.breath"	, SoundCategory.MASTER, 1, 1);

        if(!mensajegapple.isEmpty()){
            player.sendMessage(mensajegapple.replace("%tiempo%",String.valueOf(Math.floorDiv(cd - System.currentTimeMillis(), 1000)+ 1)));
        }
    }
}
