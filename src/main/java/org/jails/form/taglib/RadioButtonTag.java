package org.jails.form.taglib;

import org.jails.form.RadioButtonInput;
import org.jails.form.constructor.RadioButtonConstructor;
import org.jails.form.constructor.TagInputConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class RadioButtonTag
		extends FormInputTagSupport
		implements RadioButtonInput {
    protected static Logger logger = LoggerFactory.getLogger(RadioButtonTag.class);

	private String value;
	private String checked;
	private String tabIndex;
    private RadioGroupTag radioGroupTag;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getChecked() {
		return checked;
	}

	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String getTabIndex() {
		return tabIndex;
	}

    public RadioGroupTag getRadioGroupTag() {
        return radioGroupTag;
    }

    @Override
    public int doStartTag() throws JspException {
        radioGroupTag = (RadioGroupTag) TagSupport.findAncestorWithClass(this, RadioGroupTag.class);
        if (radioGroupTag == null) {
            throw new JspTagException("A RadioButtonTag must be nested within a RadioGroupTag.");
        }
        logger.info("adding button to radioGroupTag");
        radioGroupTag.addButton(this);
        return super.doStartTag();
    }

    @Override
	protected TagInputConstructor getInputConstructor(SimpleFormTag formTag, RepeaterTag repeatTag, ServletRequest request) throws JspTagException {
        logger.info("setting radioGroupTag in RadioButtonConstructor: " + radioGroupTag);

		return new RadioButtonConstructor(this, radioGroupTag, formTag, repeatTag, request);
	}
}

