package org.jails.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jails.util.StringUtil;
import org.jails.validation.ValidationException;

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

	protected SimpleForm newSimpleForm(HttpServletRequest request) {
		SimpleForm simpleForm = new SimpleForm(classType).inRequest(request);
		return simpleForm;
	}

	
	public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
		SimpleForm simpleForm = newSimpleForm(request);
		if (simpleFormRequest.isShow(request, simpleForm)) {
			return show(request, response, simpleForm);
		} else if (simpleFormRequest.isNew(request, simpleForm)) {
			return create(request, response, simpleForm);
		} else if (simpleFormRequest.isEdit(request, simpleForm)) {
			return edit(request, response, simpleForm);
		} else if (simpleFormRequest.isDelete(request, simpleForm)) {
			return delete(request, response, simpleForm);
		} else {
			return index(request, response, simpleForm);
		}
	}

	public String index(HttpServletRequest request, HttpServletResponse response) {
		SimpleForm simpleForm = newSimpleForm(request);
		return index(request, response, simpleForm);
	}

	public String index(HttpServletRequest request, HttpServletResponse response, SimpleForm simpleForm) {
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
		SimpleForm simpleForm = newSimpleForm(request);
		return show(request, response, simpleForm);
	}

	protected String show(HttpServletRequest request, HttpServletResponse response, SimpleForm simpleForm) {
		try {
			T object = find(classType, simpleFormRequest.getId(request));
			request.setAttribute(path, object);
			return getShowView();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return index(request, response, simpleForm);
		}
	}

	public String create(HttpServletRequest request, HttpServletResponse response) {
		SimpleForm simpleForm = newSimpleForm(request);
		return create(request, response, simpleForm);
	}

	public String create(HttpServletRequest request, HttpServletResponse response, SimpleForm simpleForm) {
		try {
			if (simpleFormRequest.isSubmitted(request, simpleForm)) {
				T object = create(classType, request.getParameterMap());
				simpleForm.bindTo(object)
						  .identifyBy(idField);
				request.setAttribute(path, object);
				return getShowView();
			} else {
				return getCreateView();
			}
		} catch (ValidationException e) {
			logger.warn(e.getMessage());
			simpleForm.setErrors(e.getErrorFields());
			return getCreateView();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return getCreateView();
		}
	}

	public String edit(HttpServletRequest request, HttpServletResponse response) {
		SimpleForm simpleForm = newSimpleForm(request);
		return edit(request, response, simpleForm);
	}

	protected String edit(HttpServletRequest request, HttpServletResponse response, SimpleForm simpleForm) {
		try {
			T object = find(classType, simpleFormRequest.getId(request));
			logger.info("Binding object " + object + " to form");
			simpleForm.bindTo(object)
					  .identifyBy(idField);
			if (simpleFormRequest.isSubmitted(request, simpleForm)) {
				logger.info("Updating object");
				update(object, request.getParameterMap());
			}
		} catch (ValidationException e) {
			logger.warn(e.getMessage());
			simpleForm.setErrors(e.getErrorFields());
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return getEditView();
	}

	public String delete(HttpServletRequest request, HttpServletResponse response) {
		SimpleForm simpleForm = newSimpleForm(request);
		return delete(request, response, simpleForm);
	}

	protected String delete(HttpServletRequest request, HttpServletResponse response, SimpleForm simpleForm) {
		try {
			T object = find(classType, simpleFormRequest.getId(request));
			if (simpleFormRequest.confirmDelete(request, simpleForm)) {
				remove(object);
			} else {
				simpleForm.bindTo(object)
						  .identifyBy(idField);
				return getConfirmView();
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return index(request, response, simpleForm);
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
	
	protected abstract List<T> findAll(Class<T> classType);

	protected abstract T find(Class<T> classType, Integer id);

	protected abstract T create(Class<T> classType, Map<String, String[]> parameterMap) throws ValidationException;

	protected abstract void update(T object, Map<String, String[]> parameterMap) throws ValidationException;

	protected abstract void remove(T object);

	
}
