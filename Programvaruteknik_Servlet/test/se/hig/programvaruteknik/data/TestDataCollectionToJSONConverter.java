package se.hig.programvaruteknik.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import com.owlike.genson.Genson;

import se.hig.programvaruteknik.data.DataCollectionToJSONConverter;
import se.hig.programvaruteknik.model.DataCollection;
import se.hig.programvaruteknik.model.MatchedDataPair;

@SuppressWarnings("javadoc")
public class TestDataCollectionToJSONConverter
{
    private static final String TITLE = "[The Title]";

    private static final String X_SOURCE_NAME = "[X Source Name]";
    private static final String X_SOURCE_LINK = "[X Source Link]";

    private static final String Y_SOURCE_NAME = "[Y Source Name]";
    private static final String Y_SOURCE_LINK = "[Y Source Link]";

    private static final String X_UNIT = "[X Unit]";
    private static final String Y_UNIT = "[Y Unit]";

    private DataCollection mockObject;

    @SuppressWarnings("serial")
    @Before
    public void setUp()
    {
	mockObject = mock(DataCollection.class);
	when(mockObject.getTitle()).thenReturn(TITLE);

	when(mockObject.getXSourceName()).thenReturn(X_SOURCE_NAME);
	when(mockObject.getXSourceLink()).thenReturn(X_SOURCE_LINK);

	when(mockObject.getYSourceName()).thenReturn(Y_SOURCE_NAME);
	when(mockObject.getYSourceLink()).thenReturn(Y_SOURCE_LINK);

	when(mockObject.getXUnit()).thenReturn(X_UNIT);
	when(mockObject.getYUnit()).thenReturn(Y_UNIT);

	when(mockObject.getData()).thenReturn(new TreeMap<String, MatchedDataPair>()
	{
	    {
		put("2016-03-11", new MatchedDataPair(10.10, 20.20));
		put("2016-03-12", new MatchedDataPair(12.12, 22.22));
	    }
	});
    }

    @SuppressWarnings("serial")
    private void testData(Map<String, Object> data)
    {
	assertNotNull(data);

	assertTrue(data.containsKey("data"));
	@SuppressWarnings("unchecked")
	Map<String, Object> content = (Map<String, Object>) data.get("data");
	assertNotNull(content);

	assertEquals(TITLE, content.get("name"));

	assertEquals(X_UNIT, content.get("a_name"));
	assertEquals(Y_UNIT, content.get("b_name"));

	assertEquals(X_SOURCE_NAME, content.get("a_source_name"));
	assertEquals(X_SOURCE_LINK, content.get("a_source_link"));

	assertEquals(Y_SOURCE_NAME, content.get("b_source_name"));
	assertEquals(Y_SOURCE_LINK, content.get("b_source_link"));

	@SuppressWarnings("unchecked")
	Map<String, Map<String, Double>> map = (Map<String, Map<String, Double>>) content.get("data");
	assertEquals(2, map.size());

	assertEquals(map.get("2016-03-11"), new TreeMap<String, Double>()
	{
	    {
		put("a", 10.10);
		put("b", 20.20);
	    }
	});

	assertEquals(map.get("2016-03-12"), new TreeMap<String, Double>()
	{
	    {
		put("a", 12.12);
		put("b", 22.22);
	    }
	});
    }

    /***
     * The idea is that Genson should somehow break if the JSON is incorrect
     */
    @Test
    public void testJSONFormat()
    {
	@SuppressWarnings("unchecked")
	Map<String, Object> pretty_data = new Genson()
		.deserialize(new DataCollectionToJSONConverter(mockObject, true).getString(), Map.class);

	@SuppressWarnings("unchecked")
	Map<String, Object> not_pretty_data = new Genson()
		.deserialize(new DataCollectionToJSONConverter(mockObject, false).getString(), Map.class);

	testData(pretty_data);
	testData(not_pretty_data);

	assertEquals(pretty_data, not_pretty_data);
    }
}
