package koks.utility;

import koks.SkyWars;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class LoadItemStack {

    public static ItemStack loadItem(ConfigurationSection section) {
        try {
            String type = section.getString("Type");
            if (type == null) return null;
            int amount = section.getInt("Amount");
            if (amount == 0) amount = 1;
            ItemBuilder.Builder itemBuilder = new ItemBuilder.Builder(Material.valueOf(type), amount);
            if (section.getString("DisplayName") != null) itemBuilder.setDisplayName(section.getString("DisplayName"));
            if (section.getString("Enchants") != null) {
                for (String enchant : section.getStringList("Enchants")) {
                    String[] values = enchant.split(",");
                    itemBuilder.addEnchant(Enchantment.getByName(values[0].split(":")[1]), Integer.parseInt(values[1].split(":")[1]));
                }
            }
            if (section.getString("PotionData") != null)
                itemBuilder.setPotionData(PotionType.valueOf(section.getString("PotionData")));
            if (section.getString("Effects") != null) {
                for (String effect : section.getStringList("Effects")) {
                    String[] values = effect.split(",");
                    itemBuilder.addCustomPotionEffect(new PotionEffect(PotionEffectType.getByName(values[0].split(":")[1]), 20 * Integer.parseInt(values[1].split(":")[1]), Integer.parseInt(values[2].split(":")[1]) - 1));
                }
            }
            if (section.getBoolean("HideAttributes")) itemBuilder.hideAttributes();

            return itemBuilder.build().getItem();
        } catch (NullPointerException e) {
            SkyWars.getInstance().consoleSendMessage(e.toString());
            SkyWars.getInstance().getLogger().warning(e.toString());
        }
        return null;
    }
}
