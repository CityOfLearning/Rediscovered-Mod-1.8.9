package com.stormister.rediscovered;

import java.util.Random;

import com.stormister.rediscovered.entity.EntityScarecrow;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RediscoveredEventHandler {
	protected static Random itemRand = new Random();

	public static BlockPos verifyRespawnCoordinates(World par0World, BlockPos par1ChunkCoordinates, boolean par2,
			EntityPlayerMP player) {
		if (player.mcServer.worldServerForDimension(0).getBlockState(par1ChunkCoordinates).getBlock()
				.equals(Blocks.bed)) {
			return par1ChunkCoordinates;
		} else {
			return null;
		}
	}

	Random gen = new Random();

	@SubscribeEvent
	public void onArrowLooseEvent(ArrowLooseEvent event) {
		EntityPlayer player = event.entityPlayer;
		InventoryPlayer inv = player.inventory;
		ItemStack par1ItemStack = inv.getStackInSlot(inv.currentItem);
		ItemStack blah = new ItemStack(Items.bow);
		ItemStack quiver = new ItemStack(Rediscovered.Quiver);
		ItemStack lquiver = new ItemStack(Rediscovered.LeatherQuiver);
		ItemStack cquiver = new ItemStack(Rediscovered.ChainQuiver);
		ItemStack gquiver = new ItemStack(Rediscovered.GoldQuiver);
		ItemStack iquiver = new ItemStack(Rediscovered.IronQuiver);
		ItemStack dquiver = new ItemStack(Rediscovered.DiamondQuiver);
		ItemStack lcquiver = new ItemStack(Rediscovered.LeatherChainQuiver);
		if (inv.getCurrentItem().equals(blah)) {

			EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack);
			ItemStack itemstack = player.inventory.armorInventory[2];

			if ((itemstack != null) && (itemstack.equals(quiver) || itemstack.equals(lquiver)
					|| itemstack.equals(cquiver) || itemstack.equals(iquiver) || itemstack.equals(gquiver)
					|| itemstack.equals(dquiver) || itemstack.equals(lcquiver))) {
				event.setCanceled(true);
			}

		}
	}

	// Quiver Bow
	@SubscribeEvent
	public void onArrowNockEvent(ArrowNockEvent event) {
		EntityPlayer player = event.entityPlayer;
		InventoryPlayer inv = player.inventory;
		ItemStack par1ItemStack = inv.getStackInSlot(inv.currentItem);

		boolean flag = player.capabilities.isCreativeMode
				|| (EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0);
		ItemStack itemstack = player.inventory.armorInventory[2];

		if ((itemstack != null) && ((itemstack.getItem() == Rediscovered.Quiver)
				|| (itemstack.getItem() == Rediscovered.LeatherQuiver)
				|| (itemstack.getItem() == Rediscovered.ChainQuiver) || (itemstack.getItem() == Rediscovered.GoldQuiver)
				|| (itemstack.getItem() == Rediscovered.IronQuiver)
				|| (itemstack.getItem() == Rediscovered.DiamondQuiver)
				|| (itemstack.getItem() == Rediscovered.LeatherChainQuiver))) {
			if (player.inventory.hasItem(Items.arrow) || flag) {
				EntityArrow entityarrow = new EntityArrow(player.worldObj, player, 1.0F);
				player.worldObj.playSoundAtEntity(player, "random.bow", 1.0F,
						1.0F / ((itemRand.nextFloat() * 0.4F) + 0.8F));
				entityarrow.setIsCritical(true);

				if (!player.worldObj.isRemote) {
					if (!flag) {
						player.inventory.consumeInventoryItem(Items.arrow);
					} else {
						entityarrow.canBePickedUp = 0;
					}
					player.worldObj.spawnEntityInWorld(entityarrow);
				}
				event.setCanceled(true);
			}
		}

	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if ((event.entity instanceof EntityPlayer) && (ExtendedPlayer.get((EntityPlayer) event.entity) == null)) {
			ExtendedPlayer.register((EntityPlayer) event.entity);
		}
	}

	// Mob AI
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityMob) {
			EntityCreature entity = (EntityCreature) event.entity;
			if (Rediscovered.ScarecrowAttractsMobs) {
				entity.targetTasks.addTask(2,
						new EntityAINearestAttackableTarget(entity, EntityScarecrow.class, false));
				entity.tasks.addTask(4, new EntityAIAttackOnCollide(entity, EntityScarecrow.class, 0.8D, true));
			} else {
				entity.tasks.addTask(1, new EntityAIAvoidEntity(entity, EntityScarecrow.class, 8.0F, 0.6D, 0.6D));
			}
		}
		if (event.entity instanceof EntityAnimal) {
			EntityCreature entity = (EntityCreature) event.entity;
			entity.tasks.addTask(1, new EntityAIAvoidEntity(entity, EntityScarecrow.class, 8.0F, 0.6D, 0.6D));
		}
	}

	@SubscribeEvent
	public void onLivingHurtEvent(LivingHurtEvent event) {
		if (!event.entityLiving.worldObj.isRemote && (event.entityLiving instanceof EntityPlayerMP)
				&& (event.entityLiving.dimension == Rediscovered.DimID)
				&& event.source.damageType.equals("outOfWorld")) {
			final WorldServer world = (WorldServer) event.entityLiving.worldObj;
			final Object[] entityList = world.loadedEntityList.toArray();
			for (final Object o : entityList) {
				if (o instanceof EntityPlayerMP) {
					final EntityPlayerMP e = (EntityPlayerMP) o;

					if (e.posY <= 10) {
						ExtendedPlayer props = ExtendedPlayer.get((EntityPlayer) event.entity);
						event.setCanceled(true);
						e.mcServer.getConfigurationManager().transferPlayerToDimension(e, 0,
								new SkyDimensionTeleporter(e.mcServer.worldServerForDimension(0)));
						BlockPos coordinates = props.getRespawn();
						if (coordinates != null) {
							coordinates = verifyRespawnCoordinates(e.mcServer.worldServerForDimension(0), coordinates,
									true, e);
						}
						if (coordinates == null) {
							coordinates = world.getSpawnPoint();
							e.setPositionAndUpdate(coordinates.getX() + 0.5D, (double) coordinates.getY() + 3,
									coordinates.getZ() + 0.5D);
							e.addChatComponentMessage(
									new ChatComponentTranslation("message.missingbed", new Object[0]));
						} else if (coordinates != null) {
							e.setPositionAndUpdate(coordinates.getX() + 0.5D, (double) coordinates.getY() + 3,
									coordinates.getZ() + 0.5D);
						}
						e.fallDistance = 0;
					}

				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		InventoryPlayer inv = player.inventory;
		ItemStack itemStack = inv.getStackInSlot(inv.currentItem);
		final World world = event.entityLiving.worldObj;

		if (!event.entityLiving.worldObj.isRemote && (event.action == Action.RIGHT_CLICK_BLOCK)
				&& world.getBlockState(event.pos).getBlock().equals(Blocks.bed)
				&& (((itemStack != null) && itemStack.getItem().equals(Rediscovered.DreamPillow)
						&& (Rediscovered.DaytimeBed || (!Rediscovered.DaytimeBed && !world.isDaytime())))
						|| (player.dimension == Rediscovered.DimID))
				&& (player instanceof EntityPlayerMP)) {
			event.setCanceled(true);
			EntityPlayerMP thePlayer = (EntityPlayerMP) player;
			ExtendedPlayer props = ExtendedPlayer.get((EntityPlayer) event.entity);
			if (Math.abs(player.dimension) != 1) { // not the end or nether
				props.setRespawn(event.pos.getX(), event.pos.getY(), event.pos.getZ());
				thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, Rediscovered.DimID,
						new SkyDimensionTeleporter(thePlayer.mcServer.worldServerForDimension(Rediscovered.DimID)));
			} else if (player.dimension == Rediscovered.DimID) {
				thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, 0,
						new SkyDimensionTeleporter(thePlayer.mcServer.worldServerForDimension(0)));
				BlockPos coordinates = props.getRespawn();
				if (coordinates != null) {
					coordinates = verifyRespawnCoordinates(thePlayer.mcServer.worldServerForDimension(0), coordinates,
							true, thePlayer);
				}
				if (coordinates == null) {
					coordinates = world.getSpawnPoint();
					thePlayer.setPositionAndUpdate(coordinates.getX() + 0.5D, (double) coordinates.getY() + 3,
							coordinates.getZ() + 0.5D);
					player.addChatComponentMessage(new ChatComponentTranslation("message.missingbed", new Object[0]));
				} else if (coordinates != null) {
					thePlayer.setPositionAndUpdate(coordinates.getX() + 0.5D, (double) coordinates.getY() + 3,
							coordinates.getZ() + 0.5D);
				}

			}
		}

		// Bush Shearing
		if ((event.action == Action.RIGHT_CLICK_BLOCK)
				&& world.getBlockState(event.pos).getBlock().equals(Blocks.double_plant) && (itemStack != null)
				&& itemStack.getItem().equals(Items.shears) && (player instanceof EntityPlayerMP)) {
			event.setCanceled(true);
			if ((world.getBlockState(event.pos) == Blocks.double_plant.getStateFromMeta(4))
					|| (world.getBlockState(event.pos.down()) == Blocks.double_plant.getStateFromMeta(4))) {
				if (world.getBlockState(event.pos) == Blocks.double_plant.getStateFromMeta(4)) {
					world.setBlockState(event.pos, Rediscovered.EmptyRoseBush.getDefaultState());
					world.setBlockState(event.pos.up(), Rediscovered.EmptyRoseBushTop.getDefaultState());
				} else if ((world.getBlockState(event.pos.down()) == Blocks.double_plant.getStateFromMeta(4))) {
					world.setBlockState(event.pos.down(), Rediscovered.EmptyRoseBush.getDefaultState());
					world.setBlockState(event.pos, Rediscovered.EmptyRoseBushTop.getDefaultState());
				}
				ItemStack itemStack2 = new ItemStack(Rediscovered.Rose, world.rand.nextInt(3) + 1);
				EntityItem item = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
						itemStack2);
				world.spawnEntityInWorld(item);
			}
			if ((world.getBlockState(event.pos) == Blocks.double_plant.getStateFromMeta(5))
					|| (world.getBlockState(event.pos.down()) == Blocks.double_plant.getStateFromMeta(5))) {
				if (world.getBlockState(event.pos) == Blocks.double_plant.getStateFromMeta(5)) {
					world.setBlockState(event.pos, Rediscovered.EmptyPeonyBush.getDefaultState());
					world.setBlockState(event.pos.up(), Rediscovered.EmptyPeonyBushTop.getDefaultState());
				} else if ((world.getBlockState(event.pos.down()) == Blocks.double_plant.getStateFromMeta(5))) {
					world.setBlockState(event.pos.down(), Rediscovered.EmptyPeonyBush.getDefaultState());
					world.setBlockState(event.pos, Rediscovered.EmptyPeonyBushTop.getDefaultState());
				}
				ItemStack itemStack2 = new ItemStack(Rediscovered.Peony, world.rand.nextInt(3) + 1);
				EntityItem item = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
						itemStack2);
				world.spawnEntityInWorld(item);
			}
			itemStack.damageItem(1, player);
		}
	}

	// Lantern
	@SubscribeEvent
	public void onPlayerMove(LivingUpdateEvent e) {
		if (e.entityLiving instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) e.entityLiving;
			if (!p.worldObj.isRemote) {
				if (Rediscovered.hasLitLanternOnHotbar(p.inventory)) {
					BlockPos pos = new BlockPos(MathHelper.floor_double(p.posX), MathHelper.floor_double(p.posY) + 1,
							MathHelper.floor_double(p.posZ));

					if (p.ridingEntity != null) {
						pos = new BlockPos(MathHelper.floor_double(p.ridingEntity.posX),
								MathHelper.floor_double(p.ridingEntity.posY) + 1,
								MathHelper.floor_double(p.ridingEntity.posZ));
					}

					if (p.worldObj.getBlockState(pos).equals(Blocks.air.getDefaultState())) {
						p.worldObj.setBlockState(pos, Rediscovered.Lantern.getDefaultState());
					}

					if (Rediscovered.usernameLastPosMap.containsKey(p.getDisplayNameString())) {
						BlockPos pos2 = Rediscovered.usernameLastPosMap.get(p.getDisplayNameString());

						if (((pos2.getX() != pos.getX()) || (pos2.getY() != pos.getY()) || (pos2.getZ() != pos.getZ()))
								&& p.worldObj.getBlockState(pos2).equals(Rediscovered.Lantern.getDefaultState())) {
							p.worldObj.setBlockToAir(pos2);
						}
					}
					// TODO Find correct string for username
					Rediscovered.usernameLastPosMap.put(p.getDisplayNameString(), pos);
				} else {
					if (Rediscovered.usernameLastPosMap.containsKey(p.getDisplayNameString())) {
						BlockPos pos = Rediscovered.usernameLastPosMap.get(p.getDisplayNameString());

						if (p.worldObj.getBlockState(pos).equals(Rediscovered.Lantern.getDefaultState())) {
							p.worldObj.setBlockToAir(pos);
						}

						Rediscovered.usernameLastPosMap.remove(p.getDisplayNameString());
					}
				}
			}
		}
	}

	// Sky Dimension Teleportation
	@SubscribeEvent
	public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
		EntityPlayer player = event.entityPlayer;
		InventoryPlayer inv = player.inventory;
		ItemStack itemStack = inv.getStackInSlot(inv.currentItem);
		World world = event.entityLiving.worldObj;

		if ((Math.abs(player.dimension) != 1) && !world.isDaytime()
				&& (itemRand.nextInt(100) <= Rediscovered.DreamChance)
				&& ((itemStack == null) || (itemStack.getItem() != Rediscovered.DreamPillow))
				&& (player instanceof EntityPlayerMP)) {
			ExtendedPlayer props = ExtendedPlayer.get((EntityPlayer) event.entity);
			props.setRespawn(event.pos.getX(), event.pos.getY(), event.pos.getZ());
			EntityPlayerMP thePlayer = (EntityPlayerMP) player;
			thePlayer.mcServer.getConfigurationManager().transferPlayerToDimension(thePlayer, Rediscovered.DimID,
					new SkyDimensionTeleporter(thePlayer.mcServer.worldServerForDimension(Rediscovered.DimID)));
		}
	}
}
