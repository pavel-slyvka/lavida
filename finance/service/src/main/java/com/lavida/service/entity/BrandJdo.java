package com.lavida.service.entity;

import javax.persistence.*;

/**
 * The BrandJdo
 * <p/>
 * Created: 12.10.13 9:55.
 *
 * @author Ruslan.
 */

@Entity
@Table(name = "brand")
@NamedQueries({
        @NamedQuery(name = BrandJdo.FIND_BY_NAME, query = "select b from BrandJdo b where b.name= :name")
})
public class BrandJdo {
    public static final String FIND_BY_NAME = "BrandJdo.findByName";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    private String name;

    public BrandJdo() {
    }

    public BrandJdo(String name) {
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

        BrandJdo brandJdo = (BrandJdo) o;

        if (name != null ? !name.equals(brandJdo.name) : brandJdo.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "BrandJdo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
