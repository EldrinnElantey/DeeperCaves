package com.kpabr.DeeperCaves;
/*
 * Created by Valiec2019
 * started on July 6, 2013
 * released on November 28, 2013
 * currently using Minecraft Forge 10.12.0.1022
 */

import java.io.IOException;
import java.net.UnknownHostException;

import com.kpabr.DeeperCaves.CommonProxy;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.LivingFallEvent;
@Mod(modid = DeeperCaves.MODID, version = DeeperCaves.VERSION, name = DeeperCaves.NAME)//, guiFactory = "com.kpabr.DeeperCaves.EndPlusConfigGUIFactory")
public class DeeperCaves
{
    @SidedProxy(clientSide="com.kpabr.DeeperCaves.client.ClientProxy", serverSide="com.kpabr.DeeperCaves.CommonProxy")
    public static CommonProxy proxy;
 
    /*Mod ID and Version declarations*/
    public static final String MODID = "DeeperCaves";
    public static final String VERSION = "0.2.0";
    public static final String NAME = "DeeperCaves";
    static int versionID = 3; //Used by version checker!
    
    static DeeperBlocks blocks = new DeeperBlocks();
    static DeeperItems items = new DeeperItems();
    static DeeperRecipes recipes = new DeeperRecipes();
    static DeeperWorldgen worldgen = new DeeperWorldgen();
    //static DeeperRendering rendering = new DeeperRendering();
    //static DeeperMobs mobs = new DeeperMobs();
    static DeeperVersionChecker versionChecker = new DeeperVersionChecker();
    public static DeeperCaves instance;
    public static Configuration config;
    public int nearvoid_counter = 0;
    
    static CreativeTabs tabDeeperCaves = new TabDeeperCavesBlocks(CreativeTabs.getNextID(), "Deeper Caves Blocks", DeeperCaves.blocks.fragmentedBedrock);
    static CreativeTabs tabDeeperCavesItems = new TabDeeperCaves(CreativeTabs.getNextID(), "Deeper Caves Items", 0);
    static CreativeTabs tabDeeperCavesTools = new TabDeeperCaves(CreativeTabs.getNextID(), "Deeper Caves Tools", 1);
    
    
    //static CreativeTabs tabEndplus = new TabEndplusBlocks(CreativeTabs.getNextID(), "EndPlus Blocks", DeeperCaves.blocks.endGrass);
    //static CreativeTabs tabEndplusSpawners = new TabEndplusSpawners(CreativeTabs.getNextID());
    //static CreativeTabs tabEndplusItems = new TabEndplus(CreativeTabs.getNextID(), "EndPlus Items", 0);
    //static CreativeTabs tabEndplusRedstone = new TabEndplus(CreativeTabs.getNextID(), "EndPlus Ender Redstone", 3);
    //static CreativeTabs tabEndplusTools = new TabEndplus(CreativeTabs.getNextID(), "EndPlus Tools", 1);
    //static CreativeTabs tabEndplusCombat = new TabEndplus(CreativeTabs.getNextID(), "EndPlus Combat", 2);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        config = new Configuration(event.getSuggestedConfigurationFile()); //gets default config file
        
        this.instance = this;
        
        
        FMLCommonHandler.instance().bus().register(worldgen);
        MinecraftForge.EVENT_BUS.register(worldgen);
        
        FMLCommonHandler.instance().bus().register(versionChecker);
        MinecraftForge.EVENT_BUS.register(versionChecker);
        
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        
        
        
        //ClientCommandHandler.instance.registerCommand(new TestCommand());
        ClientCommandHandler.instance.registerCommand(new VersionCommand());
   	    /*DeeperCaves.config.load();
        if(!config.hasKey(Configuration.CATEGORY_GENERAL, "OverrideDimensionID"))
        {
        worldgen.dimID = DeeperCaves.config.getInt("OverrideDimensionID", Configuration.CATEGORY_GENERAL, DeeperCaves.config.get(Configuration.CATEGORY_GENERAL, "OverrideDimensionID", 6).getInt(), 2, 255, "Used to help generate the End");
        }
        if(!config.hasKey(Configuration.CATEGORY_GENERAL, "AutoUpdate"))
        {
        DeeperCaves.config.getBoolean("AutoUpdate", Configuration.CATEGORY_GENERAL, true, "Sets whether the auto-upater will run");
        }
        DeeperCaves.config.save();*/

        
        
     	blocks.setupBlocks();
     	items.setupItems();
     	blocks.registerBlocks();
     	blocks.registerBlocksOreDict();
     	items.registerItems();
     	items.registerItemsOreDict();
        blocks.setupHarvestLevels();
     	recipes.setupShapelessCrafting();
     	recipes.setupShapedCrafting();
     	recipes.setupSmelting();
     	worldgen.setupWorldgen();
     	//rendering.setupArmorRenderers();
     	//mobs.setupMobs();
     	//proxy.registerRenderers();
     	//VillagerRegistry.instance().getRegisteredVillagers(); //Does nothing at this time, to be used for quest villager   
    }
    @EventHandler
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if(true)//event.modID.equals("endplus"))
        {
            updateConfig();
        }
    }
    public static void updateConfig()
    {
    	/*
         worldgen.dimID = DeeperCaves.config.get(Configuration.CATEGORY_GENERAL, "OverrideDimensionID", 6).getInt();
         //versionChecker.doUpdate = 
         System.out.println(DeeperCaves.config.get(Configuration.CATEGORY_GENERAL, "AutoUpdate", false));
         
         if(true)
         {
             config.save();
         }*/
    }
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
    	//System.out.println("Hello!");
        	if(event.player.posY <= 1.0D)
        	{
        		try
            	{
            	EntityPlayerMP player = (EntityPlayerMP)event.player;
            	if(player.dimension == 14)
            	{
            	player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 0, new DeeperTeleporter(player.mcServer.worldServerForDimension(0)));
            	}
            	else if(player.dimension == 0)
            	{
            	player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 7, new DeeperTeleporter(player.mcServer.worldServerForDimension(7)));
            	}
            	else if(player.dimension>=7 && player.dimension<14)
            	{
            	player.mcServer.getConfigurationManager().transferPlayerToDimension(player, player.dimension+1, new DeeperTeleporter(player.mcServer.worldServerForDimension(player.dimension+1)));
            	}
            	else{}
            	}
            	catch(ClassCastException e)
            	{
            		return; //not a player
            	}
        	}
        	else if(event.player.posY >= 253.0D)
        	{
        		try
            	{
            	EntityPlayerMP player = (EntityPlayerMP)event.player;
            	if(player.dimension == 7)
            	{
            	player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 0, new DeeperTeleporter(player.mcServer.worldServerForDimension(0)));
            	}
            	else if(player.dimension>7 && player.dimension<=14)
            	{
            	player.mcServer.getConfigurationManager().transferPlayerToDimension(player, player.dimension-1, new DeeperTeleporter(player.mcServer.worldServerForDimension(player.dimension-1)));
            	}
            	else{}
            	}
            	catch(ClassCastException e)
            	{
            		return; //not a player
            	}
        	}
        	else{}
    		try
            {
            EntityPlayerMP player = (EntityPlayerMP)event.player;
    		if(event.player.posY <= 220.0D && player.dimension == 14 && this.nearvoid_counter == 200)
    		{
            	player.attackEntityFrom(DamageSource.outOfWorld, 0.5F);
            	this.nearvoid_counter = 0;
    		}
    		else
    		{
    			this.nearvoid_counter = this.nearvoid_counter+1;
    		}
            }
            catch(ClassCastException e)
            {
            	return; //not a player
            }
    }
}

