package dev.matthe815.hunterarmorupgrades.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.matthe815.hunterarmorupgrades.HunterArmorUpgrades;
import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import dev.matthe815.hunterarmorupgrades.network.PacketArmorUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ScreenArmorCrafter extends ContainerScreen<ContainerArmorCrafter> {

    public ScreenArmorCrafter(ContainerArmorCrafter container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);

    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        this.addButton(new Button(edgeSpacingX + 8, edgeSpacingY + 46, 60, 20, new StringTextComponent("Upgrade"), p_onPress_1_ -> {
            HunterArmorUpgrades.CHANNEL.sendToServer(new PacketArmorUpgrade());
            return;
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;

        String upgradeText = this.container.crafterInventory.getStackInSlot(0).getTag() != null ?
                String.format("Upgrade %s/%s", this.container.crafterInventory.getStackInSlot(0).getTag().getInt("upgrade_level"), 7)
                : "Upgrade";

        this.buttons.get(0).setMessage(new StringTextComponent(upgradeText));

        if (this.container.crafterInventory.getStackInSlot(0).getItem().equals(Items.AIR)) return;

        this.font.drawText(matrixStack, this.container.crafterInventory.getStackInSlot(0).getDisplayName(), edgeSpacingX + 8, edgeSpacingY + 16, 0x000000);
        this.font.drawText(matrixStack, new StringTextComponent("Materials:"), edgeSpacingX + 8, edgeSpacingY + 28, 0x000000);

        if (this.container.crafterInventory.getStackInSlot(0).getItem() instanceof ArmorItem)this.font.drawText(matrixStack, ((ArmorItem)this.container.crafterInventory.getStackInSlot(0).getItem()).getArmorMaterial().getRepairMaterial().getMatchingStacks()[0].getDisplayName(), edgeSpacingX + 8, edgeSpacingY + 36, 0x000000);
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
