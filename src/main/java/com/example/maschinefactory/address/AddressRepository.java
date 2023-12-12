package com.example.maschinefactory.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findByStreet(String street, Pageable pageable) ;
    Page<Address> findByCity(String city, Pageable pageable) ;
    Page<Address> findByZip(int zip, Pageable pageable) ;
    Optional<Address> findByCountry(String country) ;
    Page<Address> findByStreetAndCity(String street, String city, Pageable pageable) ;
    Page<Address> findByStreetAndZip(String street, int zip, Pageable pageable) ;
    Page<Address> findByStreetAndCityAndZip(String street, String city, int zip, Pageable pageable) ;
    Page<Address> findByCityAndZip(String city, int zip, Pageable pageable) ;
    Optional<Address> findByCountryAndZip(String country, int zip) ;
    Optional<Address> findByCountryAndCity(String country, String city) ;
}
