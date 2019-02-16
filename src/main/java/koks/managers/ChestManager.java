package koks.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import koks.utility.LoadItemStack;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import koks.SkyWars;
import koks.config.SWConfig;
import koks.objects.ChestItem;
import koks.utility.ItemBuilder;

public class ChestManager {

    private static final ChestManager chestManager = new ChestManager();

    public static final ChestManager getChestManager() {
        return chestManager;
    }

    private final List<ChestItem> baseItems;
    private final List<ChestItem> midItems;

    private final List<Integer> randomLoc;
    private final Random random;


    public ChestManager() {
        random = new Random();
        baseItems = new ArrayList<>();
        midItems = new ArrayList<>();
        randomLoc = new ArrayList<>();
        for (int i = 0; i < 27; ++i) randomLoc.add(i);
    }

    public void loadItems() {
        FileConfiguration config = SWConfig.items.getConfig();
        if (config.contains("items.base")) {
            for (String item : config.getStringList("items.base")) {
                try {
                    ChestItem chestItem;
                    String[] values = item.split(",");
                    if (values[0].split(":")[0].equalsIgnoreCase("special")) {
                        ItemStack itemStack = LoadItemStack.loadItem(config.getConfigurationSection("specialItems." + values[0].split(":")[1]));
                        int chance1 = Integer.parseInt(values[1].split(":")[1]);
                        chestItem = new ChestItem(itemStack, chance1);
                        if (values.length >= 3)
                            chestItem.setChance2(Integer.parseInt(values[2].split(":")[1]));
                    } else {
                        String material = values[0].split(":")[1];
                        int amount = Integer.parseInt(values[1].split(":")[1]);
                        int chance1 = Integer.parseInt(values[2].split(":")[1]);
                        chestItem = new ChestItem(new ItemBuilder.Builder(Material.valueOf(material.toUpperCase()), amount, 0).build().getItem(), chance1);
                        if (values.length >= 4)
                            chestItem.setChance2(Integer.parseInt(values[3].split(":")[1]));
                    }
                    if (chestItem != null) baseItems.add(chestItem);
                } catch (Exception e) {
                    SkyWars.getInstance().consoleSendMessage("&c" + item + " not loaded");
                    SkyWars.getInstance().getLogger().warning(item + " not loaded: " + e);
                }
            }
        }
        if (config.contains("items.mid")) {
            for (String item : config.getStringList("items.mid")) {
                try {
                    ChestItem chestItem;
                    String[] values = item.split(",");
                    if (values[0].split(":")[0].equalsIgnoreCase("special")) {
                        ItemStack itemStack = LoadItemStack.loadItem(config.getConfigurationSection("specialItems." + values[0].split(":")[1]));
                        int chance1 = Integer.parseInt(values[1].split(":")[1]);
                        chestItem = new ChestItem(itemStack, chance1);
                        if (values.length >= 3)
                            chestItem.setChance2(Integer.parseInt(values[2].split(":")[1]));
                    } else {
                        String material = values[0].split(":")[1];
                        int amount = Integer.parseInt(values[1].split(":")[1]);
                        int chance1 = Integer.parseInt(values[2].split(":")[1]);
                        chestItem = new ChestItem(new ItemBuilder.Builder(Material.valueOf(material.toUpperCase()), amount, 0).build().getItem(), chance1);
                        if (values.length >= 4)
                            chestItem.setChance2(Integer.parseInt(values[3].split(":")[1]));
                    }
                    if (chestItem != null) midItems.add(chestItem);
                } catch (Exception e) {
                    SkyWars.getInstance().consoleSendMessage("&c" + item + " not loaded");
                    SkyWars.getInstance().getLogger().warning(item + " not loaded: " + e);
                }
            }
        }
    }

    public void fillChests(Chest chest) {
        Inventory inventory = chest.getBlockInventory();
        inventory.clear();
        int added = 0;
        Collections.shuffle(randomLoc);
        for (ChestItem chestItem : midItems) {
            if (random.nextInt(100) + 1 <= chestItem.getChance()) {
                inventory.setItem(randomLoc.get(added), chestItem.getItem());
                if (added++ >= inventory.getSize()) {
                    break;
                }
            }
        }
    }

    public void fillTeamChest(List<Chest> chests) {
        int added = 0;
        chests.forEach(c -> c.getBlockInventory().clear());
        Collections.shuffle(randomLoc);
        for (ChestItem item : baseItems) {
            if (random.nextInt(100) + 1 <= item.getChance()) {
                Inventory inv = chests.get(random.nextInt(chests.size())).getBlockInventory();
                inv.setItem(randomLoc.get(added++), item.getItem());
            }
        }
    }
}

