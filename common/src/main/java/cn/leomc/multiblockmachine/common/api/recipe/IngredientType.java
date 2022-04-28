package cn.leomc.multiblockmachine.common.api.recipe;

import cn.leomc.multiblockmachine.common.utils.PlatformSpecific;
import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;

import java.util.Locale;

public enum IngredientType {
    ITEM,
    FLUID,
    ENERGY,
    EMPTY;

    public boolean isIngredient() {
        return this == ITEM || this == FLUID;
    }

    public boolean isEmpty(){
        return this == EMPTY;
    }

    public static Component getDescription(RecipeResult result){
        return switch (result.getType()){
            case ITEM -> result.getType().getTranslation(result.getItem().getDisplayName(), asComponent((result.getItem().getCount())).withStyle(ChatFormatting.GRAY));
            case FLUID -> result.getType().getTranslation(result.getFluid().getName().copy().withStyle(), asComponent((result.getFluid().getAmount())).withStyle(ChatFormatting.GRAY));
            case ENERGY -> result.getType().getTranslation(asComponent(result.getEnergy()).withStyle(ChatFormatting.RED), new TextComponent(PlatformSpecific.getEnergy()).withStyle(ChatFormatting.RED)).withStyle(Style.EMPTY.withColor(0xef473a));
            case EMPTY -> result.getType().getTranslation().withStyle(ChatFormatting.GRAY);
        };
    }

    private static TextComponent asComponent(Object object){
        return new TextComponent(object.toString());
    }
    public TranslatableComponent getTranslation(Object... args){
        return new TranslatableComponent("text.multiblockmachine.ingredient." + name().toLowerCase(Locale.ENGLISH), args);
    }

}
