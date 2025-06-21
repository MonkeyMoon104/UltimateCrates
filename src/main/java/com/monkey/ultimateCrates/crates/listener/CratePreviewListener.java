package com.monkey.ultimateCrates.crates.listener;

import com.monkey.ultimateCrates.UltimateCrates;
import com.monkey.ultimateCrates.crates.model.Crate;
import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CratePreviewListener implements Listener {

    private final Set<String> previewInventoryTitles = new HashSet<>();

    @EventHandler
    public void onCratePreview(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() == null) return;

        Player player = event.getPlayer();
        NBTBlock nbtBlock = new NBTBlock(event.getClickedBlock());

        if (!nbtBlock.getData().hasTag("crate_id")) return;

        String crateId = nbtBlock.getData().getString("crate_id");
        Optional<Crate> crateOpt = UltimateCrates.getInstance().getCrateManager().getCrate(crateId);
        if (!crateOpt.isPresent()) return;

        Crate crate = crateOpt.get();

        if (event.getAction() == Action.LEFT_CLICK_BLOCK && !player.isSneaking()) {
            event.setCancelled(true);
            openPreviewGUI(player, crate);
            return;
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView() == null) return;
        if (event.getCurrentItem() == null) return;

        String title = event.getView().getTitle();
        if (title == null) return;

        if (previewInventoryTitles.contains(title)) {
            event.setCancelled(true);
        }
    }

    private void openPreviewGUI(Player player, Crate crate) {
        List<ItemStack> items = crate.getPrizes();
        if (items.isEmpty()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cQuesta crate non contiene premi."));
            return;
        }

        int size = Math.max(27, ((items.size() - 1) / 9 + 1) * 9);
        size = Math.min(size, 54);
        String title = ChatColor.translateAlternateColorCodes('&', crate.getDisplayName());

        Inventory inv = Bukkit.createInventory(null, size, title);

        for (int i = 0; i < items.size() && i < size; i++) {
            inv.setItem(i, items.get(i));
        }

        previewInventoryTitles.add(title);

        player.openInventory(inv);
    }
}
