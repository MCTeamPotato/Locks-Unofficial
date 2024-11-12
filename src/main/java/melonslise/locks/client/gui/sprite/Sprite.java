package melonslise.locks.client.gui.sprite;

import com.mojang.blaze3d.vertex.PoseStack;
import melonslise.locks.client.gui.sprite.action.IAction;
import melonslise.locks.client.util.LocksClientUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

import java.util.ArrayDeque;
import java.util.Queue;

// 处理游戏中的精灵图像的显示和动画
@Environment(EnvType.CLIENT)
public class Sprite
{
	private Queue<IAction> actions = new ArrayDeque<>(4);
	public TextureInfo tex;
	public float posX , posY, oldPosX, oldPosY, speedX, speedY, rot, oldRot, rotSpeed, originX, originY, alpha = 1f, oldAlpha = 1f;

	public Sprite(TextureInfo tex)
	{
		this.tex = tex;
	}

	public Sprite position(float posX, float posY)
	{
		this.posX = this.oldPosX = posX;
		this.posY = this.oldPosY = posY;
		return this;
	}

	public Sprite rotation(float rot, float originX, float originY)
	{
		this.rot = this.oldRot = rot;
		this.originX = originX;
		this.originY = originY;
		return this;
	}

	public Sprite alpha(float alpha)
	{
		this.alpha = this.oldAlpha = alpha;
		return this;
	}

	public void execute(IAction... actions)
	{
		for(IAction action : actions)
			this.actions.offer(action);
	}

	public boolean isExecuting()
	{
		return !this.actions.isEmpty();
	}

	// FIXME new quat obj every frame? JFC
	public void draw(GuiGraphics guiGraphics, float partialTick, ResourceLocation location)
	{
		if(this.alpha <= 0f)
			return;
		PoseStack mtx = guiGraphics.pose();
		mtx.pushPose();
		mtx.translate(this.originX, this.originY, 0f);
		mtx.mulPose(new Quaternionf().rotateZ(-LocksClientUtil.lerp(this.oldRot, this.rot, partialTick)));
		mtx.translate(-this.originX, -this.originY, 0f);
		this.tex.draw(guiGraphics, LocksClientUtil.lerp(this.oldPosX, this.posX, partialTick), LocksClientUtil.lerp(this.oldPosY, this.posY, partialTick), LocksClientUtil.lerp(this.oldAlpha, this.alpha, partialTick), location);
		mtx.popPose();
	}

	public void update()
	{
		this.oldPosX = this.posX;
		this.oldPosY = this.posY;
		this.oldRot = this.rot;
		this.oldAlpha = this.alpha;
		this.posX += this.speedX;
		this.posY += this.speedY;
		this.rot += this.rotSpeed;
		IAction action = this.actions.peek();
		if(action == null)
			return;
		if(action.isFinished(this))
			this.actions.poll().finish(this);
		else
			action.update(this);
	}
}