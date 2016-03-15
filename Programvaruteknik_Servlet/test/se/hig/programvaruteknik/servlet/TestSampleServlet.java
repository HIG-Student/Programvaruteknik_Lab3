package se.hig.programvaruteknik.servlet;

import com.owlike.genson.Genson;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 * @author Viktor Hanstorp
 * @author thomas
 */

@SuppressWarnings("javadoc")
public class TestSampleServlet
{
    @SuppressWarnings("serial")
    static final Map<Object, Object> example_map_a = Collections.unmodifiableMap(new TreeMap<Object, Object>()
    {
	{
	    put("A", "B");
	    put("C", "D");
	    put("E", new TreeMap<Object, Object>()
	    {
		{
		    put("F", "G");
		    put("H", 56);
		}
	    });
	}
    });

    @SuppressWarnings("serial")
    static final Map<Object, Object> example_map_b = Collections.unmodifiableMap(new TreeMap<Object, Object>()
    {
	{
	    put("A2", "B2");
	    put("E2", new TreeMap<Object, Object>()
	    {
		{
		    put("F2", "G2");
		    put("H2", 562);
		}
	    });
	}
    });

    private SampleServlet instance;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter resultingWriter = new StringWriter();

    @Before
    public void setupTest() throws Exception
    {
	instance = new SampleServlet((pretty) ->
	{
	    return new Genson().serialize(pretty ? example_map_a : example_map_b);
	});
	request = mock(HttpServletRequest.class);
	response = mock(HttpServletResponse.class);
	when(response.getWriter()).thenReturn(new PrintWriter(resultingWriter));
    }

    private void testParameter(String pretty, Map<Object, Object> map) throws Exception
    {
	when(request.getParameter("pretty")).thenReturn(pretty);
	instance.doGet(request, response);
	assertEquals(new Genson().serialize(map), resultingWriter.toString());
    }

    @Test
    public void testPretty() throws Exception
    {
	testParameter("true", example_map_a);
    }

    @Test
    public void testNotPretty() throws Exception
    {
	testParameter("false", example_map_b);
    }

    @Test
    public void testMissingPretty() throws Exception
    {
	testParameter(null, example_map_b);
    }
}
