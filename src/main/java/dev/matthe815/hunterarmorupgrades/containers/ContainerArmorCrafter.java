package dev.matthe815.hunterarmorupgrades.containers;

import dev.matthe815.hunterarmorupgrades.Registration;
import dev.matthe815.hunterarmorupgrades.utils.ItemStackUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ContainerArmorCrafter extends Container {
    public PlayerInventory inventory;
    public Inventory crafterInventory = new Inventory(1);

    public ContainerArmorCrafter(final int windowId, final PlayerInventory playerInventory) {
        super(Registration.ARMOR_CRAFTER.get(), windowId);

        this.inventory = playerInventory;
        this.addSlot(new Slot(this.crafterInventory, 0, 166, 8));

        addPlayerInventory(9 - 13, 84);
    }

    // Check if the item in the slot is an upgradeable item.
    public boolean hasUpgradableItem()
    {
        return (getSlotItem().getItem() instanceof ArmorItem);
    }

    public boolean isMaxLevel()
    {
        CompoundNBT nbt = crafterInventory.getStackInSlot(0).getTag();
        int level = nbt != null ? nbt.getInt("upgrade_level") : 0;
        return level >= ItemStackUtils.ingredientList.get(crafterInventory.getStackInSlot(0).getItem()).ingredientsForUpgrade.length;
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
        return (stackd != null && (stackd.getCount() >= ingredient.getCount()) || this.inventory.player.isCreative());
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
        Slot slot = this.getSlot(index);
        ItemStack item = slot.getStack().copy();

        if (index == 0) {
            if (!mergeItemStack(item, 1, 35, false)) {
                return ItemStack.EMPTY;
            }
            //playerIn.inventory.addItemStackToInventory(item);
        } else {
            if (crafterInventory.getStackInSlot(0) != ItemStack.EMPTY) {
                return ItemStack.EMPTY;
            }
            crafterInventory.setInventorySlotContents(0, item);
        }

        slot.putStack(ItemStack.EMPTY);
        slot.onSlotChanged();

        return ItemStack.EMPTY;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        // Return the item to you if the inventory is closed
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
