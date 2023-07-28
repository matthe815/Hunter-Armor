package dev.matthe815.hunterarmorupgrades.containers;

import dev.matthe815.hunterarmorupgrades.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;

public class ContainerArmorCrafter extends Container {
    public PlayerInventory inventory;
    public Inventory crafterInventory = new Inventory(1);

    public ContainerArmorCrafter(final int windowId, final PlayerInventory playerInventory) {
        super(Registration.ARMOR_CRAFTER.get(), windowId);

        this.inventory = playerInventory;
        this.crafterInventory.addItem(new ItemStack(Items.ANDESITE));

        this.addSlot(new Slot(this.crafterInventory, 0, 80, 35));

        addPlayerInventory(9, 84);
    }

    protected void addPlayerInventory(int xInventory, int yInventory) {
        int id = 9;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlot(new Slot(inventory, id, xInventory + x * 18, yInventory + y * 18));
                id++;
            }
        }

        id = 0;

        for (int i = 0; i < 9; i++) {
            int x = xInventory + i * 18;
            int y = yInventory + 4 + (3 * 18);

            addSlot(new Slot(inventory, id, x, y));

            id++;
        }
    }

    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        super.putStackInSlot(slotID, stack);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
