package org.jails.form.taglib;

import org.jails.form.input.Repeater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class RepeaterTag
		extends BodyTagSupport
		implements Repeater {
	private static Logger logger = LoggerFactory.getLogger(RepeaterTag.class);

	private Integer times;
	private Integer index = 0;

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public int doStartTag() throws JspException {
		SimpleFormTag formTag = (SimpleFormTag) TagSupport.findAncestorWithClass(this, SimpleFormTag.class);
		if (formTag == null) {
			if (formTag == null) {
				throw new JspTagException("A RepeatTag tag must be nested within a FormTag.");
			}
		}
		if (times == null) {
			times = (formTag.getSimpleForm() == null)
					? 1
					: formTag.getSimpleForm().getTimesToRepeat();
			logger.info("set repeater times to " + times);
		}
		return EVAL_PAGE;
	}

	@Override
	public int doAfterBody() throws JspException {
		logger.info("appending content for index: " + index + " of " + index);

		if (++index < times) {
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
