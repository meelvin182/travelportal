package ru.ipccenter.travelportal.utils;

import org.primefaces.context.RequestContext;

import java.util.List;
import java.util.Map;

abstract public class PopupBuilder {
    public final void show() {
        RequestContext.getCurrentInstance().openDialog(getOutcome(), getPopupOptions(), getPopupParams());
    };

    protected abstract Map<String, Object> getPopupOptions();
    protected abstract Map<String, List<String>> getPopupParams();
    protected abstract String getOutcome();
}
