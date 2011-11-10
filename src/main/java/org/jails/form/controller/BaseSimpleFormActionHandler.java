package org.jails.form.controller;

import org.jails.form.SimpleForm;
import org.jails.util.StringUtil;
import org.jails.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public abstract class BaseSimpleFormActionHandler<T> implements SimpleFormActionHandler {
	private static Logger logger = LoggerFactory.getLogger(BaseSimpleFormActionHandler.class);

	protected static final String idField = "id";

	protected Class<T> classType;
	protected String path;
	protected SimpleFormRouter simpleFormRequest;

	protected BaseSimpleFormActionHandler(Class<T> classType, String path) {
		this.classType = classType;
		this.path = path;
		simpleFormRequest = new SimpleFormRouter(path);
	}

	public BaseSimpleFormActionHandler(Class<T> classType) {
		this(classType, StringUtil.flattenCamelCase(classType.getSimpleName(), "_"));
	}

	public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
		if (simpleFormRequest.isShow(request)) {
			return show(request, response);
		} else if (simpleFormRequest.isNew(request)) {
			return create(request, response);
		} else if (simpleFormRequest.isEdit(request)) {
			return edit(request, response);
		} else if (simpleFormRequest.isDelete(request)) {
			return delete(request, response);
		} else {
			return index(request, response);
		}
	}

	public String index(HttpServletRequest request, HttpServletResponse response) {
		List<T> objects;
		try {
			objects = findAll(classType);
			request.setAttribute(path + "s", objects);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return getIndexView();
	}

	public String show(HttpServletRequest request, HttpServletResponse response) {
		try {
			T object = find(classType, simpleFormRequest.getId(request));
			request.setAttribute(path, object);
			return getShowView();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return index(request, response);
		}
	}

	public String create(HttpServletRequest request, HttpServletResponse response) {
		SimpleForm<T> simpleForm;
		if (simpleFormRequest.isSubmitted(request)) {
			try {
				T object = create(classType, request.getParameterMap());
				request.setAttribute(path, object);
				return getShowView();
			} catch (ValidationException e) {
				logger.warn(e.getMessage());
				SimpleForm.validateAs(classType)
						.identifyBy(idField)
						.inRequest(request)
						.setErrors(e.getErrorFields());
				return getCreateView();
			} catch (Exception e) {
				logger.warn(e.getMessage());
				return getCreateView();
			}
		} else {
			SimpleForm.validateAs(classType)
					.identifyBy(idField)
					.inRequest(request);
			return getCreateView();
		}
	}

	public String edit(HttpServletRequest request, HttpServletResponse response) {
		SimpleForm<T> simpleForm;

		T object;
		try {
			object = find(classType, simpleFormRequest.getId(request));
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return getEditView();
		}
		logger.info("Binding object " + object + " to form");

		simpleForm = SimpleForm.bindTo(object)
								.identifyBy(idField)
								.inRequest(request);
		if (simpleFormRequest.isSubmitted(request)) {
			try {
				update(object, request.getParameterMap());
			} catch (ValidationException e) {
				logger.warn(e.getMessage());
				simpleForm.setErrors(e.getErrorFields());
			} catch (Exception e) {
				logger.error(e.getMessage());
				setPageError(e.getMessage(), request);
			}

		}
		return getEditView();
	}

	public String delete(HttpServletRequest request, HttpServletResponse response) {
		T object = null;
		try {
			object = find(classType, simpleFormRequest.getId(request));
			if (simpleFormRequest.confirmDelete(request)) {
				remove(object);
				return index(request, response);
			} else {
				SimpleForm.bindTo(object)
						.identifyBy(idField)
						.inRequest(request);
				return getConfirmView();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			setPageError(e.getMessage(), request);
		} finally {
			if (object != null) {
				SimpleForm.bindTo(object)
						.identifyBy(idField)
						.inRequest(request);
				return getConfirmView();
			} else {
				return index(request, response);
			}
		}
	}

	public String getIndexView() {
		return "/view/" + path + "/index.jsp";
	}

	public String getShowView() {
		return "/view/" + path + "/show.jsp";
	}

	public String getCreateView() {
		return "/view/" + path + "/create.jsp";
	}

	public String getEditView() {
		return "/view/" + path + "/edit.jsp";
	}

	public String getConfirmView() {
		return "/view/" + path + "/confirm.jsp";
	}

	public String getDeleteView() {
		return "/view/" + path + "/delete.jsp";
	}

	protected void setPageError(String errorMessage, HttpServletRequest request) {
		request.setAttribute("error_message", errorMessage);
	}

	protected abstract List<T> findAll(Class<T> classType);

	protected abstract T find(Class<T> classType, Integer id) throws Exception;

	protected abstract T create(Class<T> classType, Map<String, String[]> parameterMap) throws Exception;

	protected abstract void update(T object, Map<String, String[]> parameterMap) throws Exception;

	protected abstract void remove(T object) throws Exception;


}
