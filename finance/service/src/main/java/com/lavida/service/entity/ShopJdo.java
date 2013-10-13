package com.lavida.service.entity;

import javax.persistence.*;

/**
 * The ShopJdo
 * <p/>
 * Created: 13.10.13 19:30.
 *
 * @author Ruslan.
 */
@Entity
@Table(name = "shop")
@NamedQueries({
        @NamedQuery(name = ShopJdo.FIND_BY_NAME, query = "select s from ShopJdo s where s.name = :name")
})
public class ShopJdo {
    public static final String FIND_BY_NAME = "ShopJdo.findByName";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public ShopJdo() {
    }

    public ShopJdo(String name) {
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

        ShopJdo shopJdo = (ShopJdo) o;

        if (name != null ? !name.equals(shopJdo.name) : shopJdo.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ShopJdo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
