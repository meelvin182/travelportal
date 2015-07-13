package ru.ipccenter.travelportal;

/**
 * Created by meelvin182 on 02.04.15.
 */

import org.apache.log4j.Logger;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;
import ru.ipccenter.travelportal.common.model.objects.Department;
import ru.ipccenter.travelportal.data.holders.DepartmentTreeNode;
import ru.ipccenter.travelportal.services.DepartmentService;
import ru.ipccenter.travelportal.utils.MessageUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@ViewScoped
public class AdminTreeBean implements Serializable {

    public static final Logger log = Logger.getLogger(AdminTreeBean.class);

    public static abstract class NodeSelectCallback {

        public abstract void handle(TreeNode node);
    }

    @Inject
    private DepartmentService departmentService;

    private DepartmentTreeNode root;

    List<NodeSelectCallback> callbacks = new LinkedList<>();

    private TreeNode selectedNode;
    private int creationIndex;


    @PostConstruct
    public void init() {
        root = new DepartmentTreeNode("Root", departmentService, false);
        selectedNode = new DepartmentTreeNode("Departments", departmentService, true);
        root.getChildren().add(selectedNode);
        selectedNode.setParent(root);
        selectedNode.setExpanded(true);
        selectedNode.setSelected(true);
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public DepartmentTreeNode getSelectedDepartmentTreeNode() {
        return (DepartmentTreeNode) getSelectedNode();
    }

    public void setSelectedNode(TreeNode selectedNode) {
        for (NodeSelectCallback callback: callbacks) {
            callback.handle(selectedNode);
        }
        this.selectedNode = selectedNode;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void newDepartment() {
        DepartmentTreeNode parent = getSelectedDepartmentTreeNode();

        if (parent == null) {
            parent = root;
        }

        try {
            final Department newDepartment = departmentService.createDepartment(
                    "New department " + (++creationIndex),
                    parent.getDepartmentId()
                );
            final DepartmentTreeNode node = new DepartmentTreeNode(newDepartment, parent);
            node.setSelected(true);
            setSelectedNode(node);

            parent.setExpanded(true);
            parent.setSelected(false);
        } catch (Exception e) {
            MessageUtils.sendMessage(null, FacesMessage.SEVERITY_WARN,
                    "Can't create department", e.getMessage());
        }
    }

    public int addNodeSelectHandler(NodeSelectCallback handler) {
        callbacks.add(handler);
        return callbacks.size() - 1;
    }

    public void removeNodeSelectHandler(int id) {
        callbacks.remove(id);
    }

    public void onNodeSelect(NodeSelectEvent event) {
        setSelectedNode(event.getTreeNode());
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteDepartment() {
        DepartmentTreeNode node = getSelectedDepartmentTreeNode();
        if(node != null && node != root) {
            try {
                final TreeNode parent = node.getParent();
                Department department = departmentService.getDepartment(node.getDepartmentId());

                department.delete();

                List<TreeNode> children = node.getChildren(false);
                if (children != null && children.size() != 0) {
                    children.clear();
                }

                children = parent.getChildren();
                if (children != null && children.size() != 0) {
                    children.remove(node);
                    parent.setExpanded(true);
                } else {
                    parent.setExpanded(false);
                }

                parent.setSelected(true);
                setSelectedNode(node.getParent());
            } catch (Exception e) {
                log.error("Can't delete department", e);
                MessageUtils.sendMessage(null, FacesMessage.SEVERITY_ERROR, node.getData().toString(), e.getMessage());
            }
        }
    }
}
