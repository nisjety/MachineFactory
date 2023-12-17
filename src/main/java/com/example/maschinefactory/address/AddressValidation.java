package com.example.maschinefactory.address;

import org.springframework.stereotype.Component;

@Component
public class AddressValidation {
    private final AddressRepository addressRepository;

    // Constructor
    public AddressValidation(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void validateExistingAddress(Long addressId) {
        addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException());
    }

    public boolean validateAddressData(Address address) throws InvalidAddressDataException {
        if (address == null) {
            throw new InvalidAddressDataException("Address is null");
        }

        if (address.getStreet() == null || address.getStreet().isEmpty()) {
            throw new InvalidAddressDataException("Street is invalid");
        }

        if (address.getCity() == null || address.getCity().isEmpty()) {
            throw new InvalidAddressDataException("City is invalid");
        }

        if (address.getCountry() == null || address.getCountry().isEmpty()) {
            throw new InvalidAddressDataException("Country is invalid");
        }

        // If all checks pass, return true indicating the address is valid
        return true;
    }

}
