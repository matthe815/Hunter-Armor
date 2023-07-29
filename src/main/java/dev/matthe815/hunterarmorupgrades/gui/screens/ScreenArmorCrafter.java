package dev.matthe815.hunterarmorupgrades.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.matthe815.hunterarmorupgrades.HunterArmorUpgrades;
import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import dev.matthe815.hunterarmorupgrades.network.PacketArmorUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ScreenArmorCrafter extends ContainerScreen<ContainerArmorCrafter> {

    public ScreenArmorCrafter(ContainerArmorCrafter container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);

    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        this.addButton(new Button(edgeSpacingX + xSize - 75, edgeSpacingY + 56, 70, 20, new StringTextComponent("Upgrade"), p_onPress_1_ -> {
            HunterArmorUpgrades.CHANNEL.sendToServer(new PacketArmorUpgrade());
            return;
        }));

        this.buttons.get(0).active = false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        boolean activeStatus;

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        String upgradeTranslation = new TranslationTextComponent("button.hunterarmorupgrades.upgrade").getString();
        String upgradeText = this.container.crafterInventory.getStackInSlot(0).getTag() != null ?
                String.format("%s %s/%s", upgradeTranslation, this.container.crafterInventory.getStackInSlot(0).getTag().getInt("upgrade_level"), 7)
                : upgradeTranslation;

        this.buttons.get(0).setMessage(new StringTextComponent(upgradeText));

        activeStatus = (this.container.crafterInventory.getStackInSlot(0).getItem() instanceof ArmorItem);

        if ((this.container.crafterInventory.getStackInSlot(0).getItem() instanceof ArmorItem)) {
            this.font.drawText(matrixStack, this.container.crafterInventory.getStackInSlot(0).getDisplayName(), edgeSpacingX + 8, edgeSpacingY + 16, 0x000000);
            this.font.drawText(matrixStack, new StringTextComponent("text.hunterarmorupgrades.materials"), edgeSpacingX + 8, edgeSpacingY + 28, 0x000000);

            ItemStack repairItem = ((ArmorItem)this.container.crafterInventory.getStackInSlot(0).getItem()).getArmorMaterial().getRepairMaterial().getMatchingStacks()[0];

            if (activeStatus == true) activeStatus = this.playerInventory.hasItemStack(repairItem);
            int textColor = this.playerInventory.hasItemStack(repairItem) ? 0x000000 : 0xFF0000;

            this.font.drawText(matrixStack, repairItem.getDisplayName(), edgeSpacingX + 8, edgeSpacingY + 36, textColor);
        };

        this.buttons.get(0).active = activeStatus;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation("hunterarmorupgrades", "textures/gui/armor_crafter.png");
}
