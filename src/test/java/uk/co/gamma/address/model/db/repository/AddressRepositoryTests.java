package uk.co.gamma.address.model.db.repository;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenExceptionOfType;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import uk.co.gamma.address.model.db.entity.AddressEntity;

@DataJpaTest
class AddressRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setup() {
        addressRepository.deleteAll();
    }

    @DisplayName("findAll() - Given no addresses, then an empty list is returned")
    @Test
    void findAll_when_noAddresses_then_emptyList() {

        List<AddressEntity> actual = addressRepository.findAll();

        then(actual).isEmpty();
    }

    @DisplayName("findAll() - Given one address, then a singleton list is returned")
    @Test
    void findAll_when_oneAddress_then_singletonList() {

        AddressEntity expected = new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY");

        entityManager.persist(expected);

        List<AddressEntity> actual = addressRepository.findAll();

        then(actual).containsExactly(expected);
    }

    @DisplayName("findAll() - Given multiple addresses, then a list of all addresses is returned")
    @Test
    void findAll_when_multipleAddresses_then_list() {

        List<AddressEntity> expected = List.of(
                new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY"),
                new AddressEntity("The Malthouse", "Elevator Road", "Manchester", "M17 1BR"),
                new AddressEntity("Holland House", "Bury Street", "London", "EC3A 5AW")
        );

        expected.forEach(address -> entityManager.persist(address));

        List<AddressEntity> actual = addressRepository.findAll();

        then(actual).containsExactlyElementsOf(expected);
    }

    @DisplayName("findByPostcode(postcode) - Given no addresses with postcode, then an empty list is returned")
    @Test
    void findByPostcode_when_noAddresses_then_emptyList() {

        entityManager.persist(new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY"));

        List<AddressEntity> actual = addressRepository.findByPostcode("RG14 5BZ");

        then(actual).isEmpty();
    }

    @DisplayName("findByPostcode(postcode) - Given one address with postcode, then a singleton list is returned")
    @Test
    void findByPostcode_when_oneAddress_then_singletonList() {

        AddressEntity expected = new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY");

        entityManager.persist(expected);

        List<AddressEntity> actual = addressRepository.findByPostcode("RG14 5BY");

        then(actual).containsExactly(expected);
    }

    @DisplayName("findByPostcode(postcode) - Given multiple addresses with postcode, then a matching list is returned")
    @Test
    void findByPostcode_when_multipleAddresses_then_matchingList() {

        List<AddressEntity> expected = List.of(
                new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY"),
                new AddressEntity("Queen's House", "Kings Road West", "Newbury", "RG14 5BY")
        );

        List<AddressEntity> notExpected = List.of(
                new AddressEntity("The Malthouse", "Elevator Road", "Manchester", "M17 1BR"),
                new AddressEntity("Holland House", "Bury Street", "London", "EC3A 5AW")
        );

        expected.forEach(address -> entityManager.persist(address));
        notExpected.forEach(address -> entityManager.persist(address));

        List<AddressEntity> actual = addressRepository.findByPostcode("RG14 5BY");

        then(actual).containsExactlyElementsOf(expected);
    }

    @DisplayName("findById(id) - Given an address is not present with the ID, then an empty optional is returned")
    @Test
    void findById_when_addressNotPresent_then_empty() {

        AddressEntity expected = new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY");

        entityManager.persist(expected);

        Optional<AddressEntity> actual = addressRepository.findById(expected.getId() + 1);

        then(actual).isEmpty();
    }

    @DisplayName("findById(id) - Given an address is present with the ID, then it is returned")
    @Test
    void findById_when_addressPresent_then_address() {

        AddressEntity expected = new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY");

        entityManager.persist(expected);

        Optional<AddressEntity> actual = addressRepository.findById(expected.getId());

        then(actual).hasValue(expected);
    }

    @DisplayName("existsById(id) - Given an address is not present with the ID, then false is returned")
    @Test
    void existsById_when_addressNotPresent_then_false() {

        AddressEntity expected = new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY");

        entityManager.persist(expected);

        boolean actual = addressRepository.existsById(expected.getId() + 1);

        then(actual).isFalse();
    }

    @DisplayName("existsById(id) - Given an address is present with the ID, then true is returned")
    @Test
    void existsById_when_addressPresent_then_true() {

        AddressEntity expected = new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY");

        entityManager.persist(expected);

        boolean actual = addressRepository.existsById(expected.getId());

        then(actual).isTrue();
    }

    @DisplayName("save(address) - Given an address is saved, then it is persisted and returned")
    @Test
    void save_when_addressSaved_then_addressPersistedAndReturned() {

        AddressEntity expected = new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY");

        AddressEntity actual = addressRepository.save(expected);

        //TODO: more assertions
        then(actual).isEqualTo(expected);
    }

    @DisplayName("save(address) - Given an invalid address provided, then DataIntegrityViolationException is thrown")
    @Test
    void save_when_invalidAddressSaved_then_DataIntegrityViolationExceptionThrown() {

        thenExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> addressRepository.save(new AddressEntity(null, null, null, null)));
    }


    @DisplayName("deleteById(address) - Given an address is present, then it is deleted")
    @Test
    void deleteById_when_addressDeleted_then_addressDeleted() {

        AddressEntity expected = new AddressEntity("King's House", "Kings Road West", "Newbury", "RG14 5BY");

        entityManager.persist(expected);

        addressRepository.deleteById(expected.getId());

        then(addressRepository.findById(expected.getId())).isEmpty();
    }
}
