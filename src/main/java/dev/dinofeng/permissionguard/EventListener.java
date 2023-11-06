package dev.dinofeng.permissionguard;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import java.net.*;
import java.io.*;

public class EventListener implements Listener {
    public static PermissionGuard pl;
    public EventListener(PermissionGuard pl) {
        EventListener.pl = pl;
        loadData();
    }

    public static boolean isPremium(String name) {
        try {
            URL url = new URL("https://api.ashcon.app/mojang/v2/user/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            return ( connection.getResponseCode() == HttpURLConnection.HTTP_OK );
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public static void loadData() {
        if(pl.getData().get("Staff") != null) {
            pl.getData().getConfigurationSection("Staff").getValues(true).forEach((name, data) -> {
                Data.INSTANCE.sessionIP.put(name, (String) data);
            });
        }
    }
    public static void putInDataBase() {
        Data.INSTANCE.sessionIP.forEach((name, ip) -> {
            pl.getData().set("Staff." + name, ip);
        });
        pl.saveDataFile();
    }

    public static void autoVerify(Player p, String name) {
        if(isPremium(name)) {
            Data.INSTANCE.playerOpNotVerified.remove(p.getName());
            Data.INSTANCE.sessionIP.put(p.getName(), p.getAddress().toString().split(":")[0]);
            putInDataBase();
            System.out.println("Auto verified " + p.getName() + " as the account is premium.");
        }
    }

    @EventHandler
    public void onOPJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(p.isOp()) {
            System.out.println("An OP Player Joined. Player Name: " + p.getName() + " | Player IP: " + p.getAddress().toString().split(":")[0] + " | Trying to auto verify if the account is premium.");
            if(Data.INSTANCE.sessionIP.get(p.getName()) != null) {
                if(!Data.INSTANCE.sessionIP.get(p.getName()).equals(p.getAddress().toString().split(":")[0])) {
                    System.out.println("Warning, an OP Player IP just changed & modified! Player Name: " + p.getName() + " | Player IP: " + p.getAddress().toString().split(":")[0]);
                    Data.INSTANCE.playerOpNotVerified.add(p.getName());
                    Data.INSTANCE.sessionIP.remove(p.getName());
                    autoVerify(p, p.getName());
                }
            } else {
                Data.INSTANCE.playerOpNotVerified.add(p.getName());
                autoVerify(p, p.getName());
            }
        }
    }

    @EventHandler
    public void onOPLeft(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(p.isOp()) {
            if(Data.INSTANCE.playerOpNotVerified.contains(p.getName())) {
                System.out.println("An OP Player Left with no verify. Player Name: " + p.getName() + " | Player IP: " + p.getAddress().toString());
                Data.INSTANCE.playerOpNotVerified.remove(p.getName());
            }
        }
    }
    @EventHandler
    public void onPlayerCommandSend(PlayerCommandPreprocessEvent e) {
        if(Data.INSTANCE.playerOpNotVerified.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVerify your account from console first."));
            System.out.println("Blocked Command from unverify OP player.");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if(Data.INSTANCE.playerOpNotVerified.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent e) {
        if(Data.INSTANCE.playerOpNotVerified.contains(e.getPlayer().getName())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lVerify your account from console first."));
            System.out.println("Blocked Chat from unverify OP player.");
        }
    }

}
