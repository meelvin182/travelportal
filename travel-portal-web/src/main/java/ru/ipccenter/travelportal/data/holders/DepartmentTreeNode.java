package ru.ipccenter.travelportal.data.holders;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import ru.ipccenter.travelportal.common.model.objects.Department;
import ru.ipccenter.travelportal.services.DepartmentService;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Ivan on 25.04.2015.
 */
public final class DepartmentTreeNode extends DefaultTreeNode {

    private final BigInteger departmentId;
    private final DepartmentService departmentService;
    private final boolean autoLoad;

    public DepartmentTreeNode(String nodeName, DepartmentService departmentService, boolean autoLoad) {
        super(nodeName);
        this.departmentId = null;
        this.departmentService = departmentService;
        this.autoLoad = autoLoad;
    }

    public DepartmentTreeNode(Department department, DepartmentService departmentService) {
        super(department.getName());
        this.departmentId = department.getId();
        this.departmentService = departmentService;
        this.autoLoad = true;
    }

    public DepartmentTreeNode(Department department, DepartmentTreeNode parent) {
        super(department.getName());
        this.departmentId = department.getId();
        this.departmentService = parent.getDepartmentService();
        this.autoLoad = true;
        this.setParent(parent);
        parent.getChildren().add(this);
    }

    public BigInteger getDepartmentId() {
        return departmentId;
    }

    @Override
    public DepartmentTreeNode getParent() {
        return (DepartmentTreeNode) super.getParent();
    }

    public List<TreeNode> getChildren(boolean autoLoad) {
        if (!autoLoad) {
            return super.getChildren();
        } else {
            return getChildren();
        }
    }

    public List<TreeNode> getChildren() {
        List<TreeNode> children = super.getChildren();
        if (autoLoad && (children == null || children.isEmpty())) {
            loadChildren();
        }

        return super.getChildren();
    }

    protected void loadChildren() {
        DepartmentTreeNode.super.getChildren().clear();
        List<Department> children = (departmentId != null)
                ? departmentService.getDepartment(departmentId).getChildren()
                : departmentService.getMajorDepartments();

        for (Department childDepartment : children) {
            DepartmentTreeNode.super.getChildren().add(new DepartmentTreeNode(childDepartment, departmentService));
        }
    }

    @Override
    public int getChildCount() {
        return getChildren().size();
    }

    protected DepartmentService getDepartmentService() {
        return departmentService;
    }
}
