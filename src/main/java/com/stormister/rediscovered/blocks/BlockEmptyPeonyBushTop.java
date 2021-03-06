package com.stormister.rediscovered.blocks;

import java.util.Random;

import com.stormister.rediscovered.Rediscovered;
import com.stormister.rediscovered.RediscoveredItemsManager;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEmptyPeonyBushTop extends Block {
	private final String name = "EmptyPeonyBushTop";

	public BlockEmptyPeonyBushTop() {
		this(Material.plants);
	}

	protected BlockEmptyPeonyBushTop(Material p_i45395_1_) {
		super(p_i45395_1_);
		setTickRandomly(true);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(Rediscovered.modid + "_" + name);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except
	 * gets checked often with plants.
	 */
	public boolean canBlockStay(World p_149718_1_, BlockPos pos) {
		return p_149718_1_.getBlockState(pos.down()).equals(RediscoveredItemsManager.EmptyPeonyBush.getDefaultState());
	}

	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos);
	}

	/**
	 * is the block grass, dirt or farmland
	 */
	protected boolean canPlaceBlockOn(Block ground) {
		return (ground == Blocks.grass) || (ground == Blocks.dirt) || (ground == Blocks.farmland);
	}

	/**
	 * checks if the block can stay, if not drop as item
	 */
	protected void checkAndDropBlock(World world, BlockPos pos) {
		if (!canBlockStay(world, pos)) {
			world.setBlockToAir(pos);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	/**
	 * Returns a bounding box from the pool of bounding boxes (this means this
	 * box can change after the pool has been cleared to be reused)
	 */
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(RediscoveredItemsManager.EmptyPeonyBush);
	}

	public String getName() {
		return name;
	}

	@SideOnly(Side.CLIENT)

	/**
	 * only called by clickMiddleMouseButton , and passed to
	 * inventory.setCurrentItem (along with isCreative)
	 */
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
		return new ItemStack(RediscoveredItemsManager.EmptyPeonyBush);
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean isNormalCube() {
		return false;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		worldIn.setBlockToAir(pos.down());
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockToAir(pos.down());
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor Block
	 */
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		checkAndDropBlock(worldIn, pos);
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		checkAndDropBlock(worldIn, pos);
	}
}