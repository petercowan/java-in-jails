package org.jails.form.taglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class RepeatTag extends BodyTagSupport {
	private static Logger logger = LoggerFactory.getLogger(RepeatTag.class);

	private SimpleFormTag formTag;
	private int times;
	private int index;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int doStartTag() throws JspException {
		formTag = (SimpleFormTag) TagSupport.findAncestorWithClass(this, SimpleFormTag.class);
		if (formTag == null) {
			if (formTag == null) {
				throw new JspTagException("A RepeatTag tag must be nested within a FormTag.");
			}
		}
		return EVAL_PAGE;
	}

	@Override
	public int doAfterBody() throws JspException {
		int repeatTimes = (formTag.getSimpleForm() == null)
				? 1
				: formTag.getSimpleForm().getTimesToRepeat();
		logger.info("appending content for index: " + index + " of " + repeatTimes);

		if (++index < repeatTimes) {
			return EVAL_BODY_AGAIN;
		} else {
			logger.info("ending evaluation");
			return SKIP_BODY;
		}
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			// Get the bodyContent for this tag and
			//wrap with form content
			JspWriter jspOut = pageContext.getOut();
			jspOut.print(bodyContent.getString());

			index = 0;
			return EVAL_PAGE;
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}
}
