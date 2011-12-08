package demo;

import org.jails.demo.AccountForm;
import org.jails.form.SimpleForm;
import org.jails.demo.controller.SimpleFormRouter;
import org.jails.validation.SimpleValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SimpleFormServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SimpleValidator validator = new SimpleValidator();

		SimpleForm<AccountForm> simpleForm = SimpleForm.validateAs(AccountForm.class);
		SimpleFormRouter simpleFormRequest = new SimpleFormRouter("test_form");

		if (simpleFormRequest.isSubmitted(request)) {
				Map<String, List<String>> errors = validator.validate(AccountForm.class, request.getParameterMap());
				if (errors != null) {
					AccountForm account = validator.getMapper().toObject(AccountForm.class, request.getParameterMap());
				//do something with valid bean

				request.setAttribute("test_form", account);
				//now display bean
				} else {
				//handle error
				}
		} else {
			simpleForm.inRequest(request);
			//forward to formPage
		}
	}
}
