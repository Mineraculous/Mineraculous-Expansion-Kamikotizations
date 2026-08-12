package dev.thomasglasser.mineraculouskamikotizations.core.customization.setting;

import dev.thomasglasser.mineraculous.api.client.gui.IconRenderer;
import dev.thomasglasser.mineraculous.api.core.customization.setting.HolderCustomizationSettingType;
import dev.thomasglasser.mineraculous.api.core.registries.MineraculousRegistries;
import dev.thomasglasser.mineraculous.api.world.miraculous.Miraculous;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

public class MiraculousCustomizationSettingType extends HolderCustomizationSettingType<Miraculous> {
    public MiraculousCustomizationSettingType() {
        super(MineraculousRegistries.MIRACULOUS, Miraculous.CODEC, Miraculous.STREAM_CODEC);
    }

    @Override
    public @Nullable IconRenderer<Holder<Miraculous>> getRenderer() {
        return (guiGraphics, miraculousHolder, x, y) -> {
            guiGraphics.renderFakeItem(Miraculous.createMiraculousStack(miraculousHolder), x, y);
            return true;
        };
    }
}
