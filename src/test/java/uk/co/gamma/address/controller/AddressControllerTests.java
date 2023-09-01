package uk.co.gamma.address.controller;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.gamma.address.exception.AddressNotFoundException;
import uk.co.gamma.address.model.Address;
import uk.co.gamma.address.service.AddressService;
import uk.co.gamma.address.service.BlackListService;

@ExtendWith(MockitoExtension.class)
class AddressControllerTests {

    @Mock
    private AddressService addressService;

    @Mock
    private BlackListService blackListService;

    @InjectMocks
    private AddressController addressController;

    @DisplayName("list() - Given no addresses, then an empty list is returned")
    @Test
    void list_when_noAddresses_then_returnEmptyList() {

        given(addressService.getAll()).willReturn(List.of());

        List<Address> response = addressController.list(null);

        then(response).isEmpty();
    }

    @DisplayName("list() - Given addresses, then the full list is returned")
    @Test
    void list_when_multipleAddresses_then_allAddressesReturned() {

        List<Address> expected = List.of(
                new Address(1, "King's House", "Kings Road West", "Newbury", "RG14 5BY"),
                new Address(2, "The Malthouse", "Elevator Road", "Manchester", "M17 1BR"),
                new Address(3, "Holland House", "Bury Street", "London", "EC3A 5AW")
        );

        given(addressService.getAll()).willReturn(expected);

        List<Address> actual = addressController.list(null);

        then(actual).containsExactlyElementsOf(expected);
    }

    @DisplayName("list(postcode) - Given addresses are present with postcode, then the matching list is returned")
    @Test
    void list_when_matchingAddresses_then_matchingAddressesReturned() {

        List<Address> expected = List.of(
                new Address(1, "King's House", "Kings Road West", "Newbury", "RG14 5BY")
        );

        given(addressService.getByPostcode("RG14 5BY")).willReturn(expected);

        List<Address> actual = addressController.list("RG14 5BY");

        then(actual).containsExactlyElementsOf(expected);
    }

    @DisplayName("get(id) - Given an address is not present, then an AddressNotFoundException is thrown")
    @Test
    void get_when_addressNotPresent_then_AddressNotFoundExceptionThrown() {

        given(addressService.getById(1)).willReturn(Optional.empty());

        thenExceptionOfType(AddressNotFoundException.class)
                .isThrownBy(() -> addressController.get(1))
                .hasFieldOrPropertyWithValue("message", "Address with ID 1 not found");
    }

    @DisplayName("get(id) - Given an address is present, then it is returned")
    @Test
    void get_when_addressPresent_then_valueReturned() {

        Address expected = new Address(1, "King's House", "Kings Road West", "Newbury", "RG14 5BY");

        given(addressService.getById(1)).willReturn(Optional.of(expected));

        Address actual = addressController.get(1);

        then(actual).isEqualTo(expected);
    }

    @DisplayName("post() - Given a new address is posted, then it is created")
    @Test
    void post_when_newAddressSubmitted_then_AddressCreated() {

        Address expected = new Address(1, "King's House", "Kings Road West", "Newbury", "RG14 5BY");

        addressController.post(expected);

        BDDMockito.then(addressService).should().create(expected);
    }

    @DisplayName("put(id) - Given an address is updated by ID, then it is updated")
    @Test
    void put_when_existingAddressModified_then_AddressUpdated() {

        Address expected = new Address(1, "King's House", "Kings Road West", "Newbury", "RG14 5BY");

        addressController.put(1, expected);

        BDDMockito.then(addressService).should().update(1, expected);
    }


    @DisplayName("delete(id) - Given an address is deleted by ID, then it is deleted")
    @Test
    void delete_when_existingAddressDeleted_then_AddressDeleted() {

        addressController.delete(1);

        BDDMockito.then(addressService).should().delete(1);
    }
}