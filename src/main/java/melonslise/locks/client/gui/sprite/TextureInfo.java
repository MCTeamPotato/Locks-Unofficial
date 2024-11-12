package melonslise.locks.client.gui.sprite;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

//存储纹理
@Environment(EnvType.CLIENT)
public class TextureInfo
{
	public int startX, startY, width, height, canvasWidth, canvasHeight;
	public ResourceLocation resourceLocation;

	public TextureInfo(int startX, int startY, int width, int height, int canvasWidth, int canvasHeight, ResourceLocation resourceLocation)
	{
		this.startX = startX;
		this.startY = startY;
		this.width = width;
		this.height = height;
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		this.resourceLocation = resourceLocation;
	}

	public void draw(GuiGraphics mtx, float x, float y, float alpha, ResourceLocation location)
	{
		mtx.blit(location, (int) x, (int) y, this.startX, this.startY, this.width, this.height, this.canvasWidth, this.canvasHeight);
		// LocksClientUtil.texture(mtx, x, y, this.startX, this.startY, this.width, this.height, this.canvasWidth, this.canvasHeight, alpha);
	}
}