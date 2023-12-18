package org.gecko.wauh;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;
import org.gecko.wauh.commands.*;
import org.gecko.wauh.data.ConfigurationManager;
import org.gecko.wauh.enchantments.enchants.weapons.bows.Aim;
import org.gecko.wauh.enchantments.enchants.weapons.bows.Multishot;
import org.gecko.wauh.enchantments.logic.EnchantmentHandler;
import org.gecko.wauh.enchantments.enchants.weapons.swords.Disarm;
import org.gecko.wauh.gui.ConfigGUI;
import org.gecko.wauh.listeners.*;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    ConfigurationManager configManager;
    FileConfiguration config;
    private int playerRadiusLimit;
    private int tntRadiusLimit;
    private int creeperRadiusLimit;
    private boolean showRemoval = true;
    private BucketListener bucketListener;
    private BarrierListener barrierListener;
    private BedrockListener bedrockListener;
    private WaterBucketListener waterBucketListener;
    private TNTListener tntListener;
    private CreeperListener creeperListener;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private final EnchantmentHandler enchantmentHandler = new EnchantmentHandler();

    // Enchantments
    public static Enchantment disarm = new Disarm(100);
    public static Enchantment aim = new Aim(101);
    public static Enchantment multishot = new Multishot(102);


    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Yay");

        // Create instances of the listeners
        bucketListener = new BucketListener();
        barrierListener = new BarrierListener();
        bedrockListener = new BedrockListener();
        waterBucketListener = new WaterBucketListener();
        tntListener = new TNTListener();
        creeperListener = new CreeperListener();
        configManager = new ConfigurationManager(Main.getPlugin(Main.class));
        config = configManager.getConfig();
        ConfigGUI configGUI = new ConfigGUI(this);

        // Register the listeners
        getServer().getPluginManager().registerEvents(bucketListener, this);
        getServer().getPluginManager().registerEvents(barrierListener, this);
        getServer().getPluginManager().registerEvents(bedrockListener, this);
        getServer().getPluginManager().registerEvents(waterBucketListener, this);
        getServer().getPluginManager().registerEvents(tntListener, this);
        getServer().getPluginManager().registerEvents(creeperListener, this);
        getServer().getPluginManager().registerEvents(configGUI, this);

        // Create enchant instances
        Disarm disarmListener = new Disarm(100);
        Aim aimListener = new Aim(101);
        Multishot multishotListener = new Multishot(102);

        // Enchantment listeners
        getServer().getPluginManager().registerEvents(disarmListener, this);
        getServer().getPluginManager().registerEvents(aimListener, this);
        getServer().getPluginManager().registerEvents(multishotListener, this);

        // Register Enchantments
        try {
            registerEnchantment(disarm);
            registerEnchantment(aim);
            registerEnchantment(multishot);
        } catch (IllegalArgumentException ignored) {}

        // Register commands
        this.getCommand("stopwauh").setExecutor(new StopWauh(bucketListener, barrierListener, bedrockListener, waterBucketListener));
        this.getCommand("setradiuslimit").setExecutor(new SetRadiusLimitCommand(this));
        this.getCommand("setradiuslimit").setTabCompleter(new SetRadiusLimitCommand(this));
        this.getCommand("toggleremovalview").setExecutor(new ToggleRemovalView(this));
        this.getCommand("test").setExecutor(new test(configGUI));
        this.getCommand("givecustomitems").setExecutor(new GiveCustomItems());
        this.getCommand("givecustomitems").setTabCompleter(new SetRadiusLimitCommand(this));
        this.getCommand("ench").setExecutor(new Ench());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Enchantment.stopAcceptingRegistrations();
    }

    public int getRadiusLimit() {
        playerRadiusLimit = config.getInt("playerRadiusLimit", playerRadiusLimit);
        return playerRadiusLimit + 2;
    }

    public void setRadiusLimit(int newLimit) {
        playerRadiusLimit = newLimit;
        config.set("playerRadiusLimit", playerRadiusLimit);
        configManager.saveConfig();
    }

    public int getTntRadiusLimit() {
        tntRadiusLimit = config.getInt("tntRadiusLimit", tntRadiusLimit);
        return tntRadiusLimit + 2;
    }

    public void setTntRadiusLimit(int newLimit) {
        tntRadiusLimit = newLimit;
        config.set("tntRadiusLimit", tntRadiusLimit);
        configManager.saveConfig();
    }

    public int getCreeperRadiusLimit() {
        creeperRadiusLimit = config.getInt("creeperRadiusLimit", creeperRadiusLimit);
        return creeperRadiusLimit + 2;
    }

    public void setCreeperLimit(int newLimit) {
        creeperRadiusLimit = newLimit;
        config.set("creeperRadiusLimit", creeperRadiusLimit);
        configManager.saveConfig();
    }

    public boolean getShowRemoval() {
        return showRemoval;
    }

    public void setRemovalView(boolean newShowRemoval) {
        showRemoval = newShowRemoval;
    }

    public BucketListener getBucketListener() {
        return bucketListener;
    }

    public BarrierListener getBarrierListener() {
        return barrierListener;
    }

    public BedrockListener getBedrockListener() {
        return bedrockListener;
    }

    public WaterBucketListener getWaterBucketListener() {
        return waterBucketListener;
    }

    public TNTListener getTntListener() {
        return tntListener;
    }

    public CreeperListener getCreeperListener() {
        return creeperListener;
    }

    public EnchantmentHandler getEnchantmentHandler() {
        return enchantmentHandler;
    }

    public static void registerEnchantment(Enchantment enchantment) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
        } catch (Exception e) {
            registered = false;
            logger.log(Level.SEVERE, "Error while registering enchantment " + enchantment + " Error:" + e);

        }
        if (registered){
            // It's been registered!
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "" + enchantment + "Registered");
        }
    }
}