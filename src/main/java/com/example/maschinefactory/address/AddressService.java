package com.example.maschinefactory.address;

import com.example.maschinefactory.address.AddressRepository;
import com.example.maschinefactory.customer.Customer;
import com.example.maschinefactory.customer.CustomerNotFoundException;
import com.example.maschinefactory.customer.InvalidCustomerDataException;
import com.example.maschinefactory.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
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

    public Customer createAddress(Address address) throws InvalidAddressDataException {
        if (addressValidation.validateAddressData(address)) {
            return addressRepository.save(address);
        } else {
            try {
                throw new InvalidAddressDataException();
            } catch (InvalidAddressDataException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Transactional
    public Address updateAddress(Long addressId, Address address) throws InvalidAddressDataException {
        if (addressValidation.validateCustomerData(address)) {
            Optional<Address> existing = addressRepository.findById(addressId);
            if (existing.isPresent()) {
                Address updated = new Address(
                        existing.get().getAddressId(),
                        address.getZip(),
                        address.getStreet(),
                        address.getCity(),
                        address.getCountry()

                );
                return addressRepository.save(updated);
            } else {
                throw new AddressNotFoundException();
            }
        } else {
            try {
                throw new InvalidAddressDataException();
            } catch (InvalidAddressDataException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Customer> getCustomersFromAddress(Long addressId) {
        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()) {
            return new ArrayList<>(address.get().getCustomers());
        } else {
            throw new AddressNotFoundException();
        }
    }

    public List<Order> getOrdersForAddress(Long addressId) {
        Optional<Address> address = addressRepository.findById(addressId);

        if (address.isPresent()) {
            return new ArrayList<>(address.get().getOrders());
        } else {
            throw new AddressNotFoundException();
        }
    }



    @Transactional
    public Address addOrderToAddress(Long addressId, Order order) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(AddressNotFoundException::new);
        address.getOrders().add(order);
        return addressRepository.save(address);
    }

    @Transactional
    public Address addCustomerToAddress(Long addressId, Customer customer) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(AddressNotFoundException::new);
        Address.getCustomers().add(customer);
        return addressRepository.save(address);
    }

    @Transactional
    public void removeOrderFromAddress(Long addressId, long orderId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(CustomerNotFoundException::new);
        address.getOrders().removeIf(order -> order.getOrderId() == orderId);
    }

    @Transactional
    public void removeCustomerFromAddress(Long addressId, long customerId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(AddressNotFoundException::new);
        address.getCustomers().removeIf(customer -> customer.getCustomerId() == customerId);
    }
}
