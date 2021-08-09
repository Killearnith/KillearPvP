/**package killearzone.killearpvp;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;

public class CommandKPvP implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("KPvP")) {
                if(args.length==0){
                    m.setTicks(3);
                    player.sendMessage(ChatColor.GREEN + "Ticks de invulnerabilidad puestos a "+m.getTicks()+"!");
                }else {
                    m.setTicks(Integer.parseInt(args[0]));
                    player.sendMessage(ChatColor.GREEN + "Ticks de invulnerabilidad a "+m.getTicks()+"!");
                    return true;
                }
            }

            player.sendMessage(ChatColor.GREEN + "No estaba previsto, consultar con el creador");
            return false;

    }

}
**/