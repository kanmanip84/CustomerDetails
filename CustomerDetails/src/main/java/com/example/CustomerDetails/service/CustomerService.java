package com.example.CustomerDetails.service;

import com.example.CustomerDetails.entity.Customer;
import com.example.CustomerDetails.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;
    public Customer saveCustomer(Customer customer) {
        return repository.save(customer);
    }
    public List<Customer> saveCustomers(List<Customer> customers) {return repository.saveAll(customers);}
    public List<Customer> getCustomers() {return repository.findAll();}
    public Customer getCustomerById(int id){return repository.findById(id).orElse(null);}
    public Customer getCustomerByName(String name){return repository.findByName(name);}
    public List<Customer> getCustomersByNameStartingWith(String startingLetter)
    {
        return repository.findByNameStartingWith(startingLetter);
    }
    public Customer updateCustomer(Customer customer)
    {
        Customer existingCustomer=repository.findById(customer.getId()).orElse(null);
        existingCustomer.setName(customer.getName());
        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
        existingCustomer.setAddress1(customer.getAddress1());
        existingCustomer.setAddress2(customer.getAddress2());
        existingCustomer.setEmailID(customer.getEmailID());
        existingCustomer.setPanCard(customer.getPanCard());
        existingCustomer.setAadhar(customer.getAadhar());
        existingCustomer.setDob(customer.getDob());
        existingCustomer.setGender(customer.getGender());
        return repository.save(existingCustomer);
    }

}
