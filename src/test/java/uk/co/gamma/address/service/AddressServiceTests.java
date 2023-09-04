package uk.co.gamma.address.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import uk.co.gamma.address.model.Address;
import uk.co.gamma.address.model.Zone;
import uk.co.gamma.address.model.db.entity.AddressEntity;
import uk.co.gamma.address.model.db.repository.AddressRepository;
import uk.co.gamma.address.model.mapper.AddressMapper;

@ExtendWith(MockitoExtension.class)
class AddressServiceTests {

    @Spy
    private final AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private BlackListService blackListService;
    @InjectMocks
    private AddressService addressService;

    @DisplayName("getAll() - Given no addresses, then an empty list is returned")
    @Test
    void getAll_when_noAddresses_then_emptyListReturned() {

        given(addressRepository.findAll()).willReturn(List.of());

        List<Address> actual = addressService.getAll(false);

        then(actual).isEmpty();
    }

    @DisplayName("getAll() - Given addresses, then the full list is returned")
    @Test
    void getAll_when_multipleAddresses_then_allAddressesReturned() {

        List<AddressEntity> expected = List.of(
                new AddressEntity(1, "King's House", "Kings Road West", "Newbury", "RG14 5BY"),
                new AddressEntity(2, "The Malthouse", "Elevator Road", "Manchester", "M17 1BR"),
                new AddressEntity(3, "Holland House", "Bury Street", "London", "EC3A 5AW")
        );

        given(addressRepository.findAll()).willReturn(expected);

        List<Address> actual = addressService.getAll(false);

        // verify
        then(actual).usingRecursiveComparison().isEqualTo(expected);
    }
    
    @DisplayName("getAll() - Given addresses and exclude blacklisted flag is true, then list is returned without blacklisted addresses")
    @Test
    void getAll_when_multipleAddresses_and_blacklistIsTrue_then_allAddressesReturned() throws Exception {
    	List<AddressEntity> allAddresses = List.of(
                new AddressEntity(1, "King's House", "Kings Road West", "Newbury", "RG14 5BY"),
                new AddressEntity(2, "The Malthouse", "Elevator Road", "Manchester", "M17 1BR"),
                new AddressEntity(3, "Holland House", "Bury Street", "London", "EC3A 5AW")
        );
    	
    	List<Zone> blackList = List.of(new Zone("RG14 5BY"));
    	
    	List<Address> expectedAfterBlacklist = List.of(
                new Address(2, "The Malthouse", "Elevator Road", "Manchester", "M17 1BR"),
                new Address(3, "Holland House", "Bury Street", "London", "EC3A 5AW")
        );
    	
    	given(addressRepository.findAll()).willReturn(allAddresses);
    	
    	given(blackListService.getAll()).willReturn(blackList);
    	
    	List<Address> actual = addressService.getAll(true);

        // verify
        then(actual).usingRecursiveComparison().isEqualTo(expectedAfterBlacklist);
    }
}
