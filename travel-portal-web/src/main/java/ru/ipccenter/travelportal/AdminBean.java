package ru.ipccenter.travelportal;

import org.apache.log4j.Logger;
import org.primefaces.event.SelectEvent;
import ru.ipccenter.travelportal.common.caches.LoadCallback;
import ru.ipccenter.travelportal.common.caches.UnlimitedCache;
import ru.ipccenter.travelportal.common.model.TPObjectFactory;
import ru.ipccenter.travelportal.common.model.objects.*;
import ru.ipccenter.travelportal.data.holders.DepartmentInfo;
import ru.ipccenter.travelportal.data.holders.DepartmentTreeNode;
import ru.ipccenter.travelportal.data.holders.EmployeeInfo;
import ru.ipccenter.travelportal.dialogs.EmployeeBean;
import ru.ipccenter.travelportal.services.DepartmentService;
import ru.ipccenter.travelportal.services.EmployeeService;
import ru.ipccenter.travelportal.utils.MessageUtils;
import ru.ipccenter.travelportal.utils.PopupBuilder;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;


@ManagedBean
@ViewScoped
public class AdminBean implements Serializable {

    private static final Logger log = Logger.getLogger(AdminBean.class);

    @Inject
    private AdminTreeBean tree;
    @Inject
    private DepartmentService departmentService;
    @Inject
    private EmployeeService employeeService;

    @EJB
    private TPObjectFactory factory;

    private DepartmentInfoCache departmentInfoCache = new DepartmentInfoCache();
    private int employeeCreateIndex;

    private class DepartmentInfoCache extends UnlimitedCache<BigInteger, DepartmentInfo> {
        private DepartmentInfoCache() {
            super(new LoadCallback<BigInteger, DepartmentInfo>() {
                @Override
                public DepartmentInfo load(BigInteger key) {
                    try {
                        return new DepartmentInfo(key, getDepartmentService());
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid department key #" + key);
                    }
                }
            });
        }

        @Override
        protected Logger getLogger() {
            return log;
        }

        @Override
        protected Integer getDefaultSize() {
            return 15;
        }

        @Override
        protected Integer getSize() throws Exception {
            return 15;
        }
    }

    public AdminTreeBean getTree() {
        return tree;
    }

    public DepartmentInfo getDepartmentInfo() {
        try {
            return departmentInfoCache.get(getSelectedDepartmentId());
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            return new DepartmentInfo();
        }
    }

    public DepartmentInfo.DepartmentType[] getDepartmentTypes() {
        return DepartmentInfo.DepartmentType.values();
    }

    public boolean showEditor() {
        return (getSelectedDepartmentId() != null);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void saveDepartment() {
        final BigInteger selectedId = getSelectedDepartmentId();
        final DepartmentInfo departmentInfo = departmentInfoCache.get(selectedId);

        boolean canSave = true;

        if (departmentInfo.getName().trim().isEmpty()) {
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_WARN, "Can't save", "Name is empty");
            canSave = false;
        }

        if (departmentInfo.getManagerId() == null) {
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_WARN, "Can't save", "Manager is empty");
            canSave = false;
        }

        if (!canSave) { return; }

        try {
            final Department department = departmentService.getDepartment(departmentInfo.getId());

            department.setName(departmentInfo.getName().trim());
            department.setManagerId(departmentInfo.getManagerId());

            final Employee manager = department.getManager();
            final Employee oldManager = department.getManager();

            if (oldManager != null) {
                final List<Employee> employees = new LinkedList<>(oldManager.getChildren());
                employees.add(oldManager);

                for (Employee employee: employees) {
                    if (!employee.getId().equals(manager.getId())) {
                        employee.setParentId(manager.getId());
                        employee.setManagerId(manager.getId());
                        employee.setOfficeId(manager.getOfficeId());
                    } else {
                        //TODO: manager of parent department
                        employee.setManagerId(null);
                        employee.setParentId(null);
                    }
                }
            }

            tree.getSelectedDepartmentTreeNode().setData(departmentInfo.getName().trim());
            departmentInfoCache.evict(selectedId);
        } catch (Exception e) {
            log.error("Can't save", e);
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_ERROR, "Can't save", e.getMessage());
        }
    }

    public void editEmployee() {
        final DepartmentInfo department = departmentInfoCache.get(getSelectedDepartmentId());
        final EmployeeInfo selectedEmployee = department.getSelectedEmployee();

        if (selectedEmployee == null) {
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_WARN, "Can't edit", "Please select employee before");
            return;
        }

        new PopupBuilder() {
            @Override
            protected Map<String, Object> getPopupOptions() {
                return new HashMap<String, Object>() {{
                    put("modal", true);
                    put("draggable", false);
                    put("resizable", false);
                    put("contentWidth", 360);
                    put("contentHeight", 420);
                    put("includeViewParams", true);
                }};
            }

            @Override
            protected Map<String, List<String>> getPopupParams() {
                return new HashMap<String, List<String>>() {{
                    put(EmployeeBean.EMPLOYEE_ID_KEY, Collections.singletonList(selectedEmployee.getId().toString()));
                }};
            }

            @Override
            protected String getOutcome() {
                return "/dialogs/employee.xhtml";
            }
        }.show();
        departmentInfoCache.evict(getSelectedDepartmentId());
    }

    public void onEditEmployeeReturn(SelectEvent event) {
        BigInteger departmentId = (BigInteger) event.getObject();
        if (departmentId != null) {
            departmentInfoCache.evict(departmentId);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void createEmployee() {
        final DepartmentInfo departmentInfo = departmentInfoCache.get(getSelectedDepartmentId());

        try {
            if (departmentInfo.getManagerId() != null) {
                final Employee manager = employeeService.getEmployee(departmentInfo.getManagerId());
                Employee employee = employeeService.createEmployee(
                        "New Employee " + (++employeeCreateIndex),
                        manager.getId(),
                        departmentInfo.getId()
                );
                employee.setOfficeId(manager.getOfficeId());
            } else {
                Employee employee = employeeService.createEmployee(
                        "New Employee " + (++employeeCreateIndex),
                        null,
                        departmentInfo.getId()
                );

                departmentInfo.setManagerId(employee.getId());
            }

            departmentInfoCache.evict(getSelectedDepartmentId());
        } catch (Exception e) {
            log.error("Can't create employee", e);
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_ERROR, "Can't create", e.getMessage());
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteEmployee() {
        final DepartmentInfo departmentInfo = departmentInfoCache.get(getSelectedDepartmentId());
        final List<EmployeeInfo> employees = departmentInfo.getEmployees();
        final EmployeeInfo employee = departmentInfo.getSelectedEmployee();

        if (employee == null) {
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_ERROR, "Please select employee before", "");
            return;
        }

        if (employee.getId().equals(departmentInfo.getManagerId())) {
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_ERROR,
                    "Can't delete manager",
                    "Please select other manager and save department before");
            return;
        }

        try {
            //employee.setManagerId(null); // after move emp from other mgr
            employeeService.getEmployee(employee.getId()).delete();
            departmentInfoCache.evict(getSelectedDepartmentId());
        } catch (Exception e) {
            log.error("Can't delete employee", e);
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_ERROR, "Can't delete", e.getMessage());
        }
    }

    public void moveFromOtherDepartment() {

    }

    protected BigInteger getSelectedDepartmentId() {
        final DepartmentTreeNode treeNode = tree.getSelectedDepartmentTreeNode();
        return (treeNode != null)
                ? treeNode.getDepartmentId()
                : null;
    }

    protected DepartmentService getDepartmentService() {
        return departmentService;
    }
}
