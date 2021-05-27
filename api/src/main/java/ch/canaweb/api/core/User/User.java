package ch.canaweb.api.core.User;

import java.util.Date;

public class User {
    private int id;
    private int name;
    private Date lastLogin;
    private Date created;

    public User(int name, Date lastLogin, Date created) {
        this.name = name;
        this.lastLogin = lastLogin;
        this.created = created;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }
}
