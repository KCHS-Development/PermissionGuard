package dev.dinofeng.permissionguard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VerifyCommand implements CommandExecutor {
    PermissionGuard pl;
    public VerifyCommand(PermissionGuard pl) {
        this.pl = pl;
        pl.getCommand("verify").setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            if(args.length == 1) {
                if(Data.INSTANCE.playerOpNotVerified.contains(pl.getServer().getPlayer(args[0]).getName())) {
                    Data.INSTANCE.playerOpNotVerified.remove(pl.getServer().getPlayer(args[0]).getName());
                    Data.INSTANCE.sessionIP.put(pl.getServer().getPlayer(args[0]).getName(), pl.getServer().getPlayer(args[0]).getAddress().toString().split(":")[0]);
                    System.out.println("Registered session for " + pl.getServer().getPlayer(args[0]).getName() + " | IP: " + pl.getServer().getPlayer(args[0]).getAddress().toString().split(":")[0]);
                    EventListener.putInDataBase();
                } else {
                    System.out.println("Invalid Players name / Already verified.");
                }
            } else {
                System.out.println("Less Args!");
            }
        } else {
            System.out.println("Refused Request from non-console.");
        }
        return true;
    }
}
