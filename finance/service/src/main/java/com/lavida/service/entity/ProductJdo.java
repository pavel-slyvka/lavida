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

    private String imageSrcURL;

    private String producerBrand;

    private String name;

    private String code;

    @Transient
    private boolean processed;

    public ProductJdo() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageSrcURL() {
        return imageSrcURL;
    }

    public void setImageSrcURL(String imageSrcURL) {
        this.imageSrcURL = imageSrcURL;
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

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductJdo that = (ProductJdo) o;

        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (imageSrcURL != null ? !imageSrcURL.equals(that.imageSrcURL) : that.imageSrcURL != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (producerBrand != null ? !producerBrand.equals(that.producerBrand) : that.producerBrand != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = imageSrcURL != null ? imageSrcURL.hashCode() : 0;
        result = 31 * result + (producerBrand != null ? producerBrand.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductJdo{" +
                "id=" + id +
                ", imageSrcURL='" + imageSrcURL + '\'' +
                ", producerBrand='" + producerBrand + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", processed=" + processed +
                '}';
    }
}
