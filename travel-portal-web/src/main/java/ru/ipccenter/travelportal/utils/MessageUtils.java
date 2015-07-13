package ru.ipccenter.travelportal.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Created by Ivan on 13.05.2015.
 */
public class MessageUtils {
    public static void sendMessage(String target, FacesMessage.Severity severity, String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary,  detail);
        getFacesContext().addMessage(target, message);
    }

    private static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}
