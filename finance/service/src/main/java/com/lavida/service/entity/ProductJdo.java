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

    private String imageSrcURL;

    private String producerBrand;

    private String name;

    private String code;

    public ProductJdo() {
    }

    public ProductJdo(String hostURL, String imageSrcURL, String producerBrand, String name, String code) {
        this.hostURL = hostURL;
        this.imageSrcURL = imageSrcURL;
        this.producerBrand = producerBrand;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostURL() {
        return hostURL;
    }

    public void setHostURL(String hostURL) {
        this.hostURL = hostURL;
    }

    public String getImageSrcURL() {
        return imageSrcURL;
    }

    public void setImageSrcURL(String srcURL) {
        this.imageSrcURL = srcURL;
    }

    public String getProducerBrand() {
        return producerBrand;
    }

    public void setProducerBrand(String producerBrand) {
        this.producerBrand = producerBrand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
        if (imageSrcURL != null ? !imageSrcURL.equals(that.imageSrcURL) : that.imageSrcURL != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hostURL != null ? hostURL.hashCode() : 0;
        result = 31 * result + (imageSrcURL != null ? imageSrcURL.hashCode() : 0);
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
                ", imageSrcURL='" + imageSrcURL + '\'' +
                ", producerBrand='" + producerBrand + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
