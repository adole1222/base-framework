package com.adole.base;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类
 */
public class BaseEntity implements Serializable {

    private Date addTime;

    private String addUser;

    private Date updateTime;

    private Date updateUser;

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Date updateUser) {
        this.updateUser = updateUser;
    }

}
