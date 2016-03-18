package se.hig.programvaruteknik.data;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.Map.Entry;

import se.hig.programvaruteknik.model.DataCollection;
import se.hig.programvaruteknik.model.MatchedDataPair;

/**
 * Gets the statistics about weather and goals as a JSON
 * 
 * @author Viktor Hanstorp (ndi14vhp@student.hig.se)
 */
public class DataCollectionToJSONConverter
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
	    if (str == null)
		b.append("null");
	    else
	    {
		b.append("\"");
		b.append(str);
		b.append("\"");
	    }
	};
    }

    private void removeLast()
    {
	lines.removeLast();
    }

    /**
     * Supplies JSON from a map
     * 
     * @param source
     *            The data collection to transform into JSON
     * @param pretty
     *            If it should have a pretty format
     */
    public DataCollectionToJSONConverter(DataCollection source, boolean pretty)
    {
	this.pretty = pretty;
	generateJSON(source);
    }

    @SuppressWarnings("unchecked")
    private void generateJSON(DataCollection source)
    {
	appendLine(V("{"));
	appendLine(T, Q("data"), V(":"));
	appendLine(T, V("{"));
	appendLine(T, T, Q("name"), V(":"), Q(source.getTitle()), V(","));
	appendLine(T, T, Q("a_name"), V(":"), Q(source.getXUnit()), V(","));
	appendLine(T, T, Q("a_source_name"), V(":"), Q(source.getXSourceName()), V(","));
	appendLine(T, T, Q("a_source_link"), V(":"), Q(source.getXSourceLink()), V(","));
	appendLine(T, T, Q("b_name"), V(":"), Q(source.getYUnit()), V(","));
	appendLine(T, T, Q("b_source_name"), V(":"), Q(source.getYSourceName()), V(","));
	appendLine(T, T, Q("b_source_link"), V(":"), Q(source.getYSourceLink()), V(","));
	appendLine(T, T, Q("data"), V(":"));
	appendLine(T, T, V("{"));
	MatchedDataPair pair = null;
	for (Entry<String, MatchedDataPair> entry : source.getData().entrySet())
	{
	    pair = entry.getValue();
	    appendLine(T, T, T, Q(entry.getKey()), V(":"));
	    appendLine(T, T, T, V("{"));
	    appendLine(T, T, T, T, Q("a"), V(":"), V(pair.getXValue()), V(","));
	    appendLine(T, T, T, T, Q("b"), V(":"), V(pair.getYValue()));
	    appendLine(T, T, T, V("}"), V(","));
	}
	if (pair != null)
	{
	    if (pretty) removeLast();
	    removeLast();
	    appendLine(V(""));
	}
	appendLine(T, T, V("}"));
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
