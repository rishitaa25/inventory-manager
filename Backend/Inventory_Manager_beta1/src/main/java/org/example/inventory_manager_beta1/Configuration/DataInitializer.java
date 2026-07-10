package org.example.inventory_manager_beta1.Configuration;

import org.example.inventory_manager_beta1.Entities.ShippingCompany;
import org.example.inventory_manager_beta1.Repositories.ShippingCompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * A data seeding class that executes on app startup and adds 4 test
 * companies to ShippingCompanies table to be used in AddItemRequests
 */
@Component
public class DataInitializer implements CommandLineRunner {
    /**
     * The auto-injected ShippingCompanyRepository used for
     */
    @Autowired
    private final ShippingCompanyRepository shippingCompanyRepository;

    /**
     * Console logger used to send messages to the server console
     */
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * INIT: The constructor for initializing the shippingCompanyRepository for
     * custom searching and other repository function
     * @param shippingCompanyRepository
     *  The custom ShippingCompanyRepository
     */
    public DataInitializer (ShippingCompanyRepository shippingCompanyRepository) {this.shippingCompanyRepository = shippingCompanyRepository;}

    /**
     * A method that runs automatically on application startup to seed the database
     * with a set of ShippingCompany objects for testing purposes.
     * If the ShippingCompany table already contains data, this method will not run
     * to avoid creating duplicate entries on every restart
     * @param args
     *  Command line arguments passed to the application on startup
     */
    @Override
    public void run(String... args) {
        if (shippingCompanyRepository.count() == 0) {
            shippingCompanyRepository.saveAll(List.of(
                    new ShippingCompany("FedEx", "fedex@test.com", "555-000-1111"),
                    new ShippingCompany("UPS", "ups@test.com", "555-000-2222"),
                    new ShippingCompany("DHL", "dhl@test.com", "555-000-3333"),
                    new ShippingCompany("Amazon Logistics", "amazon@test.com", "555-000-4444"),
                    new ShippingCompany("Fareway", "fareway@test.com", "555-000-5555")
            ));
            logger.info("Test shipping companies seeded");
        }
    }
}