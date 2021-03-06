package com.stormister.rediscovered.items;

import com.stormister.rediscovered.blocks.BlockCherryDoubleSlab;
import com.stormister.rediscovered.blocks.BlockCherryHalfSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockCherrySlab extends ItemSlab {
	/**
	 * Initializes a new instance of the ItemBlockStainedBrickSlab class.
	 *
	 * @param block
	 *            the block behind the item.
	 * @param slab
	 *            the half height slab.
	 * @param doubleSlab
	 *            the full height slab.
	 * @param stacked
	 *            whether or not the block is the stacked version.
	 */
	public ItemBlockCherrySlab(final Block block, final BlockCherryHalfSlab slab,
			final BlockCherryDoubleSlab doubleSlab, final Boolean stacked) {
		super(block, slab, doubleSlab);
	}
}