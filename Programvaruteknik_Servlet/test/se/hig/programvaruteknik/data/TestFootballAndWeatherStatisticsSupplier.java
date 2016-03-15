package se.hig.programvaruteknik.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import com.owlike.genson.Genson;

import se.hig.programvaruteknik.data.FootballAndWeatherStatisticsSupplier;
import se.hig.programvaruteknik.model.MatchedDataPair;

@SuppressWarnings("javadoc")
public class TestFootballAndWeatherStatisticsSupplier
{
    private FootballAndWeatherCombiner mockObject;

    @SuppressWarnings("serial")
    @Before
    public void setUp()
    {
	mockObject = mock(FootballAndWeatherCombiner.class);
	when(mockObject.build()).thenReturn(new TreeMap<String, List<MatchedDataPair>>()
	{
	    {
		put("2016-03-11", new ArrayList<MatchedDataPair>()
		{
		    {
			add(new MatchedDataPair(10.10, 20.20));
		    }
		});
		put("2016-03-12", new ArrayList<MatchedDataPair>()
		{
		    {
			add(new MatchedDataPair(12.12, 22.22));
		    }
		});
	    }
	});
    }

    private void testData(Map<String, Object> data)
    {
	assertNotNull(data);

	assertTrue(data.containsKey("data"));
	@SuppressWarnings("unchecked")
	Map<String, Object> content = (Map<String, Object>) data.get("data");
	assertNotNull(content);

	assertTrue(content.containsKey("name"));
	assertEquals("Goals made at temperatures", content.get("name"));

	assertTrue(content.containsKey("a_name"));
	assertEquals("Goals", content.get("a_name"));

	assertTrue(content.containsKey("b_name"));
	assertEquals("Temperature", content.get("b_name"));

	assertTrue(content.containsKey("a_source"));
	assertEquals("http://www.everysport.com/", content.get("a_source"));

	assertTrue(content.containsKey("b_source"));
	assertEquals("http://www.smhi.se/", content.get("b_source"));

	assertTrue(content.containsKey("data"));
	@SuppressWarnings("unchecked")
	List<Map<String, List<Map<String, Double>>>> list = (List<Map<String, List<Map<String, Double>>>>) content
		.get("data");
	assertEquals(2, list.size());

	assertEquals("2016-03-11", list.get(0).get("date"));
	assertEquals(1, list.get(0).get("pairs").size());
	assertEquals(new Double(10.10), list.get(0).get("pairs").get(0).get("a"));
	assertEquals(new Double(20.20), list.get(0).get("pairs").get(0).get("b"));

	assertEquals("2016-03-12", list.get(1).get("date"));
	assertEquals(1, list.get(1).get("pairs").size());
	assertEquals(new Double(12.12), list.get(1).get("pairs").get(0).get("a"));
	assertEquals(new Double(22.22), list.get(1).get("pairs").get(0).get("b"));
    }

    /***
     * The idea is that Genson should somehow break if the JSON is incorrect
     */
    @Test
    public void testJSONFormat()
    {
	@SuppressWarnings("unchecked")
	Map<String, Object> pretty_data = new Genson()
		.deserialize(new FootballAndWeatherStatisticsSupplier(mockObject.build(), true).getString(), Map.class);

	@SuppressWarnings("unchecked")
	Map<String, Object> not_pretty_data = new Genson().deserialize(
		new FootballAndWeatherStatisticsSupplier(mockObject.build(), false).getString(),
		Map.class);

	testData(pretty_data);
	testData(not_pretty_data);

	assertEquals(pretty_data, not_pretty_data);
    }
}
