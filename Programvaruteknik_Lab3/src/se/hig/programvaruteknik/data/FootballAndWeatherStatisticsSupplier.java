package se.hig.programvaruteknik.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.Map.Entry;

import se.hig.programvaruteknik.model.MatchedDataPair;

/**
 * Gets the statistics about weather and goals as a JSON
 */
public class FootballAndWeatherStatisticsSupplier
{
    private boolean pretty = false;
    private LinkedList<Consumer<StringBuilder>> lines = new LinkedList<>();
    private String result;

    private void appendLine(@SuppressWarnings("unchecked") Consumer<StringBuilder>... actions)
    {
	if (actions == null) return;
	for (Consumer<StringBuilder> action : actions)
	{
	    lines.add(action);
	}
	if (pretty) lines.add(N);
    }

    private Consumer<StringBuilder> T = (b) ->
    {
	if (pretty) b.append("\t");
    };

    private Consumer<StringBuilder> N = (b) ->
    {
	if (pretty) b.append("\n");
    };

    private Consumer<StringBuilder> V(Object obj)
    {
	return (b) -> b.append(obj);
    }

    private Consumer<StringBuilder> Q(String str)
    {
	return (b) ->
	{
	    b.append("\"");
	    b.append(str);
	    b.append("\"");
	};
    }

    private void removeLast()
    {
	lines.removeLast();
    }

    /**
     * Gets the statistics about weather and goals as a JSON
     * 
     * @param pretty
     *            If it should be prettified
     */
    public FootballAndWeatherStatisticsSupplier(boolean pretty)
    {
	this.pretty = pretty;
	generateJSON(createData());
    }

    private Map<String, List<MatchedDataPair>> createData()
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

    @SuppressWarnings("unchecked")
    private void generateJSON(Map<String, List<MatchedDataPair>> data)
    {
	appendLine(V("{"));
	appendLine(T, Q("name"), V(":"), Q("Goals made at temperatures"), V(","));
	appendLine(T, Q("a_name"), V(":"), Q("Goals"), V(","));
	appendLine(T, Q("b_name"), V(":"), Q("Temperature"), V(","));
	appendLine(T, Q("source"), V(":"), Q("http://www.everysport.com/"), V(","));
	appendLine(T, Q("data"), V(":"));
	appendLine(T, V("{"));
	for (Entry<String, List<MatchedDataPair>> entry : data.entrySet())
	{
	    appendLine(T, T, Q(entry.getKey()), V(":"));
	    appendLine(T, T, V("["));
	    for (MatchedDataPair pair : entry.getValue())
	    {
		appendLine(T, T, T, V("{"));
		appendLine(T, T, T, T, Q("a"), V(":"), V(pair.getXValue()), V(","));
		appendLine(T, T, T, T, Q("b"), V(":"), V(pair.getYValue()));
		appendLine(T, T, T, V("}"), V(","));
	    }
	    if (entry.getValue().size() > 0)
	    {
		if (pretty) removeLast();
		removeLast();
		appendLine(V(""));
	    }
	    appendLine(T, T, V("]"), V(","));
	}
	if (data.size() > 0)
	{
	    if (pretty) removeLast();
	    removeLast();
	    appendLine(V(""));
	}
	appendLine(T, V("}"));
	appendLine(V("}"));

	StringBuilder builder = new StringBuilder();
	for (Consumer<StringBuilder> line : lines)
	{
	    line.accept(builder);
	}
	result = builder.toString();
    }

    /**
     * Returns the JSON
     * 
     * @return The JSON
     */
    public String getString()
    {
	return result;
    }
}
