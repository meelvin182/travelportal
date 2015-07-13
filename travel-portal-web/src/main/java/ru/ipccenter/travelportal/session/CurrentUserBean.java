package ru.ipccenter.travelportal.session;

import org.apache.log4j.Logger;
import org.jboss.security.SecurityUtil;
import ru.ipccenter.travelportal.common.model.objects.Role;
import ru.ipccenter.travelportal.common.model.objects.User;
import ru.ipccenter.travelportal.security.UserService;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.security.acl.Group;

@SessionScoped
public class CurrentUserBean implements Serializable {
    private static final String SUBJECT_CONTEXT_KEY = "javax.security.auth.Subject.container";
    public static final Logger LOG = Logger.getLogger(CurrentUserBean.class);
    Subject subject;

    @EJB
    private UserService userService;

    private User user;
    private Role majorRole;

    public Subject getSubject() {
        try {
            subject = (Subject) PolicyContext.getContext(SUBJECT_CONTEXT_KEY);
        } catch (Exception e) {
            LOG.error(e);
        }
        return subject;
    }

    public Group getRoles() {
        if (subject != null) {
            return SecurityUtil.getSubjectRoles(getSubject());
        }
        return null;
    }

    public void logout() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) context.getSession(false);
        if(session != null)
            session.invalidate();
    }

    public User getUser() {
        if (user != null) {
            return user;
        } else {
            if (this.getSubject() == null) {
                return null;
            }

            String username = this.getSubject().getPrincipals().iterator().next().getName();
            return user = userService.getUser(username);
        }
    }

   public Role getMajorRole() {
       if (majorRole != null) {
           return majorRole;
       } else {
           if (this.getUser() == null) {
               return null;
           }
           return majorRole = this.getUser().getRole();
       }
   }
}
