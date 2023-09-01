package uk.co.gamma.address.controller;

import static org.assertj.core.api.BDDAssertions.then;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import uk.co.gamma.address.model.Address;
import uk.co.gamma.address.model.db.entity.AddressEntity;
import uk.co.gamma.address.model.db.repository.AddressRepository;

public class AddressControllerStepDefs {

    private static final String baseUrl = "/addresses";

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected AddressRepository addressRepository;

    private ResponseEntity<?> latestResponse;

    private Integer latestAddressId;

    @After
    public void cleanUp() {
        addressRepository.deleteAll();
    }

    @Given("the collection of addresses")
    public void setupAddresses(List<AddressEntity> addresses) {
        latestAddressId = addressRepository.saveAll(addresses).stream().mapToInt(AddressEntity::getId).max().orElse(0);
    }

    @When("user asks for all addresses")
    public void getAllAddresses() {
        latestResponse = restTemplate.getForEntity(baseUrl, Address[].class);
    }

    @When("user asks for all addresses with postcode {string}")
    public void getAddressesByPostcode(String postcode) {
        latestResponse = restTemplate.exchange(baseUrl.concat("?postcode={postcode}"), HttpMethod.GET, null, Address[].class, postcode);
    }

    @When("user asks for the address with the latest ID")
    public void getLatestAddress() {
        latestResponse = restTemplate.getForEntity(baseUrl.concat("/" + latestAddressId), Address.class);
    }

    @When("user asks for the address with an invalid ID")
    public void getInvalidAddress() {
        latestResponse = restTemplate.getForEntity(baseUrl.concat("/" + (latestAddressId + 1)), Address.class);
    }

    @When("user posts an address")
    public void createAddress(Address address) {
        ResponseEntity<Address> response = restTemplate.postForEntity(baseUrl, address, Address.class);
        latestAddressId = response.getBody().id();
        latestResponse = response;
    }

    @When("user updates the address with the latest ID")
    public void updateLatestAddress(Address address) {
        latestResponse = restTemplate.exchange(baseUrl.concat("/" + latestAddressId), HttpMethod.PUT, new HttpEntity<>(address), Void.class);
    }

    @When("user updates the address with an invalid ID")
    public void updateInvalidAddress(Address address) {
        latestResponse = restTemplate.exchange(baseUrl.concat("/" + (latestAddressId + 1)), HttpMethod.PUT, new HttpEntity<>(address), Void.class);
    }

    @When("user deletes the address with the latest ID")
    public void deleteLatestAddress() {
        latestResponse = restTemplate.exchange(baseUrl.concat("/" + latestAddressId), HttpMethod.DELETE, null, Void.class);
    }

    @When("user deletes the address with an invalid ID")
    public void deleteInvalidAddress() {
        latestResponse = restTemplate.exchange(baseUrl.concat("/" + (latestAddressId + 1)), HttpMethod.DELETE, null, Void.class);
    }

    @Then("user receives status code of {int}")
    public void clientReceivesStatusCode(int code) {
        then(latestResponse.getStatusCodeValue()).isEqualTo(code);
    }

    @Then("collection of addresses returned")
    public void assertAddresses(List<Address> expected) {
        Object actual = latestResponse.getBody();

        then(actual).isInstanceOf(Address[].class);
        then((Address[]) actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").containsAll(expected);
    }

    @Then("address returned")
    public void assertAddress(Address expected) {
        Object actual = latestResponse.getBody();

        then(actual).isInstanceOf(Address.class);
        then((Address) actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }
}