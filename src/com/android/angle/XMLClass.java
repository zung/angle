package com.android.angle;

import android.util.Log;



public abstract class XMLClass
{
	protected String mCommand;
	protected AngleActivity mActivity;
	
	public abstract void setCommandDefaults ();
	public abstract void setCommandParam(String param, String value);
	public abstract void setCommandValue(String value);
	public abstract void setFieldDefaults(String field);
	public abstract void setFieldParam(String field, String param, String value);
	public abstract void setFieldValue(String field, String value);
	
	public XMLClass (AngleActivity activity)
	{
		mActivity=activity;
	}
	
	public void execute ()
	{
		Log.d("XML","class.execute");
		setCommandDefaults();
		mCommand=mActivity.xmlParser.getName().toLowerCase();
		getParams();
		setCommandValue(XMLHelper.getXMLText(mActivity.xmlParser));
		getFields();
	}

	private void getParams()
	{
		for (int t = 0; t < mActivity.xmlParser.getAttributeCount(); t++)
			setCommandParam(mActivity.xmlParser.getAttributeName(t).toLowerCase(),mActivity.xmlParser.getAttributeValue(t).toLowerCase());
	}

	private void getFields()
	{
		while (XMLHelper.nextXMLTag(mActivity.xmlParser, mCommand))
		{
			String fieldName=mActivity.xmlParser.getName().toLowerCase();
			setFieldDefaults(fieldName);
			for (int t = 0; t < mActivity.xmlParser.getAttributeCount(); t++)
				setFieldParam(fieldName, mActivity.xmlParser.getAttributeName(t).toLowerCase(),mActivity.xmlParser.getAttributeValue(t));
			setFieldValue(fieldName,XMLHelper.getXMLText(mActivity.xmlParser));
		}
	}
	
}
