package de.uni_koeln.spinfo.maalr.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DelegationController {

	private final Logger logger = LoggerFactory.getLogger(DelegationController.class);

	private static final String DEFAULT_CONTEXT_PATH = "/rumantschgrischun";
	private static final String SURSILVAN_CONTEXT_PATH = "/sursilvan";
	private static final String PUTER_CONTEXT_PATH = "/puter";
	private static final String VALLADER_CONTEXT_PATH = "/vallader";
	private static final String SURMIRAN_CONTEXT_PATH = "/surmiran";
	private static final String SUTSILVAN_CONTEXT_PATH = "/sutsilvan";
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void redirectRumantsch(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("Redirecting to {}", request.getContextPath() + DEFAULT_CONTEXT_PATH);
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + DEFAULT_CONTEXT_PATH));
	}

	@RequestMapping(value = "/sursilvan", method = RequestMethod.GET)
	public void redirectSursilvan(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.info("Redirecting to {}", request.getContextPath() + SURSILVAN_CONTEXT_PATH);
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + SURSILVAN_CONTEXT_PATH));
	}

	@RequestMapping(value = "/puter", method = RequestMethod.GET)
	public void redirectPuter(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.info("Redirecting to {}", request.getContextPath() + PUTER_CONTEXT_PATH);
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + PUTER_CONTEXT_PATH));
	}
	
	@RequestMapping(value = "/vallader", method = RequestMethod.GET)
	public void redirectVallader(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.info("Redirecting to {}", request.getContextPath() + VALLADER_CONTEXT_PATH);
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + VALLADER_CONTEXT_PATH));
	}
	
	@RequestMapping(value = "/surmiran", method = RequestMethod.GET)
	public void redirectSurmiran(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.info("Redirecting to {}", request.getContextPath() + SURMIRAN_CONTEXT_PATH);
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + SURMIRAN_CONTEXT_PATH));
	}
	
	@RequestMapping(value = "/sutsilvan", method = RequestMethod.GET)
	public void redirectSutsilvan(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		logger.info("Redirecting to {}", request.getContextPath() + SUTSILVAN_CONTEXT_PATH);
		response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + SUTSILVAN_CONTEXT_PATH));
	}
	
}
