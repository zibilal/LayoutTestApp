package com.zibilal.layouttestapp.model;

/**
 * Created by Bilal on 11/23/2015.
 */
public class DefaultDataModel implements IDataAdapter {
    private String title;

    public DefaultDataModel() {}

    public DefaultDataModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static DefaultDataModel getThis(IDataAdapter iDataAdapter) throws WrongModelObjectException {
        if (DefaultDataModel.class.isInstance(iDataAdapter)) {
            return (DefaultDataModel) iDataAdapter;
        } else {
            throw new WrongModelObjectException("Wrong data model");
        }
    }
}
