package dev.matthe815.hunterarmorupgrades.gui.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.matthe815.hunterarmorupgrades.HunterArmorUpgrades;
import dev.matthe815.hunterarmorupgrades.containers.ContainerArmorCrafter;
import dev.matthe815.hunterarmorupgrades.network.PacketArmorUpgrade;
import dev.matthe815.hunterarmorupgrades.upgrades.Upgrade;
import dev.matthe815.hunterarmorupgrades.utils.ItemStackUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class ScreenArmorCrafter extends ContainerScreen<ContainerArmorCrafter> {

    private int edgeSpacingX;
    private int edgeSpacingY;

    private Button upgradeButton;

    private Button evolveButton1;
    private Button evolveButton2;

    public ScreenArmorCrafter(ContainerArmorCrafter container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
    }

    @Override
    public void init(@Nonnull Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);

        this.xSize = 202;

        this.edgeSpacingX = ((this.width - this.xSize)) / 2;
        this.edgeSpacingY = (this.height - this.ySize) / 2;

        int upgradeButtonX = edgeSpacingX + xSize - 75;
        int upgradeButtonY = edgeSpacingY + 56;

        // The upgrade button defaults to the blank - off state as if there were no items.
        this.upgradeButton = new Button(upgradeButtonX, upgradeButtonY, 70, 20, new TranslationTextComponent("button.hunterarmorupgrades.upgrade"), button -> HunterArmorUpgrades.CHANNEL.sendToServer(new PacketArmorUpgrade(0)));
        this.upgradeButton.active = false;
        this.addButton(this.upgradeButton);

        this.evolveButton1 = new Button(upgradeButtonX - 25, upgradeButtonY, 95, 20, new TranslationTextComponent("Evolve"), button -> HunterArmorUpgrades.CHANNEL.sendToServer(new PacketArmorUpgrade(0)));
        this.evolveButton1.active = false;
        this.evolveButton1.visible = false;
        this.addButton(this.evolveButton1);

        this.evolveButton2 = new Button(upgradeButtonX - 25, upgradeButtonY - 22, 95, 20, new TranslationTextComponent("Evolve 2"), button -> HunterArmorUpgrades.CHANNEL.sendToServer(new PacketArmorUpgrade(1)));
        this.evolveButton2.active = false;
        this.evolveButton2.visible = false;
        this.addButton(this.evolveButton2);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

        String upgradeTranslation = new TranslationTextComponent("button.hunterarmorupgrades.upgrade").getString();
        String upgradeText = this.container.getSlotItem().getTag() != null ? String.format("%s %s/%s", upgradeTranslation, this.container.getSlotItem().getTag().getInt("upgrade_level"), 7) : null;
        boolean isActive = this.container.hasUpgradableItem() && !this.container.isMaxLevel();

        this.makeProgressionButtions();

        // Only display the upgrade text if the slot has an upgradeable item.
        if (this.container.hasUpgradableItem() && !this.container.isMaxLevel()) {
            // Draw the name and material texts.
            int textColor1 = 0x000000;

            this.font.drawText(matrixStack, this.container.getSlotItem().getDisplayName(), this.edgeSpacingX + 8, this.edgeSpacingY + 16, textColor1);
            this.font.drawText(matrixStack, new TranslationTextComponent("text.hunterarmorupgrades.materials"), this.edgeSpacingX + 8, this.edgeSpacingY + 28, textColor1);

            // Get a list of crafting ingredients from the item stack
            assert minecraft != null;
            List<ItemStack> ingredients = ItemStackUtils.getIngredientList(this.container.getSlotItem(), minecraft.world, this.container.getSlotItem().getTag() != null ? this.container.getSlotItem().getTag().getInt("upgrade_level") : 0);

            for (int i = 0; i < ingredients.size(); i++) {
                ItemStack ingredient = ingredients.get(i);

                // Show as able if you have enough items
                if (isActive) isActive = this.container.hasIngredient(ingredient);
                int textColor = this.container.hasIngredient(ingredient) ? textColor1 : 0xFF0000;
                ITextComponent text = new StringTextComponent(String.format("%s x%s", ingredient.getDisplayName().getString(), ingredient.getCount()));

                this.font.drawText(matrixStack, text, this.edgeSpacingX + 8, this.edgeSpacingY + 39 + (10 * i), textColor);
            }
        }

        // Set the button's state
        this.upgradeButton.active = isActive;
        this.upgradeButton.setMessage(new StringTextComponent(upgradeText != null ? upgradeText : upgradeTranslation));

        this.evolveButton1.active = isActive;
        this.evolveButton2.active = isActive;
    }

    protected void makeProgressionButtions() {
        ItemStack item = this.container.getSlotItem();

        if (!this.container.hasUpgradableItem())  {
            this.upgradeButton.visible = true;
            this.evolveButton1.visible = false;
            this.evolveButton2.visible = false;
            return;
        }

        int level = this.container.getSlotItem().getTag() != null ? this.container.getSlotItem().getTag().getInt("upgrade_level") : 0;
        Upgrade route = ItemStackUtils.getUpgradeForLevel(item.getItem(), level);

        this.upgradeButton.visible = false;

        if (route != null && route.results != null)  {
            if (route.results != null && route.results.length > 1)  {
                this.evolveButton2.visible = true;
                this.evolveButton2.setMessage(route.results[1].getDisplayName());
            }

            this.evolveButton1.visible = true;
            this.evolveButton1.setMessage(route.results[0].getDisplayName());
        }
        else {
            this.upgradeButton.visible = true;
            this.evolveButton1.visible = false;
            this.evolveButton2.visible = false;
        }
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
