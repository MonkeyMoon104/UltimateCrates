package com.monkey.ultimateCrates.crates.listener.list;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.listener.helper.manager.CratePreviewManager;
import com.monkey.ultimateCrates.crates.model.Crate;
import de.tr7zw.nbtapi.NBTBlock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class CratePreviewInteractListener implements Listener {

    private final CratePreviewManager previewManager;
    private final UltimateCrates plugin;

    public CratePreviewInteractListener(CratePreviewManager previewManager, UltimateCrates plugin) {
        this.previewManager = previewManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onCratePreview(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        NBTBlock nbtBlock = new NBTBlock(event.getClickedBlock());

        if (!nbtBlock.getData().hasTag("crate_id")) return;

        String crateId = nbtBlock.getData().getString("crate_id");
        Optional<Crate> crateOpt = plugin.getCrateManager().getCrate(crateId);
        if (!crateOpt.isPresent()) return;

        Crate crate = crateOpt.get();

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && !player.isSneaking()) {
            event.setCancelled(true);
            openPreviewGUI(player, crate);
        }
    }

    private void openPreviewGUI(Player player, Crate crate) {
        var prizes = crate.getPrizes();
        if (prizes.isEmpty()) {
            String msg = plugin.getMessagesManager().getMessage("messages.crate.no_prizes");
            player.sendMessage(msg);
            return;
        }

        int size = Math.max(27, ((prizes.size() - 1) / 9 + 1) * 9);
        size = Math.min(size, 54);
        String coloredCrateName = ChatColor.translateAlternateColorCodes('&', crate.getDisplayName());
        String title = plugin.getMessagesManager().getMessage("messages.crate.preview_title", coloredCrateName);
        title = ChatColor.translateAlternateColorCodes('&', title);

        Inventory inv = Bukkit.createInventory(null, size, title);

        String chanceFormat = plugin.getMessagesManager().getMessage("messages.crate.chance_format");

        for (int i = 0; i < prizes.size() && i < size; i++) {
            var prize = prizes.get(i);
            ItemStack item = prize.getItem().clone();
            double chance = prize.getChance();

            var meta = item.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.hasLore() ? meta.getLore() : new java.util.ArrayList<>();
                String formattedChance = chanceFormat.replace("%chance%", String.format("%.1f", chance));
                lore.add(formattedChance);
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            inv.setItem(i, item);
        }

        previewManager.addPreviewTitle(title);
        player.openInventory(inv);
    }
}
