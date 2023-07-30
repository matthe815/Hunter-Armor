package dev.matthe815.hunterarmorupgrades.loot;

import com.google.gson.JsonObject;
import dev.matthe815.hunterarmorupgrades.Registration;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class SimpleChestModifier extends LootModifier {
    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    public SimpleChestModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        int random = new Random().nextInt(25);

        // Give about a 25% chance to generate an armor sphere and a 5% chance for a +.
        if (random > 2 && random < 7) {
            generatedLoot.add(new ItemStack(Registration.ARMOR_SPHERE));
        }
        if (random < 3) {
            generatedLoot.add(new ItemStack(Registration.ARMOR_SPHERE2));
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SimpleChestModifier> {

        @Override
        public SimpleChestModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            return new SimpleChestModifier(ailootcondition);
        }

        @Override
        public JsonObject write(SimpleChestModifier instance) {
            JsonObject res = this.makeConditions(instance.conditions);
            return res;
        }
    }
}
