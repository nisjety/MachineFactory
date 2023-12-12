package com.example.maschinefactory.address;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component

public class AddressValidation {
    private final AddressRepository addressRepository;

    // Constructor
    public AddressValidation(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void validateExistingAddress(Long addressId, Address updatedAddress) {
        addressRepository.findById(addressId)
                .orElseThrow(AddressNotFoundException::new);
    }

    public boolean validateAddressCredentials(Long addressId, String street, String city, int zip, String country) {
        // Check if any addresses exist with the given city, street, zip, and country
        boolean streetExists = !addressRepository.findByStreet(street, PageRequest.of(0, 1)).isEmpty();
        boolean cityExists = !addressRepository.findByCity(city, PageRequest.of(0, 1)).isEmpty();
        boolean zipExists = !addressRepository.findByZip(zip, PageRequest.of(0, 1)).isEmpty();
        boolean countryExists = addressRepository.findByCountry(country).isPresent();

        if (!(cityExists && streetExists && zipExists && countryExists)) {
            throw new AddressNotFoundException();
        }
        return true;
    }


    public boolean validateAddressData(Address address) throws InvalidAddressDataException {
        if (address == null) {
            throw new InvalidAddressDataException();
        }

        if (address.getStreet() == null || address.getStreet().isEmpty()) {
            throw new InvalidAddressDataException();
        }
        if (address.getCity() == null || address.getCity().isEmpty()) {
            throw new InvalidAddressDataException();
        }

        if (address.getCountry() == null || address.getCountry().isEmpty()) {
            throw new InvalidAddressDataException();
        }

        return true;
    }
}



