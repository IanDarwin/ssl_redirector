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

		// Get the main part of the URL
		String uri = HttpUtils.getRequestURL(req).toString();
		
		if (!uri.startsWith("http:")) {
			throw new IllegalArgumentException("Invalid URI " + uri);
		}
		
		// Poor lame script kiddie or bot attacks us;
		// no point wasting CPU picoseconds encrypting it, just trash it.
		if (uri.endsWith(".php")) {
			resp.setStatus(404);
			System.err.println("**PHP ATTACK** FROM " + req.getRemoteAddr());
			// It is tempting to automate the blocking of this site
			// via PF, but that can be abused for DOS attacks. Just log it.
			resp.getWriter().println("<h1>404. Not found.</h1>");
			return;
		}
		
		final String queryString = req.getQueryString();
		if (queryString != null) {
			uri += '?' + queryString;
		}
		System.out.println("SslRedirector: " + req.getRemoteAddr() + " -> " + uri);
		final String redirect = uri.replaceAll("^http:", "https:");
		resp.sendRedirect(redirect);
	}
}
