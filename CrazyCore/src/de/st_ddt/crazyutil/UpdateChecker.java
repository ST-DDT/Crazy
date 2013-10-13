package de.st_ddt.crazyutil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * This class is based on an example from: <br>
 * <a href=https://github.com/h31ix/ServerModsAPI-Example/blob/master/Update.java> ServerModsAPI-Example / Update.java by h31ix</a>
 */
public class UpdateChecker
{

	// An optional API key to use, will be null if not submitted
	private static String apiKey;
	// Keys for extracting file information from JSON response
	private static final String API_NAME_VALUE = "name";
	private static final String API_LINK_VALUE = "downloadUrl";
	private static final String API_RELEASE_TYPE_VALUE = "releaseType";
	// private static final String API_FILE_NAME_VALUE = "fileName";
	private static final String API_GAME_VERSION_VALUE = "gameVersion";
	// Static information for querying the API
	private static final String API_QUERY = "/servermods/files?projectIds=";
	private static final String API_HOST = "https://api.curseforge.com";
	// Project
	private final String projectName;
	private final String projectVersion;
	private final int projectID;
	// Latest
	private String latestTitle;
	private String latestLink;
	private String latestType;
	// private String latestFileName;
	private String latestVersion;
	private String latestGameVersion;
	// Update
	private boolean hasUpdate;
	private boolean queried = false;

	/**
	 * Check for updates
	 * 
	 * @param projectID
	 *            The BukkitDev Project ID, found in the "Facts" panel on the right-side of your project page.
	 */
	public UpdateChecker(final String projectName, final String projectVersion, final int projectID)
	{
		super();
		this.projectName = projectName;
		this.projectVersion = projectVersion;
		this.projectID = projectID;
	}

	/**
	 * Query the API to find the latest approved file's details.
	 */
	public boolean query()
	{
		queried = true;
		URL url = null;
		try
		{
			// Create the URL to query using the project's ID
			url = new URL(API_HOST + API_QUERY + projectID);
		}
		catch (final MalformedURLException e)
		{
			// There was an error creating the URL
			e.printStackTrace();
			return hasUpdate;
		}
		try
		{
			// Open a connection and query the project
			final URLConnection conn = url.openConnection();
			if (apiKey != null)
				// Add the API key to the request if present
				conn.addRequestProperty("X-API-Key", apiKey);
			// Add the user-agent to identify the program
			conn.addRequestProperty("User-Agent", "ServerModsAPI-Example (by Gravity)");
			// Read the response of the query
			// The response will be in a JSON format, so only reading one line is necessary.
			final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final String response = reader.readLine();
			// Parse the array of files from the query's response
			final JSONArray array = (JSONArray) JSONValue.parse(response);
			if (array.size() > 0)
			{
				// Get the newest file's details
				final JSONObject latest = (JSONObject) array.get(array.size() - 1);
				// Get the version's title
				latestTitle = (String) latest.get(API_NAME_VALUE);
				// Get the version's link
				latestLink = (String) latest.get(API_LINK_VALUE);
				// Get the version's release type
				latestType = (String) latest.get(API_RELEASE_TYPE_VALUE);
				// Get the version's file name
				// latestFileName = (String) latest.get(API_FILE_NAME_VALUE);
				// Get the version's game version
				latestGameVersion = (String) latest.get(API_GAME_VERSION_VALUE);
				latestVersion = latestTitle.substring(projectName.length() + 2);
				hasUpdate = VersionComparator.compareVersions(projectVersion, latestVersion) == -1;
			}
		}
		catch (final IOException e)
		{
			// There was an error reading the query
			e.printStackTrace();
		}
		return hasUpdate;
	}

	/**
	 * @return The Server dependend API-Key, found at <a href=>https://dev.bukkit.org/home/servermods-apikey/</a>
	 */
	public final static String getApiKey()
	{
		return apiKey;
	}

	/**
	 * @param apiKey
	 *            The Server dependend API-Key, found at <a href=>https://dev.bukkit.org/home/servermods-apikey/</a>
	 */
	public final static void setApiKey(final String apiKey)
	{
		UpdateChecker.apiKey = apiKey;
	}

	/**
	 * @return The title of the latest version.
	 */
	public String getLatestTitle()
	{
		return latestTitle;
	}

	/**
	 * @return The link where you can download the latest version.
	 */
	public final String getLatestLink()
	{
		return latestLink;
	}

	/**
	 * @return The type of the update. Ex: Release
	 */
	public final String getLatestType()
	{
		return latestType;
	}

	/**
	 * @return The latest version available.
	 */
	public String getLatestVersion()
	{
		return latestVersion;
	}

	/**
	 * @return The game version this update is designed for.
	 */
	public final String getLatestGameVersion()
	{
		return latestGameVersion;
	}

	/**
	 * @return True, if an update is available.
	 */
	public final boolean hasUpdate()
	{
		return hasUpdate;
	}

	/**
	 * @return True, if it has already checked for updates once, False otherwise.
	 */
	public final boolean wasQueried()
	{
		return queried;
	}
}
