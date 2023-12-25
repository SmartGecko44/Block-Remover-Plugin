package org.gecko.wauh.enchantments.tools.pickaxes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class Smelt extends Enchantment implements Listener {

    //FIXME

    private final Set<Material> SMELTABLE_MATERIALS = EnumSet.of(Material.IRON_ORE, Material.GOLD_ORE);

    public Smelt() {
        super(104);
    }

    @Override
    public String getName() {
        return "Smelt";
    }

    @Override
    public int getMaxLevel() {
        return 1; // You can adjust the maximum level as needed
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return enchantment == Enchantment.getByName("Drill");
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType().equals(Material.DIAMOND_PICKAXE);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // This uses a map of all enchantments because for some reason, using the preexisting function doesn't work
        Map<Enchantment, Integer> itemEnch = event.getPlayer().getInventory().getItemInMainHand().getEnchantments();
        if (itemEnch.containsKey(Enchantment.getByName("Smelt"))) {
            event.getBlock().getDrops().clear();
            if (SMELTABLE_MATERIALS.contains(event.getBlock().getType())) {
                switch (event.getBlock().getType()) {
                    case IRON_ORE:
                        event.getBlock().getDrops().add(new ItemStack(Material.IRON_INGOT, 1));
                        break;
                    case GOLD_ORE:
                        event.getBlock().getDrops().add(new ItemStack(Material.GOLD_INGOT, 1));
                        break;
                }
            } else {
                event.getBlock().getDrops().add(new ItemStack(event.getBlock().getType()));
            }
        }
    }
}
