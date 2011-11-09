package org.jails.form;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SimpleFormActionHandler<T> {

	public String handleRequest(HttpServletRequest request, HttpServletResponse response);

	public String index(HttpServletRequest request, HttpServletResponse response);

	public String show(HttpServletRequest request, HttpServletResponse response);

	public String create(HttpServletRequest request, HttpServletResponse response);

	public String edit(HttpServletRequest request, HttpServletResponse response);

	public String delete(HttpServletRequest request, HttpServletResponse response);
}
