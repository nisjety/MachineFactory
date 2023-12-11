package com.example.maschinefactory.address;

import com.example.maschinefactory.address.AddressRepository;
import com.example.maschinefactory.customer.Customer;
import com.example.maschinefactory.customer.CustomerNotFoundException;
import com.example.maschinefactory.customer.InvalidCustomerDataException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;


    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Page<Address> findAllAddress(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }


    public Optional<Address> findAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public Page<Address> findAddressByStreet(String street, Pageable pageable) {
        return addressRepository.findByStreet(street, pageable);
    }

    public Page<Address> findAddressByCity(String city, Pageable pageable) {
        return addressRepository.findByCity(city, pageable);
    }

    public Page<Address> findAddressByZip(int zip, Pageable pageable) {
        return addressRepository.findByZip(zip, pageable);
    }

    public Page<Address> findAddressByStreetAndCity(String street, String city, Pageable pageable) {
        return addressRepository.findByStreetAndCity(street, city, pageable);
    }
    public Page<Address> findAddressByStreetAndZip(String street, int zip, Pageable pageable) {
        return addressRepository.findByStreetAndZip(street, zip, pageable);
    }

    public Page<Address> findAddressByStreetAndCityAndZip(String street, String city, int zip, Pageable pageable) {
        return addressRepository.findByStreetAndCityAndZip(street, city, zip, pageable);
    }

    public Page<Address> findAddressByCityAndZip(String city, int zip, Pageable pageable) {
        return addressRepository.findByCityAndZip(city, zip, pageable);
    }

    public Optional<Address> findAddressByCountry(String country) {
        return addressRepository.findByCountry(country);
    }
    public Optional<Address> findByCountryAndZip(String country, int zip) {
        return addressRepository.findByCountryAndZip(country, zip);
    }

    public Optional<Address> findAddressByCountryAndCity(String country, String city) {
        return addressRepository.findByCountryAndCity(country, city);
    }

}
