package main;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

@SuppressWarnings("deprecation")
@WebServlet(name = "SslRedirectorServlet",
	description = "Trivial SSL redirector servlet",
	urlPatterns = {"/*"})
public class SslRedirector extends HttpServlet {

	private static final long serialVersionUID = -6929090915091038163L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		final String queryString = req.getQueryString();
		String uri = HttpUtils.getRequestURL(req).toString() + 
				queryString != null ? '?' + queryString : "";
		System.out.println("SslRedirector.doGet(): " + req.getRemoteAddr() + "->" + uri);
		if (!uri.startsWith("http:")) {
			throw new IllegalArgumentException("Invalid URI " + uri);
		}
		final String redirect = uri.replaceAll("^http:", "https:");
		resp.sendRedirect(redirect);
	}
}
