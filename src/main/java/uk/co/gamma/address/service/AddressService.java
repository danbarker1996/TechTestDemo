package uk.co.gamma.address.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.gamma.address.exception.AddressNotFoundException;
import uk.co.gamma.address.exception.CouldNotReadBlacklistException;
import uk.co.gamma.address.model.Address;
import uk.co.gamma.address.model.Zone;
import uk.co.gamma.address.model.db.entity.AddressEntity;
import uk.co.gamma.address.model.db.repository.AddressRepository;
import uk.co.gamma.address.model.mapper.AddressMapper;

/**
 * Address service is a Component class that returns  {@link Address}.
 */
@Component
public class AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final BlackListService blackListService;

    /**
     * Constructor.

     * @param addressRepository  {@link AddressRepository}.
     * @param addressMapper  {@link AddressMapper}
     * @param blackListService  {@link blackListService}
     */
    @Autowired
    AddressService(AddressRepository addressRepository, AddressMapper addressMapper, BlackListService blackListService) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.blackListService = blackListService;
    }

    /**
     * getAll get all the addresses of the system.
     * 
     * @param excludeInvalidPostcodes boolean to remove blacklisted addresses

     * @return List  {@link Address} . Empty if none found.
     */
    public List<Address> getAll(boolean excludeInvalidPostcodes) {
    	List<Address> addressList = addressMapper.entityToModel(addressRepository.findAll());
    	if (excludeInvalidPostcodes) {
    		return compareToBlacklist(addressList);
    	}
    	
        return addressList;
    }
    
    /**
     * getByPostcode find Addresses by their postcode.

     * @param postcode the postcode to search by.
     * @param excludeInvalidPostcodes boolean to remove blacklisted addresses

     * @return List of  {@link Address}. Empty list if not found.
     */
    public List<Address> getByPostcode(String postcode, boolean excludeInvalidPostcodes) {
    	List<Address> addresses = addressMapper.entityToModel(addressRepository.findByPostcode(postcode));
    	
    	if (excludeInvalidPostcodes) {
    		return compareToBlacklist(addresses);
    	}
    	
        return addresses;
    }

    /**
     * findById find an address by Id.

     * @param id to search on.

     * @return  {@link Address} Optional.
     */
    public Optional<Address> getById(Integer id) {
        return addressRepository.findById(id).map(addressMapper::entityToModel);
    }

    /**
     * create Address and save to db.

     * @param address   {@link Address} to save

     * @return  {@link Address}
     */
    public Address create(Address address) {
        logger.info("Adding new address: {}", address);
        return save(addressMapper.modelToEntity(address));
    }

    /**
     * update an Address.

     * @param id of Address

     * @param address  {@link Address}

     * @return {@link Address}
     */
    public Address update(Integer id, Address address) {
        return addressRepository.findById(id).map(addressEntity -> {
            logger.info("Updating existing address {}: {}", id, address);
            addressEntity.setBuilding(address.building());
            addressEntity.setStreet(address.street());
            addressEntity.setTown(address.town());
            addressEntity.setPostcode(address.postcode());
            return save(addressEntity);
        }).orElseThrow(() -> new AddressNotFoundException(id));

    }

    /**
     * delete an address by id.

     * @param id of Address to delete
     */
    public void delete(Integer id) {
        if (!addressRepository.existsById(id)) {
            throw new AddressNotFoundException(id);
        }
        logger.info("Deleting address {}", id);
        addressRepository.deleteById(id);
    }

    /**
     * save an  {@link Address}.

     * @param addressEntity  {@link AddressEntity}

     * @return  {@link Address}
     */
    private Address save(AddressEntity addressEntity) {
        return addressMapper.entityToModel(addressRepository.save(addressEntity));
    }
    
    /**
     * compare a list of addresses to blacklisted postcodes, and remove ones that match
     * 
     * @param list of addresses to compare to blacklist
     
     * @return list of addresses excluding any blacklisted postcodes
     * 
     * @exception CouldNotReadBlacklistException
     */
    private List<Address> compareToBlacklist(final List<Address> addresses) {
    	try {
			List<Zone> blacklistedZones = blackListService.getAll();
			
			for (Zone zone : blacklistedZones) {
				addresses.removeIf(address -> address.postcode().equalsIgnoreCase(zone.getPostCode()));
			}
			
			return addresses;
    	} catch (IOException e) {
    		/*
    		 * As we know the error is intermittent with the blacklist service, we can call the compareToBlacklist method
    		 * recursively - it will have an escape when the request is successful.
    		 */
    		logger.error("Error trying to get blacklist, retrying...");
    		return compareToBlacklist(addresses);
    		
    	} catch (InterruptedException e) {
    		logger.error("Error trying to read blacklist, throwing exception");
			throw new CouldNotReadBlacklistException();
		}
    }
}
