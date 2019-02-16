package koks.objects;

import org.bukkit.inventory.ItemStack;

import koks.managers.GameManager;
import koks.objects.SWGame.GameStatus;

public class ChestItem {

	private ItemStack item;
	private int chance1;
	private int chance2;

	public ChestItem(ItemStack item, int chance1) {
		this.item = item;
		this.chance1 = chance1;
		this.chance2 = chance1;
	}

	public void setChance2(int chance2) {
		this.chance2 = chance2;
	}

	public ItemStack getItem() {
		return this.item;
	}

	public int getChance() {
		if(GameManager.getGame().isStatus(GameStatus.REFILL_CHESTS_2)) return chance2;
		return this.chance1;
	}
}
