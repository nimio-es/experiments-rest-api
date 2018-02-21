package es.nimio.exercise.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private long id;

    @Column(name = "ref")
    @NotNull
    private String ref;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "common_price")
    @NotNull
    private Double commonPrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCommonPrice() {
        return commonPrice;
    }

    public void setCommonPrice(Double commonPrice) {
        this.commonPrice = commonPrice;
    }

    public Product() {}

    public Product(final String ref, final String name, final Double commonPrice) {
        this.ref = ref;
        this.name = name;
        this.commonPrice = commonPrice;
    }
}
