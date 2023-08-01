package dev.matthe815.hunterarmorupgrades.containers;

import dev.matthe815.hunterarmorupgrades.Registration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class ContainerArmorCrafter extends Container {
    public PlayerInventory inventory;
    public Inventory crafterInventory = new Inventory(1);

    public ContainerArmorCrafter(final int windowId, final PlayerInventory playerInventory) {
        super(Registration.ARMOR_CRAFTER.get(), windowId);

        this.inventory = playerInventory;
        this.addSlot(new Slot(this.crafterInventory, 0, 153, 8));

        addPlayerInventory(9, 84);
    }

    // Check if the item in the slot is an upgradeable item.
    public boolean hasUpgradableItem()
    {
        return (getSlotItem().getItem() instanceof ArmorItem);
    }

    public ItemStack getSlotItem()
    {
        return this.crafterInventory.getStackInSlot(0);
    }

    public boolean hasIngredient(ItemStack ingredient)
    {
        ItemStack stackd = null;
        for (ItemStack stack : this.inventory.mainInventory) {
            if (stack.getItem().equals(ingredient.getItem())) stackd = stack;
        }
        return stackd != null && (stackd.getCount() >= ingredient.getCount());
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
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        Slot sourceSlot = inventorySlots.get(index);
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        this.crafterInventory.setInventorySlotContents(0, copyOfSourceStack);

        return ItemStack.EMPTY;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        // Return the item to you if the inventory is closed
        if (!(this.crafterInventory.getStackInSlot(0).getItem() instanceof ArmorItem)) return;
        playerIn.inventory.addItemStackToInventory(this.crafterInventory.getStackInSlot(0));

        super.onContainerClosed(playerIn);
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
