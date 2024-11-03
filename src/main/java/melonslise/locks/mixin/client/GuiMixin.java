package melonslise.locks.mixin.client;


import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import melonslise.locks.client.util.LocksClient;
import melonslise.locks.client.util.LocksClientUtil;
import melonslise.locks.common.util.Lockable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {


    @Inject(method = "render",at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        // if(e.getType() != RenderGuiOverlayEvent.ElementType.ALL || tooltipLockable == null)
        if (LocksClient.tooltipLockable == null)
            return;
        if (LocksClient.holdingPick(mc.player)) {
            PoseStack mtx = guiGraphics.pose();
            Vector3f vec = LocksClientUtil.worldToScreen(LocksClient.tooltipLockable.getLockState(mc.level).pos, f);
            if (vec.z() < 0d) {
                mtx.pushPose();
                mtx.translate(vec.x(), vec.y(), 0f);
                LocksClient.renderHudTooltip(mtx, Lists.transform(LocksClient.tooltipLockable.stack.getTooltipLines(mc.player, mc.options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL), Component::getVisualOrderText), mc.font);
                mtx.popPose();
            }
        }
        LocksClient.tooltipLockable = null;


    }

}
