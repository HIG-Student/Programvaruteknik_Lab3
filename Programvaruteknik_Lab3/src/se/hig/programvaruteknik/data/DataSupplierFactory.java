package se.hig.programvaruteknik.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Produces {@link Supplier Supplier&lt;String&gt;} for data sources
 */
public class DataSupplierFactory
{
    /**
     * Creates a fetcher that fetches data from the specified URL
     * <br>
     * <br>
     * The resulting supplier can throw RuntimeException if error occurs
     * 
     * @param url
     *            The URL to read data from
     * @param cache
     *            Indicates if the data should be cached
     * @return The {@link Supplier Supplier&lt;String&gt;}
     */
    public static Supplier<String> createURLFetcher(String url, boolean cache)
    {
	if (cache)
	    return createURLFetcher(url);
	else
	    return () ->
	    {
		try
		{
		    URL websiteURL = new URL(url);

		    try (BufferedReader br = new BufferedReader(new InputStreamReader(websiteURL.openStream())))
		    {
			return br.lines().collect(Collectors.joining("\n\r"));
		    }
		    catch (IOException ex)
		    {
			throw new RuntimeException(ex);
		    }
		}
		catch (MalformedURLException ex)
		{
		    throw new RuntimeException(ex);
		}
	    };
    };

    /**
     * Creates a fetcher that fetches data from the specified path
     * <br>
     * <br>
     * The resulting supplier can throw RuntimeException if error occurs
     * 
     * @param path
     *            The path to read data from
     * @return The {@link Supplier Supplier&lt;String&gt;}
     */
    public static Supplier<String> createFileFetcher(String path)
    {
	return () ->
	{
	    try
	    {
		return Files.lines(new File(path).toPath()).collect(Collectors.joining("\n\r"));
	    }
	    catch (IOException ex)
	    {
		throw new RuntimeException(ex);
	    }
	};
    };

    /**
     * Creates a fetcher that fetches data from the specified URL and cache the
     * result for future use
     * <br>
     * <br>
     * The resulting supplier can throw RuntimeException if error occurs
     * 
     * @param url
     *            The URL to read data from
     * @return The {@link Supplier Supplier&lt;String&gt;}
     * @throws IOException
     *             If errors occurs
     */
    public static Supplier<String> createURLFetcher(String url)
    {
	return createURLFetcher(url, false);
    };
}
