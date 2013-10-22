package com.lavida.service.entity;

import javax.persistence.*;

/**
 * The ProductJdo
 * <p/>
 * Created: 22.10.13 11:19.
 *
 * @author Ruslan.
 */
@Entity
@Table (name = "products")
public class ProductJdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String hostURL;

    private String srcURL;

    private String producerBrand;

    private String name;

    private String code;

    public ProductJdo() {
    }

    public ProductJdo(String hostURL, String srcURL, String producerBrand, String name, String code) {
        this.hostURL = hostURL;
        this.srcURL = srcURL;
        this.producerBrand = producerBrand;
        this.name = name;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductJdo that = (ProductJdo) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (hostURL != null ? !hostURL.equals(that.hostURL) : that.hostURL != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (producerBrand != null ? !producerBrand.equals(that.producerBrand) : that.producerBrand != null)
            return false;
        if (srcURL != null ? !srcURL.equals(that.srcURL) : that.srcURL != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hostURL != null ? hostURL.hashCode() : 0;
        result = 31 * result + (srcURL != null ? srcURL.hashCode() : 0);
        result = 31 * result + (producerBrand != null ? producerBrand.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductJdo{" +
                "id=" + id +
                ", hostURL='" + hostURL + '\'' +
                ", srcURL='" + srcURL + '\'' +
                ", producerBrand='" + producerBrand + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
