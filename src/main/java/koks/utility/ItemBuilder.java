package koks.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import koks.SkyWars;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class ItemBuilder {

    private final ItemStack item;

    public ItemStack getItem() {
        return item;
    }

    public static class Builder {

        private ItemStack item;
        private Material material;
        private short id;
        private int amount;
        private String name;
        private List<String> lore;
        private Map<Enchantment, Integer> enchants;
        private boolean itemFlags;
        private boolean unbreakable;

        private PotionData potionData;
        private List<PotionEffect> effects;

        public Builder(ItemStack item){
            this.item = item;
        }

        public Builder(Material material, int amount, int id) {
            this.material = material;
            this.amount = amount;
            this.id = (short) id;
        }

        public Builder(Material material, int amount) {
            this.material = material;
            this.amount = amount;
        }

        public Builder(Material material) {
            this.material = material;
            this.amount = 1;
        }

        public Builder setDisplayName(String name) {
            this.name = name.replace('&', '§');
            return this;
        }

        public Builder setLore(String l) {
            l = l.replace('&', '§');
            lore = new ArrayList<>();
            String[] s = l.split("|");
            for (String string : s) lore.add(string);
            return this;
        }

        public Builder setLore(List<String> lore) {
            for (String s : lore) s = s.replace('&', '§');
            this.lore = lore;
            return this;
        }

        public Builder addLineInLore(String line){
            lore = item.getItemMeta().getLore();
            lore.add(line.replace('&', '§'));
            return this;
        }

        public Builder addEnchant(Enchantment enchant, int level) {
            if (enchants == null) enchants = new HashMap<Enchantment, Integer>();
            enchants.put(enchant, level);
            return this;
        }

        public Builder hideAttributes() {
            itemFlags = true;
            return this;
        }

        public Builder setUnbreakable() {
            itemFlags = true;
            return this;
        }

        public Builder setPotionData(PotionType type) {
            potionData = new PotionData(type);
            return this;
        }

        public Builder addCustomPotionEffect(PotionEffect type) {
            if(effects == null) effects = new ArrayList<>();
            effects.add(type);
            return this;
        }

        public ItemBuilder build() {
            if(item == null) item = new ItemStack(material, amount, id);
            ItemMeta meta = item.getItemMeta();

            if(name != null) meta.setDisplayName(name);
            if(lore != null) meta.setLore(lore);
            if (itemFlags) {
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                //meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }
            if (unbreakable) meta.setUnbreakable(true);

            if (enchants != null) {
                for (Enchantment enchant : enchants.keySet()) {
                    meta.addEnchant(enchant, enchants.get(enchant), true);
                }
            }
            item.setItemMeta(meta);
            try {
                if(potionData != null){
                    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
                    potionMeta.setBasePotionData(potionData);
                    item.setItemMeta(potionMeta);
                }
                if(effects != null){
                    PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
                    effects.forEach(potionEffect -> potionMeta.addCustomEffect(potionEffect, true));
                    item.setItemMeta(potionMeta);
                }
            }catch (ClassCastException e){
                SkyWars.getInstance().consoleSendMessage("&c" + e.toString());
            }
            //if(enchants != null) item.addEnchantments(enchants);
            return new ItemBuilder(this);
        }
    }

    private ItemBuilder(Builder builder) {
        item = builder.item;
    }
}
