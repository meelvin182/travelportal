package ru.ipccenter.travelportal.session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Ivan on 17.05.2015.
 */
@RequestScoped
@ManagedBean
public class LogoutListener {

    @Inject
    private CurrentUserBean currentUserBean;

    public void logout() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        currentUserBean.logout();
        try { context.redirect("/index.xhtml"); } catch (IOException e) {};
    }

    public boolean isEmployee() {
        return "Employee".equals(currentUserBean.getMajorRole().getName()) || "Common Department".equals(currentUserBean.getMajorRole().getName());
    }
}
