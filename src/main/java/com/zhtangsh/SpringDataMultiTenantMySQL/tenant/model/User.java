package com.zhtangsh.SpringDataMultiTenantMySQL.tenant.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Created by Zihan Eric Tang(mrzihan.tang@gmail.com) on 2018/12/10
 */
@Entity
public class User {
    @Id
    private long id;
    private String name;
    private String extra;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
