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

	public BaseSimpleFormActionHandler(Class<T> classType) {
		this.classType = classType;
		path = StringUtil.flattenCamelCase(classType.getSimpleName(), "_");
	}

	protected BaseSimpleFormActionHandler(Class<T> classType, String path) {
		this.classType = classType;
		this.path = path;
	}

	protected SimpleBeanForm newSimpleForm(HttpServletRequest request) {
		SimpleBeanForm simpleForm = new SimpleBeanForm(classType).setFormInRequest(request);
		return simpleForm;
	}

	
	protected SimpleFormRequest<SimpleBeanForm> getSimpleFormRequest(HttpServletRequest request) {
		SimpleBeanForm simpleForm = newSimpleForm(request);
		SimpleFormRequest<SimpleBeanForm> simpleFormRequest = new SimpleFormRequest<SimpleBeanForm>(request, simpleForm, path);
		return simpleFormRequest;
	}

	public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
		SimpleFormRequest<SimpleBeanForm> simpleFormRequest = getSimpleFormRequest(request);

		if (simpleFormRequest.isShow()) {
			return show(simpleFormRequest);
		} else if (simpleFormRequest.isNew()) {
			return create(simpleFormRequest);
		} else if (simpleFormRequest.isEdit()) {
			return edit(simpleFormRequest);
		} else if (simpleFormRequest.isDelete()) {
			return delete(simpleFormRequest);
		} else {
			return index(simpleFormRequest);
		}
	}

	public String index(HttpServletRequest request, HttpServletResponse response) {
		return index(getSimpleFormRequest(request));
	}

	public String index(SimpleFormRequest<SimpleBeanForm> simpleFormRequest) {
		List<T> objects;
		try {
			objects = findAll(classType);
			simpleFormRequest.setAttribute(path + "s", objects);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return getIndexView();
	}

	public String show(HttpServletRequest request, HttpServletResponse response) {
		return show(getSimpleFormRequest(request));
	}

	protected String show(SimpleFormRequest simpleFormRequest) {
		try {
			T object = find(classType, simpleFormRequest.getId());
			simpleFormRequest.setAttribute(path, object);
			return getShowView();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return index(simpleFormRequest);
		}
	}

	public String create(HttpServletRequest request, HttpServletResponse response) {
		return create(getSimpleFormRequest(request));
	}

	public String create(SimpleFormRequest<SimpleBeanForm> simpleFormRequest) {
		try {
			if (simpleFormRequest.isSubmitted()) {
				T object = create(classType, simpleFormRequest.getParameterMap());
				simpleFormRequest.getSimpleForm()
								.bindTo(object)
								.identifyWith(idField);
				simpleFormRequest.setAttribute(path, object);
				return getShowView();
			} else {
				return getCreateView();
			}
		} catch (ValidationException e) {
			logger.warn(e.getMessage());
			simpleFormRequest.getSimpleForm().setErrorFieldMap(e.getErrorFields());
			return getCreateView();
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return getCreateView();
		}
	}

	public String edit(HttpServletRequest request, HttpServletResponse response) {
		return edit(getSimpleFormRequest(request));
	}

	protected String edit(SimpleFormRequest<SimpleBeanForm> simpleFormRequest) {
		try {
			T object = find(classType, simpleFormRequest.getId());
			logger.info("Binding object " + object + " to form");
			simpleFormRequest.getSimpleForm()
							.bindTo(object)
							.identifyWith(idField);
			if (simpleFormRequest.isSubmitted()) {
				logger.info("Updating object");
				update(object, simpleFormRequest.getParameterMap());
			}
		} catch (ValidationException e) {
			logger.warn(e.getMessage());
			simpleFormRequest.getSimpleForm().setErrorFieldMap(e.getErrorFields());
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return getEditView();
	}

	public String delete(HttpServletRequest request, HttpServletResponse response) {
		return delete(getSimpleFormRequest(request));
	}

	public String delete(SimpleFormRequest<SimpleBeanForm> simpleFormRequest) {
		try {
			T object = find(classType, simpleFormRequest.getId());
			if (simpleFormRequest.confirmDelete()) {
				remove(object);
			} else {
				simpleFormRequest.getSimpleForm()
								.bindTo(object)
								.identifyWith(idField);
				return getConfirmView();
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return index(simpleFormRequest);
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
