package com.stormister.rediscovered.blocks;

import com.stormister.rediscovered.Rediscovered;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPeony extends BlockBush {
	public static final String[] field_149859_a = new String[] { "peony" };
	public static final String[] field_149858_b = new String[] { "peony" };

	public static int func_149856_f(String p_149856_0_) {
		int i;

		for (i = 0; i < field_149858_b.length; ++i) {
			if (field_149858_b[i].equals(p_149856_0_)) {
				return i;
			}
		}

		for (i = 0; i < field_149859_a.length; ++i) {
			if (field_149859_a[i].equals(p_149856_0_)) {
				return i;
			}
		}

		return 0;
	}

	public static BlockFlower func_149857_e(String p_149857_0_) {
		String[] astring = field_149858_b;
		int i = astring.length;
		int j;
		String s1;

		for (j = 0; j < i; ++j) {
			s1 = astring[j];

			if (s1.equals(p_149857_0_)) {
				return Blocks.yellow_flower;
			}
		}

		astring = field_149859_a;
		i = astring.length;

		for (j = 0; j < i; ++j) {
			s1 = astring[j];

			if (s1.equals(p_149857_0_)) {
				return Blocks.red_flower;
			}
		}

		return null;
	}

	private final String name = "Peony";

	public BlockPeony(int par1) {
		super(Material.plants);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(Rediscovered.modid + "_" + name);
	}

	public String getName() {
		return name;
	}
}