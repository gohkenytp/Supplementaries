package net.mehvahdjukaar.supplementaries.datagen;

import net.mehvahdjukaar.supplementaries.datagen.types.IWoodType;
import net.mehvahdjukaar.supplementaries.datagen.types.WoodTypes;
import net.mehvahdjukaar.supplementaries.setup.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId, String locale) {
        super(gen, modId, locale);
    }



    @Override
    protected void addTranslations() {
        for(IWoodType wood : WoodTypes.TYPES.values()){
            add(Registry.SIGN_POST_ITEMS.get(wood).get(), format(wood.toString()+"_"+Registry.SIGN_POST_NAME));
            add(Registry.HANGING_SIGNS_ITEMS.get(wood).get(), format(wood.toString()+"_"+Registry.HANGING_SIGN_NAME));
        }
    }


    public static String format(String name){
        String[] words = name.split("_");

        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].substring(0,1).toUpperCase() + words[i].substring(1);
        }
        StringBuilder ret = new StringBuilder();
        for (String s : words){
            ret.append(s);
            if(!s.equals(words[words.length-1]))ret.append(" ");
        }
        return ret.toString();
    }
}
