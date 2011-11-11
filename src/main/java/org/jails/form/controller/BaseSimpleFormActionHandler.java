package org.jails.form.controller;

import org.jails.form.SimpleForm;
import org.jails.util.StringUtil;
import org.jails.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
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
				if (simpleFormRequest.idCount(request) > 1) {
					List<T> objects = createAll(classType, simpleFormRequest.getIds(request), request.getParameterMap());
					request.setAttribute(path + "s", objects);
					return getShowAllView();
				} else {
					T object = create(classType, simpleFormRequest.getId(request), request.getParameterMap());
					request.setAttribute(path, object);
					return getShowView();
				}
			} catch (ValidationException e) {
				logger.warn(e.getMessage());
				simpleForm = SimpleForm.validateAs(classType)
									.identifyBy(idField)
									.inRequest(request);
				for (Map<String, List<String>> errors : e.getErrorFields().values())
					simpleForm.addErrorFieldsMap(errors);
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

		List<T> objects;
		try {
			objects = findAll(classType, simpleFormRequest.getIds(request));
		} catch (Exception e) {
			logger.warn(e.getMessage());
			if (simpleFormRequest.idCount(request) > 1) {
				return getEditAllView();
			} else {
				return getEditView();
			}
		}
		logger.info("Binding objects  to form");


		simpleForm = SimpleForm.bindTo(listToArray(objects))
				.identifyBy(idField)
				.inRequest(request);
		if (simpleFormRequest.isSubmitted(request)) {
			try {
				if (objects.size() > 1) {
					editAll(objects, request.getParameterMap());
				} else {
					edit(objects.get(0), request.getParameterMap());
				}
			} catch (ValidationException e) {
				for (Map<String, List<String>> errors : e.getErrorFields().values())
					simpleForm.addErrorFieldsMap(errors);
			}
		}
		return getEditView();
	}

	public String delete(HttpServletRequest request, HttpServletResponse response) {
		List<T> objects = null;
		try {
			objects = findAll(classType, simpleFormRequest.getIds(request));
			if (simpleFormRequest.confirmDelete(request)) {
				if (objects.size() > 1) {
					deleteAll(objects);
				} else {
					delete(objects.get(0));
				}
				return index(request, response);
			} else {
				SimpleForm.bindTo(listToArray(objects))
						.identifyBy(idField)
						.inRequest(request);
				return getConfirmView();
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			setPageError(e.getMessage(), request);
		} finally {
			if (objects != null && objects.size() > 0) {
				SimpleForm.bindTo(listToArray(objects))
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

	public String getShowAllView() {
		return "/view/" + path + "/showAll.jsp";
	}

	public String getCreateView() {
		return "/view/" + path + "/create.jsp";
	}

	public String getEditView() {
		return "/view/" + path + "/edit.jsp";
	}

	public String getEditAllView() {
		return "/view/" + path + "/editAll.jsp";
	}

	public String getConfirmView() {
		return "/view/" + path + "/confirm.jsp";
	}

	public String getDeleteView() {
		return "/view/" + path + "/delete.jsp";
	}

	public String getDeleteAllView() {
		return "/view/" + path + "/deleteAll.jsp";
	}

	protected T[] listToArray(List<T> objects) {
		int size = (objects == null) ? 0 : objects.size();
		return (T[]) Array.newInstance(classType, size);
	}

	protected void setPageError(String errorMessage, HttpServletRequest request) {
		request.setAttribute("error_message", errorMessage);
	}

	protected abstract List<T> findAll(Class<T> classType);

	protected abstract T find(Class<T> classType, Integer id) throws Exception;

	protected abstract List<T> findAll(Class<T> classType, Integer[] ids) throws Exception;

	protected abstract T create(Class<T> classType, Integer id, Map<String, String[]> parameterMap) throws ValidationException;

	protected abstract List<T> createAll(Class<T> classType, Integer[] ids, Map<String, String[]> parameterMap) throws ValidationException;

	protected abstract void edit(T object, Map<String, String[]> parameterMap) throws ValidationException;

	protected abstract void editAll(List<T> objects, Map<String, String[]> parameterMap) throws ValidationException;

	protected abstract void delete(T object) throws Exception;

	protected abstract void deleteAll(List<T> objects) throws Exception;

}
