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
        return false;
    }
}
