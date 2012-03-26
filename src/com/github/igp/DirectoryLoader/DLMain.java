package com.github.igp.DirectoryLoader;

import java.io.File;
import java.util.logging.Logger;

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
					getServer().getPluginManager().enablePlugin(plugin);
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
