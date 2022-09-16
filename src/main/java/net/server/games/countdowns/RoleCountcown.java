package net.server.games.countdowns;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.server.games.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RoleCountcown extends Countdown{

    private Main plugin;
    private int seconds = 30;

    public RoleCountcown(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 30:
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            player.sendTitle("§7Die §6Rollen §7werden in §6 " + seconds + " Sekunden", "§7bekannt gegeben.");
                        }
                        Bukkit.broadcastMessage(Main.Prefix + "§7Die §6Rollen §7werden in §6 " + seconds + " Sekunden §7bekannt gegeben.");
                        break;
                    case 15: case 10: case 5: case 3: case 2:
                        Bukkit.broadcastMessage(Main.Prefix + "§7Noch §6 " + seconds + " Sekunden §7bis zur Rollenbekanntgabe.");
                        break;
                    case 1:
                        Bukkit.broadcastMessage(Main.Prefix + "§7Noch §6eine Sekunde §7bis zur Rollenbekanntgabe");
                        break;
                    case 0:
                        stop();
                        Bukkit.broadcastMessage(Main.Prefix + "§aDie §6Rollen §awurden bekannt gegeben.");
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            player.sendTitle("§aDie §6Rollen §awurden bekannt gegeben.", "Viel Spaß.");
                        }
                        break;

                    default:
                        break;
                }
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(seconds == 30){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Die §6Rollen §7werden in §6 " +
                                seconds +
                                " Sekunden §7bekannt gegeben."));
                    }else if(seconds > 1 ){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Noch §6 " +
                                seconds +
                                " Sekunden §7bis zur Rollenbekanntgabe."));
                    }else if(seconds == 1){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Noch §6eine Sekunde §7bis zur Rollenbekanntgabe"));
                    }else if(seconds == 0){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aDie §6Rollen §awurden bekannt gegeben."));
                    }

                }

                seconds--;
            }
        }, 0, 20);
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

}
