package com.stormister.rediscovered.render;

import com.stormister.rediscovered.RediscoveredItemsManager;
import com.stormister.rediscovered.entity.EntityRediscoveredPotion;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRediscoveredPotion extends RenderSnowball {
	public RenderRediscoveredPotion(RenderManager p_i46136_1_, RenderItem p_i46136_2_) {
		super(p_i46136_1_, RediscoveredItemsManager.RediscoveredPotion, p_i46136_2_);
	}

	@Override
	public ItemStack func_177082_d(Entity p_177082_1_) {
		return func_177085_a((EntityRediscoveredPotion) p_177082_1_);
	}

	public ItemStack func_177085_a(EntityRediscoveredPotion p_177085_1_) {
		return new ItemStack(field_177084_a, 1, p_177085_1_.metadata);
	}
}