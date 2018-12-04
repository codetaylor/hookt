package com.codetaylor.mc.hookt.modules.hookt.events;

import com.codetaylor.mc.hookt.modules.hookt.ModuleHookt;
import com.codetaylor.mc.hookt.modules.hookt.ModuleHooktConfig;
import com.codetaylor.mc.hookt.modules.hookt.Util;
import com.codetaylor.mc.hookt.modules.hookt.rule.RuleLocator;
import com.codetaylor.mc.hookt.modules.hookt.rule.data.Rule;
import com.codetaylor.mc.hookt.modules.hookt.rule.drop.DropModifier;
import com.codetaylor.mc.hookt.modules.hookt.rule.log.DebugFileWrapper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

  private RuleLocator ruleLocator;
  private DropModifier dropModifier;
  private DebugFileWrapper debugFileWrapper;

  public EventHandler(
      RuleLocator ruleLocator,
      DropModifier dropModifier
  ) {

    this.ruleLocator = ruleLocator;
    this.dropModifier = dropModifier;
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onItemFishedEvent(ItemFishedEvent event) {

    EntityFishHook hookEntity = event.getHookEntity();
    EntityPlayer player = hookEntity.getAngler();
    BlockPos hookPos = hookEntity.getPosition();
    BlockPos playerPos = player.getPosition();
    World world = player.getEntityWorld();
    NonNullList<ItemStack> drops = event.getDrops();

    Rule matchedRule = this.ruleLocator.locate(
        world,
        player,
        drops
    );

    int experience = Util.RANDOM.nextInt(6) + 1;

    if (matchedRule != null) {
      long start = System.currentTimeMillis();

      event.setCanceled(true);

      if (matchedRule.debug) {
        this.initializeDebugFileWrapper();
      }

      this.dropModifier.modifyDrops(
          world,
          playerPos,
          matchedRule,
          drops,
          this.getLuck(player),
          experience,
          this.debugFileWrapper,
          matchedRule.debug
      );

      for (ItemStack itemstack : drops) {

        boolean directlyAdded = false;

        if (ModuleHooktConfig.ENABLE_DIRECT_TO_INVENTORY) {
          directlyAdded = player.addItemStackToInventory(itemstack);
        }

        if (!directlyAdded) {
          EntityItem entityitem = new EntityItem(world, hookPos.getX(), hookPos.getY(), hookPos.getZ(), itemstack);
          double d0 = playerPos.getX() - hookPos.getX();
          double d1 = playerPos.getY() - hookPos.getY();
          double d2 = playerPos.getZ() - hookPos.getZ();
          double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

          entityitem.motionX = d0 * 0.1D;
          entityitem.motionY = d1 * 0.1D + (double) MathHelper.sqrt(d3) * 0.08D;
          entityitem.motionZ = d2 * 0.1D;

          world.spawnEntity(entityitem);

          Item item = itemstack.getItem();

          if (item == Items.FISH || item == Items.COOKED_FISH) {
            player.addStat(StatList.FISH_CAUGHT, 1);
          }
        }
      }

      if (ModuleHooktConfig.ENABLE_PROFILE_LOG_OUTPUT) {

        this.initializeDebugFileWrapper();
        this.debugFileWrapper.info(String.format(
            "Modified drops in %d ms",
            (System.currentTimeMillis() - start)
        ));
      }

    } else {

      while (experience > 0) {
        int xpDrop = EntityXPOrb.getXPSplit(experience);
        experience -= xpDrop;
        world.spawnEntity(new EntityXPOrb(
            world,
            playerPos.getX(),
            playerPos.getY() + 0.5,
            playerPos.getZ(),
            xpDrop
        ));
      }
    }

    this.closeDebugFileWrapper();
  }

  private int getLuck(EntityPlayer player) {

    return (int) (EnchantmentHelper.getFishingLuckBonus(player.getHeldItemMainhand()) + player.getLuck());
  }

  private void initializeDebugFileWrapper() {

    if (this.debugFileWrapper == null) {
      this.debugFileWrapper = new DebugFileWrapper(ModuleHookt.LOG_FILE_WRITER_PROVIDER.createLogFileWriter());
    }
  }

  private void closeDebugFileWrapper() {

    if (this.debugFileWrapper != null) {
      this.debugFileWrapper.close();
      this.debugFileWrapper = null;
    }
  }

}
