package classes;

import subscription.Subscriber;

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
    public String toString() {
        return "User{" +
                "id: " + id +
                ", name: " + name +
                ", email: " + email +
                ", Suscriber? > " + isSuscriber +
                '}';
    }
}

