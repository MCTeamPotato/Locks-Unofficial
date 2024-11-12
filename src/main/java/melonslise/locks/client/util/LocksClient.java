package melonslise.locks.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import melonslise.locks.client.init.LocksRenderTypes;
import melonslise.locks.common.components.interfaces.ISelection;
import melonslise.locks.common.config.LocksServerConfig;
import melonslise.locks.common.init.LocksComponents;
import melonslise.locks.common.init.LocksItemTags;
import melonslise.locks.common.util.Lockable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.List;

public class LocksClient {

    public static Lockable tooltipLockable;

    public static void renderLocks(PoseStack mtx, MultiBufferSource.BufferSource buf, Frustum ch, float pt) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 o = LocksClientUtil.getCamera().getPosition();
        BlockPos.MutableBlockPos mut = new BlockPos.MutableBlockPos();

        double dMin = 0d;

        assert mc.level != null;
        for (Lockable lkb : LocksComponents.LOCKABLE_HANDLER.get(mc.level).getLoaded().values()) {
            Lockable.State state = lkb.getLockState(mc.level);
            if (state == null || !state.inRange(o) || !state.inView(ch))
                continue;

            double d = o.subtract(state.pos).lengthSqr();
            if (d <= 25d) {
                Vec3 look = o.add(mc.player.getViewVector(pt));
                double d1 = LocksClientUtil.distanceToLineSq(state.pos, o, look);
                if (d1 <= 4d && (dMin == 0d || d1 < dMin)) {
                    tooltipLockable = lkb;
                    dMin = d1;
                }
            }

            mtx.pushPose();
            // For some reason translating by negative player position and then the point coords causes jittering in very big z and x coords. Why? Thus we use 1 translation instead
            mtx.translate(state.pos.x - o.x, state.pos.y - o.y, state.pos.z - o.z);
            //mtx.mulPose(new Quaternionf().rotateY(-state.tr.dir.toYRot() - 90));
            // FIXME 3 FUCKING QUATS PER FRAME !!! WHAT THE FUUUUUUCK!!!!!!!!!!!
            if (state.tr.face == AttachFace.CEILING) {
                mtx.mulPose(new Quaternionf().rotateX(90f));
            } else if (state.tr.dir == Direction.WEST || state.tr.dir == Direction.EAST) {
                mtx.mulPose(new Quaternionf().rotateY(1.6f));
            }
            if (state.tr.face == AttachFace.FLOOR) {
                mtx.mulPose(new Quaternionf().rotateX(45f));
            }

            mtx.translate(0d, 0.1d, 0d);
            mtx.mulPose(new Quaternionf().rotateZ(Mth.sin(LocksClientUtil.cubicBezier1d(1f, 1f, LocksClientUtil.lerp(lkb.maxSwingTicks - lkb.oldSwingTicks, lkb.maxSwingTicks - lkb.swingTicks, pt) / lkb.maxSwingTicks) * lkb.maxSwingTicks / 5f * 3.14f) * 0.4f));
            mtx.translate(0d, -0.1d, 0d);
            mtx.scale(0.5f, 0.5f, 0.5f);
            int light = LevelRenderer.getLightColor(mc.level, mut.set(state.pos.x, state.pos.y, state.pos.z));
            mc.getItemRenderer().renderStatic(lkb.stack, ItemDisplayContext.FIXED, light, OverlayTexture.NO_OVERLAY, mtx, buf, mc.level, 0);
            mtx.popPose();
        }
        buf.endBatch();
    }

    public static boolean holdingPick(Player player) {
        for (net.minecraft.world.InteractionHand InteractionHand : InteractionHand.values())
            if (player.getItemInHand(InteractionHand).is(LocksItemTags.LOCK_PICKS))
                return true;
        return false;
    }


    public static void renderSelection(PoseStack mtx, MultiBufferSource buf) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 o = LocksClientUtil.getCamera().getPosition();
        assert mc.player != null;
        ISelection select = LocksComponents.SELECTION.get(mc.player);
        if (select == null)
            return;
        BlockPos pos = select.get();
        if (pos == null)
            return;
        BlockPos pos1 = mc.hitResult instanceof BlockHitResult ? ((BlockHitResult) mc.hitResult).getBlockPos() : pos;
        boolean allow = Math.abs(pos.getX() - pos1.getX()) * Math.abs(pos.getY() - pos1.getY()) * Math.abs(pos.getZ() - pos1.getZ()) <= LocksServerConfig.MAX_LOCKABLE_VOLUME.get() && LocksServerConfig.canLock(mc.level, pos1);
        // Same as above
        LevelRenderer.renderLineBox(mtx, buf.getBuffer(LocksRenderTypes.OVERLAY_LINES), Math.min(pos.getX(), pos1.getX()) - o.x, Math.min(pos.getY(), pos1.getY()) - o.y, Math.min(pos.getZ(), pos1.getZ()) - o.z, Math.max(pos.getX(), pos1.getX()) + 1d - o.x, Math.max(pos.getY(), pos1.getY()) + 1d - o.y, Math.max(pos.getZ(), pos1.getZ()) + 1d - o.z, allow ? 0f : 1f, allow ? 1f : 0f, 0f, 0.5f);
        RenderSystem.disableDepthTest();
    }

    // Taken from Screen and modified to draw and fancy line and square and removed color recalculation
    public static void renderHudTooltip(PoseStack mtx, List<? extends FormattedCharSequence> lines, Font font) {
        if (lines.isEmpty())
            return;
        int width = 0;
        for (FormattedCharSequence line : lines) {
            int j = font.width(line);
            if (j > width)
                width = j;
        }

        int x = 36;
        int y = -36;
        int height = 8;
        if (lines.size() > 1)
            height += 2 + (lines.size() - 1) * 10;

        mtx.pushPose();

        BufferBuilder buf = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        LocksClientUtil.square(buf, mtx, 0f, 0f, 4f, 0.05f, 0f, 0.3f, 0.8f);
        LocksClientUtil.line(buf, mtx, 1f, -1f, x / 3f + 0.6f, y / 2f, 2f, 0.05f, 0f, 0.3f, 0.8f);
        LocksClientUtil.line(buf, mtx, x / 3f, y / 2f, x - 3f, y / 2f, 2f, 0.05f, 0f, 0.3f, 0.8f);
        // line(buf, last, 1f, -1f, x - 3f, y / 2f, 2f, 0.05f, 0f, 0.3f, 0.8f);
        LocksClientUtil.vGradient(buf, mtx, x - 3, y - 4, x + width + 3, y - 3, 0.0627451f, 0f, 0.0627451f, 0.9411765f, 0.0627451f, 0f, 0.0627451f, 0.9411765f);
        LocksClientUtil.vGradient(buf, mtx, x - 3, y + height + 3, x + width + 3, y + height + 4, 0.0627451f, 0f, 0.0627451f, 0.9411765f, 0.0627451f, 0f, 0.0627451f, 0.9411765f);
        LocksClientUtil.vGradient(buf, mtx, x - 3, y - 3, x + width + 3, y + height + 3, 0.0627451f, 0f, 0.0627451f, 0.9411765f, 0.0627451f, 0f, 0.0627451f, 0.9411765f);
        LocksClientUtil.vGradient(buf, mtx, x - 4, y - 3, x - 3, y + height + 3, 0.0627451f, 0f, 0.0627451f, 0.9411765f, 0.0627451f, 0f, 0.0627451f, 0.9411765f);
        LocksClientUtil.vGradient(buf, mtx, x + width + 3, y - 3, x + width + 4, y + height + 3, 0.0627451f, 0f, 0.0627451f, 0.9411765f, 0.0627451f, 0f, 0.0627451f, 0.9411765f);
        LocksClientUtil.vGradient(buf, mtx, x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, 0.3137255f, 0f, 1f, 0.3137255f, 0.15686275f, 0f, 0.49803922f, 0.3137255f);
        LocksClientUtil.vGradient(buf, mtx, x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, 0.3137255f, 0f, 1f, 0.3137255f, 0.15686275f, 0f, 0.49803922f, 0.3137255f);
        LocksClientUtil.vGradient(buf, mtx, x - 3, y - 3, x + width + 3, y - 3 + 1, 0.3137255f, 0f, 1f, 0.3137255f, 0.3137255f, 0f, 1f, 0.3137255f);
        LocksClientUtil.vGradient(buf, mtx, x - 3, y + height + 2, x + width + 3, y + height + 3, 0.15686275f, 0f, 0.49803922f, 0.3137255f, 0.15686275f, 0f, 0.49803922f, 0.3137255f);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderTexture(0, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // RenderSystem.shadeModel(7425);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferUploader.draw(buf.buildOrThrow());
        // RenderSystem.shadeModel(7424);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableBlend();
        RenderSystem.setShaderTexture(0, 7424);
        MultiBufferSource.BufferSource buf1 = MultiBufferSource.immediate(new ByteBufferBuilder(0));

        Matrix4f last = mtx.last().pose();
        for (int a = 0; a < lines.size(); ++a) {
            FormattedCharSequence line = lines.get(a);
            if (line != null)
                font.drawInBatch(line, (float) x, (float) y, -1, true, last, buf1, Font.DisplayMode.NORMAL, 0, 15728880);
            if (a == 0)
                y += 2;
            y += 10;
        }

        buf1.endBatch();

        mtx.popPose();
    }
}
