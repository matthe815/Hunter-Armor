package dev.matthe815.hunterarmorupgrades.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemMixin {
    @Shadow @Nullable public abstract CompoundNBT getTag();

    @Shadow public abstract Item getItem();

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    public void getDisplayNameMixin (CallbackInfoReturnable<ITextComponent> cir)
    {
        if (this.getTag() == null || !this.getTag().contains("upgrade_level")) return;
        cir.setReturnValue(new StringTextComponent(String.format("%s +%s", this.getItem().getName().getString(), this.getTag().getInt("upgrade_level"))));
    }
}
