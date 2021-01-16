package com.king.bishe.chat.Pojo;

public enum Role {
    PojoUserLevel(0),
    StaffLevel(1),
    AdministratorLevel(3)

    ;
    private int roleLevel;

    Role(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public int getRoleLevel() {
        return roleLevel;
    }
}
