package com.github.igp.DirectoryLoader;

import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class DLMain extends JavaPlugin
{
	private Logger log;

	@Override
	public void onEnable()
	{
		log = this.getLogger();
		
		final File configFile = new File(getDataFolder().getAbsolutePath() + File.separator + "config.yml");
		if ((configFile == null) || !configFile.exists())
		{
			log.info("Configuration file not found: saving default");
			saveResource("dlconfig.yml", false);
			final File f = new File(getDataFolder().getAbsolutePath() + File.separator + "dlconfig.yml");
			if(!f.renameTo(configFile))
				log.warning("Unable to rename configuration file: please manually rename dlconfig.yml to config.yml and restart the server");
		}
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerInterface(DLPluginLoader.class);
		
		log.info("Starting to load plugins");
		for (final String directory : getConfig().getStringList("Directories"))
		{
			final File f = new File(directory);
			
			try
			{
				if (f.exists())
				{
				if (f.isDirectory())
					{
						Plugin plugin = pm.loadPlugin(f);
						log.info("Loading " + plugin.getDescription().getFullName());
					}
					else
						log.severe("Not a directory: " + f.getAbsolutePath());
				}
				else
					log.severe("Does not exist: " + f.getAbsolutePath());
			}
			catch (UnknownDependencyException e)
			{
				log.severe("Unable to load dependency for plugin at: " + f.getAbsolutePath());
			}
			catch (InvalidPluginException e)
			{
				log.severe("Invalid plugin at: " + f.getAbsolutePath());
			}
			catch (InvalidDescriptionException e)
			{
				log.severe("Invalid plugin at: " + f.getAbsolutePath());
			}
		}
		log.info("Finished loading plugins");
	}

	@Override
	public void onDisable()
	{
		log.info("Disabled.");
	}
}
