package com.nyfaria.undeadtakeover.datagen;

import com.nyfaria.undeadtakeover.Constants;
import com.nyfaria.undeadtakeover.registration.RegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {
    public ModSoundProvider(PackOutput output) {
        super(output, Constants.MODID);
    }

    @Override
    public void registerSounds() {
    }

    public void addSound(RegistryObject<SoundEvent, SoundEvent> entry) {
        add(entry.get(), SoundDefinition.definition().with(sound(entry.getId())));
    }
}
