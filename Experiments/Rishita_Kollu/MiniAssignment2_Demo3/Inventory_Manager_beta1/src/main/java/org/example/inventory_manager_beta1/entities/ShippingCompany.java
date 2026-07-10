package org.example.inventory_manager_beta1.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "Shipping_Companies")
@ToString
public class ShippingCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer shippingCompanyId;

    @Column(name = "company_name")
    private String shippingCompanyName;

    @Column(name = "company_email")
    private String shippingCompanyEmail;

    @Column(name = "company_phone")
    private String shippingCompanyPhone;

    public ShippingCompany() {}

    public ShippingCompany(String companyName, String companyEmail, String companyPhone) {
        this.shippingCompanyName = companyName;
        this.shippingCompanyEmail = companyEmail;
        this.shippingCompanyPhone = companyPhone;
    }
}
