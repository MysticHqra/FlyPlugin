package me.mystichqra.flyplugin.commands;

import me.mystichqra.flyplugin.FlyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;

public class FlyCommand implements CommandExecutor, Listener {

    Plugin plugin = FlyPlugin.getPlugin(FlyPlugin.class);
    String prefix = FlyPlugin.Colors(plugin.getConfig().getString("prefix")+"&r");
    static ArrayList<String> flyingplayers = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;
            if(args.length==0)
                if(player.hasPermission("flyplugin.fly"))
                    ToggleFly(player);
                else
                    player.sendMessage(prefix+FlyPlugin.Colors(plugin.getConfig().getString("player-no-permission-msg")));

            else if(args.length==1) {
                if(args[0].equalsIgnoreCase("reload")) {

                    if(player.hasPermission("flyplugin.reload")) {
                        plugin.reloadConfig();
                        player.sendMessage(prefix+FlyPlugin.Colors(plugin.getConfig().getString("reload-msg")));
                    }
                    else
                        player.sendMessage(prefix+FlyPlugin.Colors(plugin.getConfig().getString("player-no-permission-msg")));

                }
                else if(args[0].equalsIgnoreCase("help")) {

                    if(player.hasPermission("flyplugin.help")) {
                        player.sendMessage(FlyPlugin.Colors("&8----------&aFly Plugin&8----------"));
                        player.sendMessage(FlyPlugin.Colors("&8------------------------"));
                        if(player.hasPermission("flyplugin.fly")) player.sendMessage(FlyPlugin.Colors("&6/fly &8- &cToggles fly for player"));
                        if(player.hasPermission("flyplugin.fly.others")) player.sendMessage(FlyPlugin.Colors("&6/fly <player> &8- &cToggles fly for the given player"));
                    }
                    else
                        player.sendMessage(prefix+FlyPlugin.Colors(plugin.getConfig().getString("player-no-permission-msg")));

                }

                else if(player.hasPermission("flyplugin.fly.others")) {
                    Player target = Bukkit.getPlayerExact(args[0]);
                    if(target instanceof Player)
                        ToggleFly(target);
                    else
                        player.sendMessage(prefix+FlyPlugin.Colors(plugin.getConfig().getString("invalid-player-msg")));
                }
                else
                    player.sendMessage(prefix+FlyPlugin.Colors(plugin.getConfig().getString("player-no-permission-msg")));
            }

            else
                player.sendMessage(prefix+FlyPlugin.Colors(plugin.getConfig().getString("incorrect-format-msg")));
        }
        else
            System.out.println(prefix+FlyPlugin.Colors(plugin.getConfig().getString("console-error-message")));

        return true;
    }


    public void ToggleFly(Player player) {

        if(!flyingplayers.contains(player.getName())) {
            flyingplayers.add(player.getName());
            player.setAllowFlight(true);
            player.sendMessage(prefix+FlyPlugin.Colors(plugin.getConfig().getString("fly-on-message")));
        }
        else {
            flyingplayers.remove(player.getName());
            player.setAllowFlight(false);
            player.sendMessage(prefix+FlyPlugin.Colors(plugin.getConfig().getString("fly-off-message")));
        }

    }

    @EventHandler
    public void ChangeGamemode(PlayerCommandPreprocessEvent e) {

        Player player = e.getPlayer();
        String command = e.getMessage().substring(1);

        if (command.equalsIgnoreCase("gamemode creative"))
            flyingplayers.add(player.getName());

        if (command.equalsIgnoreCase("gamemode survival"))
            flyingplayers.remove(player.getName());


    }

    @EventHandler
    public void JoinFly(PlayerJoinEvent e) {

        boolean flag = plugin.getConfig().getBoolean("enable-join-fly");
        Player player = e.getPlayer();

        if(flag) {
            if (player.hasPermission("flyplugin.joinfly")) {
                Location loc = player.getLocation();
                loc.setY(loc.getY() - 2);
                Block block = loc.getBlock();

                if (block.getType() == Material.AIR) {
                    flyingplayers.remove(player.getName());
                    ToggleFly(player);
                    player.setFlying(true);
                    player.sendMessage(FlyPlugin.Colors(plugin.getConfig().getString("joinfly-message")));
                }
            }
        }



    }


}
