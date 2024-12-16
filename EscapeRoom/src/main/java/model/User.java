package model;

import subscription.Subscriber;

import java.util.Objects;

public class User implements Subscriber {
    private Integer id;
    private String name;
    private String email;
    private Boolean isSuscriber;

    public User(){};

    public User(String name, String email, Boolean isSuscriber) {
        this.name = name;
        this.email = email;
        this.isSuscriber = isSuscriber;
    }

    public User(Integer id, String name, String email, Boolean isSuscriber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isSuscriber = isSuscriber;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIsSuscriber(Boolean isSuscriber) {
        this.isSuscriber = isSuscriber;
    }

    @Override
    public void update(String event) {
        displayEvent(event);
    }
    public void displayEvent(String event){
        System.out.println("Hola " + this.name + ", exciting news for you:\n"
                + event + "\n");
    }

    public Boolean isSuscriber() {
        return isSuscriber;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getName(), user.getName()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(isSuscriber, user.isSuscriber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getEmail(), isSuscriber);
    }

    @Override
    public String toString() {
        return "User{" +
                "id: " + id +
                ", name: " + name +
                ", email: " + email +
                ", Suscriber? > " + isSuscriber +
                '}';
    }
}

