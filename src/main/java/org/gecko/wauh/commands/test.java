package org.gecko.wauh.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gecko.wauh.gui.ConfigGUI;

public class test implements CommandExecutor {
    ConfigGUI configGUI;

    public test(ConfigGUI configGUI) {
        this.configGUI = configGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        configGUI.openGUI(player);

        return true;
    }
}