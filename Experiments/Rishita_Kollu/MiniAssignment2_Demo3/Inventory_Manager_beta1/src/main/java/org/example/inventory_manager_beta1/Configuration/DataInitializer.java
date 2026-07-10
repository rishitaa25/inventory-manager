package org.example.inventory_manager_beta1.Configuration;

import org.example.inventory_manager_beta1.entities.ShippingCompany;
import org.example.inventory_manager_beta1.repositories.ShippingCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private final ShippingCompanyRepository shippingCompanyRepository;

    public DataInitializer (ShippingCompanyRepository shippingCompanyRepository) {this.shippingCompanyRepository = shippingCompanyRepository;}

    @Override
    public void run(String... args) {
        if (shippingCompanyRepository.count() == 0) {
            shippingCompanyRepository.saveAll(List.of(
                    new ShippingCompany("FedEx", "fedex@test.com", "555-0001"),
                    new ShippingCompany("UPS", "ups@test.com", "555-0002"),
                    new ShippingCompany("DHL", "dhl@test.com", "555-0003"),
                    new ShippingCompany("Amazon Logistics", "amazon@test.com", "555-0004")
            ));
            //Debug: Remove later
            System.out.println("Test shipping companies seeded");
        }
    }
}