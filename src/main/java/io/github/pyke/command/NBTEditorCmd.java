package io.github.pyke.command;

import io.github.pyke.NBTEditor;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.pyke.NBTEditor.SYSTEM_PREFIX;

public class NBTEditorCmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SYSTEM_PREFIX + "§c플레이어만 사용할 수 있는 명령어입니다.");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage(SYSTEM_PREFIX + "§c아이템을 손에 들고 사용해주세요.");
            return true;
        }

        ItemMeta meta = item.getItemMeta();

        if (args.length >= 2 && args[0].equalsIgnoreCase("name")) {
            String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            name = name.replace("&", "§");
            meta.displayName(Component.text(name));
            item.setItemMeta(meta);
            player.sendMessage(SYSTEM_PREFIX + "§f아이템의 정보가 변경되었습니다.");
            return true;
        }

        if (args.length >= 3 && args[0].equalsIgnoreCase("lore")) {
            if (args[1].equalsIgnoreCase("add")) {
                List<Component> lore = meta.lore();
                if (null == lore || lore.isEmpty()) { lore = new ArrayList<>(); }

                String content = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                content = content.replace("&", "§");
                lore.add(Component.text(content));
                meta.lore(lore);
                item.setItemMeta(meta);
                player.sendMessage(SYSTEM_PREFIX + "§f아이템의 정보가 변경되었습니다.");
                return true;
            }
            else if (args[1].equalsIgnoreCase("set") && args.length >= 4) {
                int line;
                try {
                    line = Integer.parseInt(args[2]) - 1;
                } catch (NumberFormatException e) {
                    player.sendMessage(SYSTEM_PREFIX + "§c줄 번호는 숫자여야 합니다.");
                    return true;
                }

                List<Component> lore = meta.lore();
                if (null == lore || lore.isEmpty()) { lore = new ArrayList<>(); }

                if (line < 0 || line >= lore.size()) {
                    player.sendMessage(SYSTEM_PREFIX + "§f해당 줄 번호는 현재 lore 범위를 벗어났습니다.");
                    return true;
                }

                String content = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
                content = content.replace("&", "§");
                lore.set(line, Component.text(content));
                meta.lore(lore);
                item.setItemMeta(meta);
                player.sendMessage(SYSTEM_PREFIX + "§f아이템의 정보가 변경되었습니다.");
                return true;
            }
        }

        player.sendMessage(SYSTEM_PREFIX + "§c잘못된 명령어입니다.");
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        return Collections.emptyList();
    }
}