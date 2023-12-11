package com.example.maschinefactory.address;


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

    public void validateAddressCredentials(String city, String street, String zip, String country) {
        Address address = (addressRepository.findByCity(city),
        addressRepository.findByStreet(street),
        addressRepository.findByZip(zip),
        addressRepository.findByCountry(country)
        } AddressNotFoundException::new


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
        if (address.getZip() == null || address.getZip().isEmpty()) {
            throw new InvalidAddressDataException();
        }
        if (address.getCountry() == null || address.getCountry().isEmpty()) {
            throw new InvalidAddressDataException();
        }


        return true;
    }
}


