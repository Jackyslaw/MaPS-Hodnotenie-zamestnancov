package com.hackag.fibimeter.dto;

/**
 * @author Jakub Růžička
 */
public class RoleInformation {

    private boolean isAdmin;
    private boolean isManager;
    private boolean isUser;

    public RoleInformation() {
    }

    public RoleInformation(boolean isAdmin, boolean isManager, boolean isUser) {
        this.isAdmin = isAdmin;
        this.isManager = isManager;
        this.isUser = isUser;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public boolean getIsManager() {
        return isManager;
    }

    public boolean getIsUser() {
        return isUser;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setIsManager(boolean manager) {
        isManager = manager;
    }

    public void setIsUser(boolean user) {
        isUser = user;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RoleInformation{");
        sb.append("isAdmin=").append(isAdmin);
        sb.append(", isManager=").append(isManager);
        sb.append(", isUser=").append(isUser);
        sb.append('}');
        return sb.toString();
    }
}
