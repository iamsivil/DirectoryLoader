package com.github.igp.DirectoryLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.UnknownDependencyException;
import org.bukkit.plugin.java.JavaPlugin;

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
			saveDefaultConfig();
		}

		final DLPluginLoader pluginLoader = new DLPluginLoader(getServer());

		for (final String directory : getConfig().getStringList("directories"))
		{
			final File f = new File(directory);
			Plugin plugin = null;

			try
			{
				plugin = pluginLoader.loadPlugin(f);
			}
			catch (final InvalidPluginException e)
			{
				log.severe("Invalid plugin at: " + f.getAbsolutePath());
			}
			catch (final UnknownDependencyException e)
			{
				log.severe("Unable to load dependency for plugin at: " + f.getAbsolutePath());
			}
			finally
			{
				if (plugin == null)
					log.severe("Error enabling plugin at: " + f.getAbsolutePath());
				else
				{
					log.info("Loading " + plugin.getDescription().getFullName());
					
					try
					{
						File pluginConfig = new File(f.getAbsolutePath() + File.separator + "config.yml");
						if ((pluginConfig != null) && pluginConfig.exists())
						{
							plugin.getConfig().load(f.getAbsolutePath() + File.separator + "config.yml");
						}	
					}
					catch (final FileNotFoundException e)
					{
					}
					catch (final IOException e)
					{
					}
					catch (final InvalidConfigurationException e)
					{
					}
					
					pluginLoader.enablePlugin(plugin);
				}
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
