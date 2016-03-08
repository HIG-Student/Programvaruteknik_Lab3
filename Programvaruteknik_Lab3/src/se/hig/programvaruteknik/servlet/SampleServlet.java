package se.hig.programvaruteknik.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.hig.programvaruteknik.data.FootballAndWeatherStatisticsSupplier;

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

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	response.setContentType("application/json;charset=UTF-8");
	response.getWriter().append(
		new FootballAndWeatherStatisticsSupplier("true".equalsIgnoreCase(request.getParameter("pretty")))
			.getString());
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
