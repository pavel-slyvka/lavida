package com.lavida.service.entity;

import javax.persistence.*;

/**
 * The SizeJdo
 * <p/>
 * Created: 13.10.13 18:25.
 *
 * @author Ruslan.
 */
@Entity
@Table(name = "size")
@NamedQueries({
        @NamedQuery(name = SizeJdo.FIND_BY_NAME, query = "select s from SizeJdo s where s.name = :name")
})
public class SizeJdo {
    public static final String FIND_BY_NAME = "SizeJdo.findByName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public SizeJdo() {
    }

    public SizeJdo(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SizeJdo sizeJdo = (SizeJdo) o;

        if (name != null ? !name.equals(sizeJdo.name) : sizeJdo.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SizeJdo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
