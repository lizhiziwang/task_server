package com.zsh.task.constant;

public enum IdentityType {
    ADMIN("管理员",0,"ADMIN"),
    VIP("会员",1,"VIP"),
    COM_USRE("普通用户",2,"COM_USRE");

    private final String name_;
    private final int Lev;
    private final String code;

    IdentityType(String name,int Lev,String code){
        this.name_ = name;
        this.Lev = Lev;
        this.code = code;
    }
    public String getName_(){
        return this.name_;
    }

    public static IdentityType getByLev(int lev){
        IdentityType[] values = values();
        for (IdentityType item : values) {
            if (item.Lev == lev) {
                return item;
            }
        }
        throw new IllegalArgumentException("Parameter not present");
    }
}
