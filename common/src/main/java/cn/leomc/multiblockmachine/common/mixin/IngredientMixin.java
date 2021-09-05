package cn.leomc.multiblockmachine.common.mixin;

import cn.leomc.multiblockmachine.common.api.IngredientExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Predicate;

@Mixin(Ingredient.class)
public abstract class IngredientMixin implements Predicate<ItemStack>, IngredientExtension {

    @Shadow
    private ItemStack[] itemStacks;

    @Shadow
    protected abstract void dissolve();

    @Override
    public ItemStack[] getItemsServer() {
        dissolve();
        return itemStacks;
    }
}
