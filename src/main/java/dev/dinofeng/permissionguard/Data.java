package dev.dinofeng.permissionguard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public enum Data {
    INSTANCE;

    public List<String> playerOpNotVerified = new ArrayList<>();
    public Map<String, String> sessionIP = new HashMap<>();

}
