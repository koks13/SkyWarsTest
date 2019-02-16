package koks.managers;

import koks.SkyWars;
import koks.config.SWConfig;
import koks.objects.SWKit;
import koks.objects.SWPlayer;
import koks.utility.ChatUtil;
import koks.utility.ItemBuilder;

import koks.utility.LoadItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionType;

import java.util.HashMap;
import java.util.Map;

public class KitManager {

    private static Map<String, SWKit> kits = new HashMap<>();

    public static Map<String, SWKit> getKits() {
        return kits;
    }

    private static SWKit defaultKit;

    public static SWKit getDefaultKit() {
        return defaultKit;
    }

    public static void loadKits() {
        FileConfiguration config = SWConfig.kits.getConfig();
        if (config.getConfigurationSection("kits") != null) {
            for (String kitID : config.getConfigurationSection("kits").getKeys(false)) {
                try {
                    ConfigurationSection section = config.getConfigurationSection("kits." + kitID);
                    SWKit.KitBuilder kitBuilder = new SWKit.KitBuilder(section.getString("DisplayName"));
                    if (section.contains("Description")) {
                        for (String line : section.getStringList("Description")) {
                            kitBuilder.addLineInDescription(line);
                        }
                    }
                    if (section.contains("Rarity"))
                        kitBuilder.setRarity(SWKit.KitRarity.valueOf(section.getString("Rarity")));
                    if (section.contains("Cost")) kitBuilder.setCost(section.getInt("Cost"));
                    if (section.contains("DisplayItemType")) {
                        ItemBuilder.Builder displayItem = new ItemBuilder.Builder(Material.valueOf(section.getString("DisplayItemType"))).hideAttributes();
                        if (section.contains("DisplayItemPotionData"))
                            displayItem.setPotionData(PotionType.valueOf(section.getString("DisplayItemPotionData")));
                        kitBuilder.setDisplayItem(displayItem.build().getItem());
                    }
                    if (section.contains("KitItems")) {
                        for (String item : section.getConfigurationSection("KitItems").getKeys(false)) {
                            kitBuilder.addItemInKit(LoadItemStack.loadItem(section.getConfigurationSection("KitItems." + item)));
                        }
                    }
                    SWKit kit = kitBuilder.build();
                    SkyWars.getInstance().consoleSendMessage("&e" + kitID + " &aloaded!");
                    if (kit.isRarity(SWKit.KitRarity.DEFAULT)) defaultKit = kit;
                    kits.put(kit.getKitName(), kit);
                } catch (NullPointerException e) {
                    SkyWars.getInstance().consoleSendMessage("&c" + kitID + " not loaded");
                    SkyWars.getInstance().getLogger().warning(kitID + " not loaded: " + e.toString());
                }
            }
        }
    }

    public static SWKit getKit(String name) {
        name = ChatUtil.clearColor(name);
        for (String kitName : kits.keySet()) {
            if (name.equalsIgnoreCase(ChatUtil.clearColor(kitName))) return kits.get(kitName);
        }
        return null;
    }

    public static Inventory getKitsShop(SWPlayer player) {
        Inventory inv = Bukkit.createInventory(null, 54, ChatUtil.format("&b&lМАГАЗИН КИТОВ"));
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i > 44 || i % 9 == 0 || i % 9 == 8) {
                inv.setItem(i, new ItemBuilder.Builder(Material.STAINED_GLASS_PANE, 1, 7).setDisplayName("&b").build().getItem());
            }
        }
        for (SWKit kit : kits.values()) {
            if (kit.isRarity(SWKit.KitRarity.DEFAULT)) continue;
            if (player.getStats().containsKit(kit)) {
                inv.setItem(inv.firstEmpty(), new ItemBuilder.Builder(kit.getDisplayItem().clone()).addLineInLore("&a&lКуплен").build().getItem());
            } else {
                inv.setItem(inv.firstEmpty(), new ItemBuilder.Builder(kit.getDisplayItem().clone()).addLineInLore("&a&lСтоимость &8>> &e" + kit.getCost()).build().getItem());
            }
        }
        inv.setItem(49, new ItemBuilder.Builder(Material.EMERALD).setDisplayName("&e&lКоинов: &6" + player.getStats().getCoins()).build().getItem());
        return inv;
    }
}
