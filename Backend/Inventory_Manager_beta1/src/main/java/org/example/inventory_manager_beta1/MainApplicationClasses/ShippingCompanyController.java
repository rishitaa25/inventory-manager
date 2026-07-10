package org.example.inventory_manager_beta1.MainApplicationClasses;

import org.example.inventory_manager_beta1.DTO.Shipments.ShippingCompanyRequest;
import org.example.inventory_manager_beta1.Entities.ShippingCompany;
import org.example.inventory_manager_beta1.Services.ShippingCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * A controller class containing all the different HTTP mappings for ShippingCompany related methods
 */
@RestController
@RequestMapping("/shipping-company")
public class ShippingCompanyController {
    @Autowired
    ShippingCompanyService shippingCompanyService;

    /**
     * CREATE: A logic method that creates a new ShippingCompany entity
     * @param companyRequest
     *  The DTO containing the
     * @return
     * True if completed
     */
    @PostMapping("/add")
    public Boolean addShippingCompany(@RequestBody ShippingCompanyRequest companyRequest) {
        shippingCompanyService.addShippingCompany(companyRequest);
        return true;
    }

    /**
     * READ: A controller method for handling searching for a specific ShippingCompany
     * @param companyId
     *  The company ID of the specific ShippingCompany
     * @return
     *  The specific ShippingCompany
     */
    @GetMapping("/find-by/id/{companyId}")
    public ShippingCompany getShippingCompanyViaId(@PathVariable("companyId") Integer companyId) {return shippingCompanyService.findById(companyId);}

    /**
     * READ: A controller method for handling searching for a specific ShippingCompany
     * @param companyName
     *  The name of the ShippingCompany
     * @return
     *  The ShippingCompany with the matching name if it exists
     */
    @GetMapping("/find-by/name/{companyName}")
    public ShippingCompany getShippingCompanyViaName(@PathVariable("companyName") String companyName) {return shippingCompanyService.findByShippingCompanyName(companyName);}

    /**
     * READ: A controller method for handling searching for a specific ShippingCompany
     * @param companyEmail
     *  The email of the ShippingCompany
     * @return
     *  The ShippingCompany with the matching email if it exists
     */
    @GetMapping("/find-by/email/{companyEmail}")
    public ShippingCompany getShippingCompanyViaEmail(@PathVariable("companyEmail") String companyEmail) {return shippingCompanyService.findByShippingCompanyEmail(companyEmail);}

    /**
     * READ: A controller method for handling searching for a specific ShippingCompany
     * @param companyPhoneNumber
     *  The phone number of the ShippingCompany
     * @return
     *  The ShippingCompany with the matching phone number if it exists
     */
    @GetMapping("/find-by/phone-number/{companyPhoneNumber}")
    public ShippingCompany getShippingCompanyViaPhoneNumber(@PathVariable("companyPhoneNumber") String companyPhoneNumber) {return shippingCompanyService.findByShippingCompanyPhone(companyPhoneNumber);}

    /**
     * READ: A controller method for handling searching for all ShippingCompanies
     * @return
     *  A List of all existing ShippingCompanies
     */
    @GetMapping("/find/all")
    public List<ShippingCompany> getShippingCompanies() {return shippingCompanyService.findAll();}

    /**
     * UPDATE: A controller method for handling updating the information for
     * a specific ShippingCompany
     * @param companyId
     *  The ID of the ShippingCompany being updated
     * @param companyRequest
     *  The DTO containing the new information about the ShippingCompany
     * @return
     *  True if completed
     */
    @PutMapping("/update/{companyId}")
    public Boolean updateShippingCompany(@PathVariable Integer companyId, @RequestBody ShippingCompanyRequest companyRequest) {
        shippingCompanyService.updateShippingCompany(companyId, companyRequest);
        return true;
    }

    /**
     * DELETE: A controller method for handling deleting a ShippingCompany
     * @param companyId
     *  The ID of the ShippingCompany being deleted
     * @return
     *  True if completed
     */
    @DeleteMapping("/delete/{companyId}")
    public Boolean deleteShippingCompany(@PathVariable Integer companyId) {
        shippingCompanyService.deleteShippingCompany(companyId);
        return true;
    }
}
