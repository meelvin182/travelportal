package ru.ipccenter.travelportal;

import ru.ipccenter.travelportal.common.model.objects.Role;
import ru.ipccenter.travelportal.session.CurrentUserBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@ManagedBean(name = "TabMenu")
public class TabMenu {
    private List<Tab> tabs = new ArrayList<>();
    Role currentRole;

    @Inject
    CurrentUserBean currentUser;


//    @PostConstruct
//    public void init() {
//        tabs = new ArrayList<>();
//
//        Role role = currentUser.getMajorRole();
//        if (role == null) {
//            tabs.add(new Tab("Debug", "/debug.xhtml"));
//        } else {
//            tabs.add(new Tab("Employee", ""));
//            tabs.add(new Tab("Reports", ""));
//            if (!role.getName().equals("Common Department")) {
//                tabs.add(0, new Tab("Travel Manager", ""));
//                if (role.getName().equals("IT Department")) {
//                    tabs.add(2, new Tab("Administrator", ""));
//                }
//            }
//        }
//    }
//
//    public List<Tab> getTabs() {
//        return tabs;
//    }

    public void setCurrentRole(Role role) {
        if (currentRole == null || role == null || !currentRole.getId().equals(role.getId())) {
            this.currentRole = role;
            updateTabs(this.currentRole);
        }
    }

    public void updateTabs (Role role) {
        tabs.removeAll(tabs);
        if (role == null) {
        }
        else {
            for(ru.ipccenter.travelportal.common.model.attributes.Tab tab: role.getTabs()) {
                tabs.add(new Tab(tab));
            }
//            tabs.add(new Tab("Employee", ""));
//            tabs.add(new Tab("Reports", ""));
//            if (!role.getName().equals("Common Department")) {
//                tabs.add(0, new Tab("Travel Manager", ""));
//                if (role.equals("IT Department")) {
//                    tabs.add(2, new Tab("Administrator", ""));
//                }
//            }
        }
    }

    public List<Tab> getTabs() {
        Role role = currentUser.getMajorRole();
        setCurrentRole(role);
        return tabs;
    }

    public static class Tab {
        private String name;
        private String address;

        public Tab(String name, String address) {
            this.name = name;
            this.address = address;
        }

        public Tab(ru.ipccenter.travelportal.common.model.attributes.Tab tab) {
            if(tab == null) {
                this.name = "";
                this.address = "";
            } else {
                this.name = tab.getValue();
                if(tab.getValue() == null)
                    this.address = "";
                else
                    this.address = "/tabs/" + tab.getValue().toLowerCase().replaceAll(" ", "_") + ".xhtml";
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
