package org.example.inventory_manager_beta1.mainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Shipments.ShippingCompanyRequest;
import org.example.inventory_manager_beta1.entities.ShippingCompany;
import org.example.inventory_manager_beta1.services.ShippingCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShippingCompanyMethods {
    @Autowired
    ShippingCompanyService shippingCompanyService;

    /**
     * CREATE: A logic method that creates a new ShippingCompany entity
     * @param companyRequest
     *  The DTO containing the
     * @return
     */
    @PostMapping("/company/add")
    public Boolean addShippingCompany(@RequestBody ShippingCompanyRequest companyRequest) {
        ShippingCompany company = new ShippingCompany(companyRequest.getShippingCompanyName(), companyRequest.getShippingCompanyEmail(), companyRequest.getShippingCompanyPhone());
        shippingCompanyService.addShippingCompany(company);
        return true;
    }

    /**
     *
     * @param companyId
     * @return
     */
    @GetMapping("/company/{companyId}")
    public ShippingCompany getShippingCompany(@PathVariable Integer companyId) {
        return shippingCompanyService.findByShippingCompanyId(companyId);
    }

    /**
     * READ:
     * @return
     */
    @GetMapping("/companies")
    public List<ShippingCompany> getShippingCompanies() {return shippingCompanyService.findAll();}

    /**
     * UPDATE:
     * @param companyId
     * @param companyRequest
     * @return
     */
    @PutMapping("/company/{companyId}/update")
    public Boolean updateShippingCompany(@PathVariable Integer companyId, @RequestBody ShippingCompanyRequest companyRequest) {
        shippingCompanyService.updateShippingCompany(companyId, companyRequest);
        return true;
    }

    /**
     * DELETE
     * @param companyId
     * @return
     */
    @DeleteMapping("/company/{companyId}/delete")
    public Boolean deleteShippingCompany(@PathVariable Integer companyId) {
        shippingCompanyService.deleteShippingCompany(companyId);
        return true;
    }
}
