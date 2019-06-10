package de.uni_koeln.spinfo.maalr.webapp.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GwtCacheFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {

		if (((HttpServletRequest) request).getRequestURI().contains(".nocache.")) {
			Date now = new Date();
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setDateHeader("Date", now.getTime());
			httpResponse.setHeader("Pragma", "no-cache");
			httpResponse.setHeader("Cache-control", "no-cache, no-store, must-revalidate, max-age=600");
		}

		filterChain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
