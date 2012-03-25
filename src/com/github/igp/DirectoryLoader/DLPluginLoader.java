package com.github.igp.DirectoryLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.bukkit.Server;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.yaml.snakeyaml.error.YAMLException;

public class DLPluginLoader extends JavaPluginLoader {
	@SuppressWarnings("unused")
	private final Server server;
	protected final Pattern[] fileFilters = {
		Pattern.compile(".$")
	};

	public DLPluginLoader(final Server instance) {
		super(instance);
		server = instance;
	}

	@Override
	public Plugin loadPlugin(final File file) throws InvalidPluginException {

		if (!file.isDirectory())
			throw new InvalidPluginException("Not a directory");

		return super.loadPlugin(file);
	}

	@Override
	@SuppressWarnings("unused")
	public PluginDescriptionFile getPluginDescription(final File file) throws InvalidDescriptionException {
		InputStream stream = null;
		try {
			final File config = new File(file.getAbsolutePath() + File.separator + "plugin.yml");

			if (config == null)
				throw new InvalidDescriptionException(new FileNotFoundException("Folder does not contain plugin.yml"));

			stream = new FileInputStream(config);

			return new PluginDescriptionFile(stream);
		}
		catch (final IOException ex) {
			throw new InvalidDescriptionException(ex);
		}
		catch (final YAMLException ex) {
			throw new InvalidDescriptionException(ex);
		}
		finally {
			if (stream != null) {
				try {
					stream.close();
				}
				catch (final IOException e) {
				}
			}
		}
	}

	@Override
	public Pattern[] getPluginFileFilters() {
		return this.fileFilters;
	}
}
