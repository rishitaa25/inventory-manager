package org.example.inventory_manager_beta1.services;

import org.example.inventory_manager_beta1.DTO.Shipments.ShippingCompanyRequest;
import org.example.inventory_manager_beta1.entities.ShippingCompany;
import org.example.inventory_manager_beta1.repositories.ShippingCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingCompanyService {
    @Autowired
    private final ShippingCompanyRepository shippingCompanyRepository;

    /**
     *
     * @param shippingCompanyRepository
     */
    public ShippingCompanyService(ShippingCompanyRepository shippingCompanyRepository) {this.shippingCompanyRepository = shippingCompanyRepository;}

    /*==================================SEARCH METHODS==================================*/

    /**
     * A search method to find a specific shipping company by its ID
     * @param shippingCompanyId
     *  The ID of the shippingCompany
     * @return
     *  The ShippingCompany with the matching ID if found
     */
    public ShippingCompany findByShippingCompanyId(Integer shippingCompanyId) {
        return shippingCompanyRepository.findById(shippingCompanyId)
                .orElseThrow(() -> new RuntimeException("ShippingCompany not found"));
    }

    /**
     * A search method to find all shipping companies in the database
     * @return
     *  A list of all ShippingCompanies
     */
    public List<ShippingCompany> findAll() {return shippingCompanyRepository.findAll();}

    /**
     * A search method to find a specific shipping company by its name
     * @param companyName
     *  The name of the ShippingCompany
     * @return
     *  The ShippingCompany with the matching name
     */
    public Optional<ShippingCompany> findByShippingCompanyName(String companyName) {return shippingCompanyRepository.findByShippingCompanyName(companyName);}

    /**
     * A search method to find a specific shipping company by its email
     * @param companyEmail
     *  The email of the ShippingCompany
     * @return
     *  The ShippingCompany with the matching email
     */
    public Optional<ShippingCompany> findByShippingCompanyEmail(String companyEmail) {return shippingCompanyRepository.findByShippingCompanyEmail(companyEmail);}

    /**
     * A search method to find a specific shipping company by its phone number
     * @param companyPhone
     *  The phone number of the ShippingCompany
     * @return
     *  The shippingCompany with the matching phone number
     */
    public Optional<ShippingCompany> findByShippingCompanyPhone(String companyPhone) {return shippingCompanyRepository.findByShippingCompanyPhone(companyPhone);}

    /*==================================LOGIC METHODS==================================*/

    /**
     * A logic method that creates a new ShippingCompany
     * @param shippingCompany
     *  The ShippingCompany object
     */
    public void addShippingCompany(ShippingCompany shippingCompany) {shippingCompanyRepository.save(shippingCompany);}

    /**
     * A logic method that updates an existing ShippingCompany
     * @param companyId
     *  The ID of the ShippingCompany being updated
     * @param companyRequest
     *  The new JSON packet with updated information
     */
    public void updateShippingCompany(Integer companyId, ShippingCompanyRequest companyRequest) {
        ShippingCompany company = findByShippingCompanyId(companyId);
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
        ShippingCompany company = findByShippingCompanyId(companyId);
        shippingCompanyRepository.delete(company);
    }
}
