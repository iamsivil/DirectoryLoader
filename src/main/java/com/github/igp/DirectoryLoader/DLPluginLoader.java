package com.github.igp.DirectoryLoader;

import org.bukkit.Server;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.*;
import java.util.regex.Pattern;

@SuppressWarnings("UnusedDeclaration")
public class DLPluginLoader extends JavaPluginLoader
{
	@SuppressWarnings("FieldCanBeLocal")
	private final Server server;
	protected final Pattern[] fileFilters =
	{
		Pattern.compile(".$")
	};

	public DLPluginLoader(final Server instance)
	{
		super(instance);
		server = instance;
	}

	@Override
	public Plugin loadPlugin(final File file) throws InvalidPluginException
	{
		if (!file.isDirectory())
			throw new InvalidPluginException("Not a directory");

		return super.loadPlugin(file);
	}

	@Override
	public PluginDescriptionFile getPluginDescription(final File file) throws InvalidDescriptionException
	{
		InputStream stream = null;
		PluginDescriptionFile pluginDesc = null;

		try
		{
			final File desc = new File(file.getAbsolutePath() + File.separator + "plugin.yml");

			if ((desc == null) || (!desc.exists()))
				throw new InvalidDescriptionException(new FileNotFoundException("Folder does not contain plugin.yml"));

			stream = new FileInputStream(desc);
		}
		catch (final IOException ex)
		{
			throw new InvalidDescriptionException(ex);
		}
		catch (final YAMLException ex)
		{
			throw new InvalidDescriptionException(ex);
		}
		finally
		{
			if (stream != null)
			{
				pluginDesc = new PluginDescriptionFile(stream);

				try
				{
					stream.close();
				}
				catch (final IOException ignore)
				{
				}
			}
		}

		return pluginDesc;
	}

	@Override
	public Pattern[] getPluginFileFilters()
	{
		return this.fileFilters;
	}
}
