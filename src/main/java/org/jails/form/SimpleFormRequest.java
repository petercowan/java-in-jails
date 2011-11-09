package org.jails.form;

import org.jails.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleFormRequest<T extends SimpleForm> extends HttpServletRequestWrapper {
	private static Logger logger = LoggerFactory.getLogger(SimpleFormRequest.class);

	public static String ACTION_DELETE = "delete";
	public static String ACTION_NEW = "new";
	public static String ACTION_EDIT = "edit";
	public static String ACTION_SHOW = "show";

	public static String ACTION_SUBMIT = "action_submit";
	public static String SUBMIT_DELETE = "action_delete";
	public static String SUBMIT_NEW = "action_new";
	public static String SUBMIT_EDIT = "action_edit";
	public static String SUBMIT_SHOW = "action_show";
	public static String CONFIRM_DELETE = "confirm_delete";

	T simpleForm;
	private String path;

	public SimpleFormRequest(HttpServletRequest request, T simpleForm, String path) {
		super(request);
		this.simpleForm = simpleForm;
		this.path = path;
	}

	public T getSimpleForm() {
		return simpleForm;
	}

	@Override
	public String getMethod() {
		String method = getParameter(simpleForm.getMetaParameterName("method"));
		return (method != null) ? method : super.getMethod();
	}

	public boolean isShow() {
		boolean isShow = ((ACTION_SHOW.equals(getActionPath()) || StringUtil.isEmpty(getActionPath()))
				&& getMethod().equalsIgnoreCase("GET"))
				&& getId() != null;
		logger.info("isShow? " + isShow);
		return isShow;
	}

	public boolean isNew() {
		boolean isCreate = (ACTION_NEW.equals(getActionPath()) || getMethod().equalsIgnoreCase("PUT"));
		logger.info("isCreate? " + isCreate);
		return isCreate;
	}

	public boolean isEdit() {
		boolean isEdit = (ACTION_EDIT.equals(getActionPath()) || getMethod().equalsIgnoreCase("POST"));
		logger.info("isEdit? " + isEdit);
		return isEdit;
	}

	public boolean isDelete() {
		boolean isDelete = (ACTION_DELETE.equals(getActionPath()) || getMethod().equalsIgnoreCase("DELETE"));
		logger.info("isDelete? " + isDelete);
		return isDelete;
	}

	public boolean confirmDelete() {
		boolean isConfirmDelete = StringUtil.getBoolean(getParameter(simpleForm.getParameterName(CONFIRM_DELETE)));
		logger.info("isConfirmDelete? " + isConfirmDelete);
		return isConfirmDelete;
	}

	public boolean isSubmitted() {
		boolean isSubmitted = (isDelete() && (simpleForm.getMetaParameterName(SUBMIT_DELETE)).equals(getParameter(simpleForm.getMetaParameterName(ACTION_SUBMIT))))
				|| (isEdit() && (simpleForm.getMetaParameterName(SUBMIT_EDIT)).equals(getParameter(simpleForm.getMetaParameterName(ACTION_SUBMIT))))
				|| (isNew() && (simpleForm.getMetaParameterName(SUBMIT_NEW)).equals(getParameter(simpleForm.getMetaParameterName(ACTION_SUBMIT))));
		logger.info("isSubmitted? " + isSubmitted);
		return isSubmitted;
	}

	//requestPath = /path/[id],[id].../action
	public Integer[] getIds() {
		Integer[] ids = null;
		String requestPath = getRequestURI().replace("/" + path + "/", "");
		Pattern idPattern = Pattern.compile("^([0-9,]+)");
		Matcher idMatcher = idPattern.matcher(requestPath);
		if (idMatcher.find()) {
			logger.info("matched: " + idMatcher.group(0));
			String[] idStrings = idMatcher.group(0).split(",");
			ids = new Integer[idStrings.length];
			for (int i = 0; i < idStrings.length; i++) {
				try {
					logger.info("parsing " + idStrings[i] + "|");
					ids[i] = Integer.parseInt(idStrings[i]);
				} catch (Exception e) {
					logger.warn(e.toString());
				}
			}
		} else {
			logger.info("No match");
		}
		if (ids == null) ids = new Integer[0];
		return ids;
	}

	public Integer getId() {
		Integer[] ids = getIds();
		return (ids.length == 0) ? null : ids[0];
	}

	protected String getActionPath() {
		String actionPath = (getRequestURI().indexOf("/") >= 0)
				? getRequestURI().substring(getRequestURI().lastIndexOf("/") + 1)
				: null;
		logger.info("actionPath: " + actionPath);
		return actionPath;
	}
}
