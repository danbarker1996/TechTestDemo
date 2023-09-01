package uk.co.gamma.address.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.co.gamma.address.exception.AddressNotFoundException;
import uk.co.gamma.address.model.Address;
import uk.co.gamma.address.service.AddressService;
import uk.co.gamma.address.service.BlackListService;

@RestController
@RequestMapping(value = "/addresses", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AddressController {

    private final AddressService addressService;
    private final BlackListService blackListService;

    @Autowired
    public AddressController(AddressService addressService, BlackListService blackListService) {
        this.addressService = addressService;
        this.blackListService = blackListService;
    }

    @ApiResponse(responseCode = "200", description = "Returns list of all addresses", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Address.class))))
    @GetMapping
    public List<Address> list(@RequestParam(value = "postcode", required = false) String postcode) {
        if (StringUtils.isNotBlank(postcode)) {
            return addressService.getByPostcode(postcode);
        }
        return addressService.getAll();
    }

    @ApiResponse(responseCode = "200", description = "Address returned", content = @Content(schema = @Schema(implementation = Address.class)))
    @GetMapping("/{id}")
    public Address get(@PathVariable Integer id) {
        return addressService.getById(id).orElseThrow(() -> new AddressNotFoundException(id));
    }

    @ApiResponse(responseCode = "201", description = "Address successfully created", content = @Content(schema = @Schema(implementation = Address.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Address post(@Valid @RequestBody Address address) {

        return addressService.create(address);
    }

    @ApiResponse(responseCode = "200", description = "Address successfully amended", content = @Content(schema = @Schema(implementation = Address.class)))
    @PutMapping("/{id}")
    public Address put(@PathVariable Integer id, @Valid @RequestBody Address address) {
        return addressService.update(id, address);
    }

    @ApiResponse(responseCode = "204", description = "Address successfully deleted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        addressService.delete(id);
    }
}