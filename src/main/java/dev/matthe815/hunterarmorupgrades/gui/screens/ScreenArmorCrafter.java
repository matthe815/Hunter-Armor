package dev.matthe815.hunterarmorupgrades.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.matthe815.hunterarmorupgrades.HunterArmorUpgrades;
import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import dev.matthe815.hunterarmorupgrades.network.PacketArmorUpgrade;
import dev.matthe815.hunterarmorupgrades.utils.ItemStackUtils;
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

import javax.annotation.Nonnull;
import java.util.List;

public class ScreenArmorCrafter extends ContainerScreen<ContainerArmorCrafter> {

    private final int edgeSpacingX;
    private final int edgeSpacingY;

    private Button upgradeButton;

    public ScreenArmorCrafter(ContainerArmorCrafter container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);

        this.edgeSpacingX = (this.width - this.xSize) / 2;
        this.edgeSpacingY = (this.height - this.ySize) / 2;
    }

    @Override
    public void init(@Nonnull Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);

        int upgradeButtonX = edgeSpacingX + xSize - 75;
        int upgradeButtonY = edgeSpacingY + 56;

        // The upgrade button defaults to the blank - off state as if there were no items.
        this.upgradeButton = new Button(upgradeButtonX, upgradeButtonY, 70, 20, new TranslationTextComponent("button.hunterarmorupgrades.upgrade"), button -> HunterArmorUpgrades.CHANNEL.sendToServer(new PacketArmorUpgrade()));
        this.upgradeButton.active = false;
        this.addButton(this.upgradeButton);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        String upgradeTranslation = new TranslationTextComponent("button.hunterarmorupgrades.upgrade").getString();
        String upgradeText = this.container.getSlotItem().getTag() != null ? String.format("%s %s/%s", upgradeTranslation, this.container.getSlotItem().getTag().getInt("upgrade_level"), 7) : null;
        boolean isActive = this.container.hasUpgradableItem();

        // Only display the upgrade text if the slot has an upgradeable item.
        if (this.container.hasUpgradableItem()) {
            // Draw the name and material texts.
            int textColor1 = 0x000000;

            this.font.drawText(matrixStack, this.container.getSlotItem().getDisplayName(), this.edgeSpacingX + 8, this.edgeSpacingY + 16, textColor1);
            this.font.drawText(matrixStack, new TranslationTextComponent("text.hunterarmorupgrades.materials"), this.edgeSpacingX + 8, this.edgeSpacingY + 28, textColor1);

            // Get a list of crafting ingredients from the item stack
            assert minecraft != null;
            List<ItemStack> ingredients = ItemStackUtils.makeMergedList(this.container.getSlotItem(), minecraft.world);

            for (int i = 0; i < ingredients.size(); i++) {
                ItemStack ingredient = ingredients.get(i);

                // Show as able if you have enough items
                if (isActive) isActive = this.container.hasIngredient(ingredient);
                int textColor = this.container.hasIngredient(ingredient) ? textColor1 : 0xFF0000;
                ITextComponent text = new StringTextComponent(String.format("%s x%s", ingredient.getDisplayName().getString(), ingredient.getCount()));

                this.font.drawText(matrixStack, text, this.edgeSpacingX + 8, this.edgeSpacingY + 36 + (10 * i), textColor);
            }
        }

        // Set the button's state
        this.upgradeButton.active = isActive;
        this.upgradeButton.setMessage(new StringTextComponent(upgradeText != null ? upgradeText : upgradeTranslation));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(@Nonnull MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        assert this.minecraft != null;
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        int edgeSpacingX = (this.width - this.xSize) / 2;
        int edgeSpacingY = (this.height - this.ySize) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, this.xSize, this.ySize);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(HunterArmorUpgrades.MOD_ID, "textures/gui/armor_crafter.png");
}
