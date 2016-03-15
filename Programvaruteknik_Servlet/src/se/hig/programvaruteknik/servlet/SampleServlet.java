package se.hig.programvaruteknik.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.hig.programvaruteknik.data.FootballAndWeatherCombiner;
import se.hig.programvaruteknik.data.FootballAndWeatherStatisticsSupplier;
import se.hig.programvaruteknik.data.SMHILocation;

/**
 * Servlet implementation class SampleServlet
 */
@WebServlet("/SampleServlet")
public class SampleServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    private Function<Boolean, String> JSONGenerator;

    /**
     * Creates a servlet that can serve a JSON
     */
    public SampleServlet()
    {
	this((pretty) ->
	{
	    Map<String, SMHILocation> locationMapping = new TreeMap<>();
	    locationMapping.put("63057", SMHILocation.KALMAR_FLYGPLATS);
	    locationMapping.put("61401", SMHILocation.HALMSTAD);
	    locationMapping.put("61383", SMHILocation.MALMÖ_A);
	    locationMapping.put("61382", SMHILocation.KARLSHAMN);
	    locationMapping.put("61378", SMHILocation.BORÅS);
	    locationMapping.put("60907", SMHILocation.NORRKÖPING_SMHI);
	    locationMapping.put("60662", SMHILocation.HELSINGBORG_A);
	    locationMapping.put("60659", SMHILocation.ULLARED_A);
	    locationMapping.put("60649", SMHILocation.ÖREBRO_A);
	    locationMapping.put("60610", SMHILocation.GÄVLE_A);
	    locationMapping.put("60029", SMHILocation.NORRKÖPING_SMHI);
	    locationMapping.put("110637", SMHILocation.GÖTEBORG_A);
	    locationMapping.put("61381", SMHILocation.GÖTEBORG_A);
	    locationMapping.put("32736", SMHILocation.GÖTEBORG_A);
	    locationMapping.put("110645", SMHILocation.STOCKHOLM_A);
	    locationMapping.put("110511", SMHILocation.STOCKHOLM_A);
	    locationMapping.put("5184", SMHILocation.STOCKHOLM_A);
	    locationMapping.put("110295", SMHILocation.STOCKHOLM_A);
	    locationMapping.put("18", SMHILocation.STOCKHOLM_A);
	    locationMapping.put("13", SMHILocation.STOCKHOLM_A);

	    FootballAndWeatherCombiner combiner = new FootballAndWeatherCombiner();
	    combiner.setArenaToLocationMapper(locationMapping);

	    return new FootballAndWeatherStatisticsSupplier(combiner.build(), pretty).getString();
	});
    }

    /**
     * Creates a servlet that serves the JSON that the generator generates
     * 
     * @param JSONGenerator
     *            The generator that generates the JSON, the boolean input
     *            defines if it should have a pretty format or not
     */
    public SampleServlet(Function<Boolean, String> JSONGenerator)
    {
	this.JSONGenerator = JSONGenerator;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	boolean pretty = "true".equalsIgnoreCase(request.getParameter("pretty"));

	response.setContentType("application/json;charset=UTF-8");
	response.getWriter().append(JSONGenerator.apply(pretty));
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	doGet(request, response);
    }
}
