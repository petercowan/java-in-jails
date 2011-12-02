package org.jails.demo;

import org.jails.form.SimpleForm;
import org.jails.form.controller.SimpleFormRouter;
import org.jails.validation.SimpleValidator;
import org.jails.validation.ValidationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleFormServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SimpleValidator validator = new SimpleValidator();

		SimpleForm<ModelBean> simpleForm = SimpleForm.validateAs(ModelBean.class);
		SimpleFormRouter simpleFormRequest = new SimpleFormRouter("model_bean");

		if (simpleFormRequest.isSubmitted(request)) {
			try {
				ModelBean bean = validator.validate(ModelBean.class, request.getParameterMap());
				//do something with valid bean

				request.setAttribute("model_bean", bean);
				//now display bean
			} catch (ValidationException e) {
				simpleForm.setErrors(e.getErrorFields());
				//back to form page
			} catch (Exception e) {
				//handle error
			}
		} else {
			simpleForm.inRequest(request);
			//forward to formPage
		}
	}
}
