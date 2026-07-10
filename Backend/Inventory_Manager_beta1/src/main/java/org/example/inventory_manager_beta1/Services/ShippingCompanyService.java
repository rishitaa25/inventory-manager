package org.example.inventory_manager_beta1.Services;

import org.example.inventory_manager_beta1.DTO.Shipments.ShippingCompanyRequest;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.ShippingCompanyNotFoundException;
import org.example.inventory_manager_beta1.Entities.ShippingCompany;
import org.example.inventory_manager_beta1.Repositories.ShippingCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * A robust service class to handle all the different logic and search methods relating
 * to the ShippingCompany class. It is complete with all the ShippingCompanyRepository
 * search methods defined, and it handles every ShippingCompany related logic method
 * with included custom error handling
 */
@Service
public class ShippingCompanyService {
    /**
     * The auto-injected ShippingCompanyRepository for handling custom searches
     */
    @Autowired
    private final ShippingCompanyRepository shippingCompanyRepository;

    /**
     * INIT: Used for initializing the shippingCompanyRepository for custom database searches
     * @param shippingCompanyRepository
     *  The repository containing the custom search methods
     */
    public ShippingCompanyService(ShippingCompanyRepository shippingCompanyRepository) {this.shippingCompanyRepository = shippingCompanyRepository;}

    /*==================================SEARCH METHODS==================================*/

    /**
     * A search method to find a specific shipping company by its ID
     * @param id
     *  The ID of the shippingCompany
     * @return
     *  The ShippingCompany with the matching ID if found
     * @throws ShippingCompanyNotFoundException
     *  Custom RuntimeException if ShippingCompany ID search doesn't find anything
     */
    public ShippingCompany findById(Integer id) {
        return shippingCompanyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShippingCompany with ID: " + id + " not found"));
    }

    /**
     * A search method to find a specific shipping company by its name
     * @param companyName
     *  The name of the ShippingCompany
     * @return
     *  The ShippingCompany with the matching name
     * @throws ShippingCompanyNotFoundException
     *  Custom RuntimeException if ShippingCompany name search doesn't find anything
     */
    public ShippingCompany findByShippingCompanyName(String companyName) {
        return shippingCompanyRepository.findByShippingCompanyName(companyName)
                .orElseThrow(() -> new ShippingCompanyNotFoundException("Shipping company with name: " + companyName + " not found"));
    }

    /**
     * A search method to find a specific shipping company by its email
     * @param companyEmail
     *  The email of the ShippingCompany
     * @return
     *  The ShippingCompany with the matching email
     * @throws ShippingCompanyNotFoundException
     *  Custom RuntimeException if ShippingCompany email search doesn't find anything
     */
    public ShippingCompany findByShippingCompanyEmail(String companyEmail) {
        return shippingCompanyRepository.findByShippingCompanyEmail(companyEmail)
                .orElseThrow(() -> new ShippingCompanyNotFoundException("Shipping company with email: " + companyEmail + " not found"));
    }

    /**
     * A search method to find a specific shipping company by its phone number
     * @param companyPhone
     *  The phone number of the ShippingCompany
     * @return
     *  The shippingCompany with the matching phone number
     * @throws ShippingCompanyNotFoundException
     *  Custom RuntimeException if ShippingCompany phone number search doesn't find anything
     */
    public ShippingCompany findByShippingCompanyPhone(String companyPhone) {
        return shippingCompanyRepository.findByShippingCompanyPhone(companyPhone)
                .orElseThrow(() -> new ShippingCompanyNotFoundException("Shipping company with phone number: " + companyPhone + " not found"));
    }

    /**
     * A search method to find all shipping companies in the database
     * @return
     *  A list of all ShippingCompanies
     */
    public List<ShippingCompany> findAll() {return shippingCompanyRepository.findAll();}

    /*==================================LOGIC METHODS==================================*/

    /**
     * A logic method that creates a new ShippingCompany
     * @param shippingCompanyRequest
     *  The DTO containing the information about the ShippingCompany from JSON
     */
    public void addShippingCompany(ShippingCompanyRequest shippingCompanyRequest) {
        ShippingCompany company = new ShippingCompany(shippingCompanyRequest.getShippingCompanyName(), shippingCompanyRequest.getShippingCompanyEmail(), shippingCompanyRequest.getShippingCompanyPhone());
        shippingCompanyRepository.save(company);
    }

    /**
     * A logic method that updates an existing ShippingCompany
     * @param companyId
     *  The ID of the ShippingCompany being updated
     * @param companyRequest
     *  The new JSON packet with updated information
     */
    public void updateShippingCompany(Integer companyId, ShippingCompanyRequest companyRequest) {
        ShippingCompany company = findById(companyId);
        company.setShippingCompanyName(companyRequest.getShippingCompanyName());
        company.setShippingCompanyEmail(companyRequest.getShippingCompanyEmail());
        company.setShippingCompanyPhone(companyRequest.getShippingCompanyPhone());
        shippingCompanyRepository.save(company);
    }

    /**
     * A logic method that deletes a specific ShippingCompany
     * @param companyId
     *  The ID of the ShippingCompany
     */
    public void deleteShippingCompany(Integer companyId) {
        ShippingCompany company = findById(companyId);
        shippingCompanyRepository.delete(company);
    }
}
