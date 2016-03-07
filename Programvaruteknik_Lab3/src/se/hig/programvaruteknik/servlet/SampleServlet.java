package se.hig.programvaruteknik.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.hig.programvaruteknik.data.FootballAndWeatherCombiner;
import se.hig.programvaruteknik.data.SMHILocation;
import se.hig.programvaruteknik.model.MatchedDataPair;

/**
 * Servlet implementation class SampleServlet
 */
@WebServlet("/SampleServlet")
public class SampleServlet extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public SampleServlet()
    {
	// TODO Auto-generated constructor stub
    }

    private List<MatchedDataPair> createData()
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
	return combiner.build();
    }

    private String toJSON(List<MatchedDataPair> data)
    {
	StringBuilder builder = new StringBuilder();
	builder.append("{\n");
	builder.append("\t\"description\":\"Goals made at temperatures\",\n");
	builder.append("\t\"unit_a\":\"Goals\",\n");
	builder.append("\t\"unit_b\":\"Temperature\",\n");
	builder.append("\t\"data\":\n");
	builder.append("\t{\n");
	for (MatchedDataPair pair : data)
	{
	    builder.append("\t\t{");
	    builder.append("\t\t\t\"x\": ");
	    builder.append(pair.getXValue());
	    builder.append(",\n");
	    builder.append("\t\t\t\"y\": ");
	    builder.append(pair.getYValue());
	    builder.append("\n}\n");
	}
	builder.append("\t}\n");
	builder.append("}");
	return builder.toString();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	// request.getParameter(arg0)
	response.setContentType("application/json;charset=UTF-8");
	response.getWriter().append(toJSON(createData()));
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	// TODO Auto-generated method stub
	doGet(request, response);
    }

}
