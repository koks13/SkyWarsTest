package koks.objects;

import koks.utility.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SWKit {

    private ItemStack displayItem;
    private String kitName;
    private List<ItemStack> itemsInKit;
    private int cost;
    private KitRarity rarity;

    public int getCost() {
        return cost;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public List<ItemStack> getItemsInKit() {
        return itemsInKit;
    }

    public String getKitName() {
        return kitName;
    }

    public boolean isRarity(KitRarity rarity) {
        return this.rarity == rarity;
    }

    public static class KitBuilder {
        private ItemStack displayItem;
        private String kitName;
        private List<String> description;
        private List<ItemStack> itemsInKit;
        private int cost;
        private KitRarity rarity;

        public KitBuilder(String kitName) {
            this.kitName = kitName.replace('&', '§');
        }

        public KitBuilder setDisplayItem(ItemStack item) {
            this.displayItem = item;
            return this;
        }

        public KitBuilder addLineInDescription(String line) {
            if (line == null) return this;
            if (description == null) description = new ArrayList<>();
            description.add(line.replace('&', '§'));
            return this;
        }

        public KitBuilder addItemInKit(ItemStack item) {
            if(item == null) return this;
            if (itemsInKit == null) itemsInKit = new ArrayList<>();
            itemsInKit.add(item);
            return this;
        }

        public KitBuilder setCost(int cost) {
            this.cost = cost;
            return this;
        }

        public KitBuilder setRarity(KitRarity rarity) {
            if (rarity == null) return this;
            this.rarity = rarity;
            return this;
        }

        public SWKit build() {
            if (rarity != null && rarity != KitRarity.DEFAULT)
                addLineInDescription("&b&lРедкость &8>> " + rarity.toString());
            return new SWKit(this);
        }
    }

    private SWKit(KitBuilder builder) {
        this.kitName = builder.kitName;
        this.cost = builder.cost;
        this.rarity = builder.rarity;
        this.displayItem = new ItemBuilder.Builder(builder.displayItem).setDisplayName(kitName).setLore(builder.description).hideAttributes().build().getItem();
        this.itemsInKit = builder.itemsInKit;
    }

    public enum KitRarity {
        DEFAULT, COMMON, RARE, EPIK, LEGENDARY;

        @Override
        public String toString() {
            switch (this) {
                case DEFAULT:
                    return "§a§lОБЫЧНЫЙ";
                case COMMON:
                    return "§a§lОБЫЧНЫЙ";
                case RARE:
                    return "§9§lРЕДКИЙ";
                case EPIK:
                    return "§5§lЭПИЧЕСКИЙ";
                case LEGENDARY:
                    return "§6§lЛЕГЕНДАРНЫЙ";
                default:
                    return this.name();
            }
        }
    }
}
