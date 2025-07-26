package io.github.pyke;

import io.github.pyke.command.NBTEditorCmd;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class NBTEditor extends JavaPlugin {
    private static NBTEditor instance;
    public static final String SYSTEM_PREFIX = "§6[System] ";

    @Override
    public void onEnable() {
        instance = this;

        Objects.requireNonNull(getCommand("nbtedit")).setExecutor(new NBTEditorCmd());
    }

    static public NBTEditor getInstance() { return instance; }
}