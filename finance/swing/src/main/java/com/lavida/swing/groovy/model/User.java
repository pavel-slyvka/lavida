package com.lavida.swing.groovy.model;

//import com.tangosol.io.pof.PofReader;
//import com.tangosol.io.pof.PofWriter;
//import com.tangosol.io.pof.PortableObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.io.Serializable;

@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"login"}))
public class User implements
//        PortableObject,
        Serializable {

	private static final long serialVersionUID = 8979110171187319767L;

	@Id
    @GeneratedValue
    private Long id;

    private String login;

    private String password;

    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 characters long.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Firstname must be alphanumeric with no spaces")
    private String firstName;

    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 characters long.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Lastname must be alphanumeric with no spaces")
    private String lastName;

    @Min(10)
    @Max(100)
    private int age;

    @Pattern(regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", message = "Invalid email address.")
    private String email;

    public User() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (age != user.age) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + age;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }

/*
    public void readExternal(PofReader pofReader) throws IOException {
        setId(pofReader.readLong(0));
        setFirstName(pofReader.readString(1));
        setLastName(pofReader.readString(2));
        setAge(pofReader.readInt(3));
        setLogin(pofReader.readString(4));
        setPassword(pofReader.readString(5));
    }

    public void writeExternal(PofWriter pofWriter) throws IOException {
        pofWriter.writeLong(0, getId());
        pofWriter.writeString(1, getFirstName());
        pofWriter.writeString(2, getLastName());
        pofWriter.writeInt(3, getAge());
        pofWriter.writeString(4, getLogin());
        pofWriter.writeString(5, getPassword());
    }
*/
}
