package com.ecommerceboari.api.repository;

import com.ecommerceboari.api.model.Address;
import com.ecommerceboari.api.util.AddressCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @DisplayName("Save creates address when Successful")
    @Test
    void save_PersistAddress_WhenSuccessful() {
        Address addressToBeSaved = AddressCreator.createValidAddress();

        Address addressSaved = this.addressRepository.save(addressToBeSaved);

        Assertions.assertThat(addressSaved).isNotNull();

        Assertions.assertThat(addressSaved.getId()).isNotNull();

        Assertions.assertThat(addressSaved.getStreet()).isEqualTo(addressToBeSaved.getStreet());
    }

    @DisplayName("Save updates address when Successful")
    @Test
    void update_ReplaceAddress_WhenSuccessful() {
        Address addressToBeSaved = AddressCreator.createValidAddress();

        Address addressSaved = this.addressRepository.save(addressToBeSaved);

        addressSaved.setStreet("UpdatedStreet");

        Address addressUpdated = this.addressRepository.save(addressSaved);

        Assertions.assertThat(addressUpdated).isNotNull();

        Assertions.assertThat(addressUpdated.getId()).isNotNull();

        Assertions.assertThat(addressUpdated.getStreet()).isEqualTo(addressSaved.getStreet());
    }

    @DisplayName("Delete removes address when Successful")
    @Test
    void delete_RemovesAddress_WhenSuccessful() {
        Address addressToBeSaved = AddressCreator.createValidAddress();

        Address addressSaved = this.addressRepository.save(addressToBeSaved);

        this.addressRepository.delete(addressSaved);

        Optional<Address> addressOptional = this.addressRepository.findById(addressSaved.getId());

        Assertions.assertThat(addressOptional).isNotPresent();
    }
}