package com.lavida.service.entity;

import javax.persistence.*;

/**
 * The SellerJdo is an entity for a seller .
 * Created: 12:51 02.09.13
 *
 * @author Ruslan
 */
@Entity
@Table(name = "sellers")
@NamedQueries({
        @NamedQuery(name = SellerJdo.FIND_BY_NAME, query = "select s from SellerJdo s where s.name = :name")
})
public class SellerJdo {
    public static final String FIND_BY_NAME = "SellerJdo.findByName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public SellerJdo() {
    }

    public SellerJdo(String name) {
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

        SellerJdo seller = (SellerJdo) o;

        if (id != seller.id) return false;
        if (!name.equals(seller.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SellerJdo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
