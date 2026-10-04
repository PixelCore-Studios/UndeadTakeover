package com.nyfaria.undeadtakeover.datagen;

import com.google.common.collect.ImmutableMap;
import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.init.BiomeInit;
import com.nyfaria.undeadtakeover.init.BlockInit;
import com.nyfaria.undeadtakeover.init.DimensionInit;
import com.nyfaria.undeadtakeover.init.EntityInit;
import com.nyfaria.undeadtakeover.init.ItemInit;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ModLangProvider extends LanguageProvider {
    protected static final Map<String, String> REPLACE_LIST = ImmutableMap.of(
            "tnt", "TNT",
            "sus", ""
    );

    public ModLangProvider(PackOutput output) {
        super(output, Constants.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        ItemInit.ITEMS.getEntries().forEach(this::itemLang);
        EntityInit.ENTITIES.getEntries().forEach(this::entityLang);
        BlockInit.BLOCKS.getEntries().forEach(this::blockLang);
        biomeLang(BiomeInit.ROTTING_PLAINS);
        biomeLang(BiomeInit.BLOOD_BIRCH_FOREST);
        biomeLang(BiomeInit.BLOOD_VALLEY);
        addDimension(DimensionInit.THE_REVENANT, checkReplace(DimensionInit.THE_REVENANT.identifier().getPath()));
        add("itemGroup." + Constants.MODID + ".tab", Constants.MOD_NAME);
    }

    protected void itemLang(RegistryObject<Item, ?> entry) {
        if (!(entry.get() instanceof BlockItem)) {
            addItem(entry, checkReplace(entry));
        }
    }

    protected void blockLang(RegistryObject<Block, ?> entry) {
        addBlock(entry, checkReplace(entry));
    }

    protected void entityLang(RegistryObject<EntityType<?>, ? extends EntityType<?>> entry) {
        addEntityType(entry, checkReplace(entry));
    }

    protected void biomeLang(ResourceKey<Biome> biome) {
        addBiome(biome, checkReplace(biome.identifier().getPath()));
    }

    protected String checkReplace(RegistryObject<?, ?> registryObject) {
        return checkReplace(registryObject.getId().getPath());
    }

    protected String checkReplace(String path) {
        return Arrays.stream(path.split("_"))
                .map(this::replaceWord)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "))
                .trim();
    }

    protected String replaceWord(String word) {
        return REPLACE_LIST.containsKey(word) ? REPLACE_LIST.get(word) : StringUtils.capitalize(word);
    }
}
