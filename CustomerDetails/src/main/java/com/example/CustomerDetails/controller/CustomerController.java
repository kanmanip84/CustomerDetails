package com.example.CustomerDetails.controller;

import com.example.CustomerDetails.entity.Customer;
import com.example.CustomerDetails.repository.CustomerRepository;
import com.example.CustomerDetails.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.io.*;
import java.util.*;


@RestController

public class CustomerController {
    @Autowired
    private CustomerService service;
    @Autowired
    private CustomerRepository repository;
    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/addCustomer")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
        String msg = "This method is used to add a Customer" +
                     "Before adding customer it will Validate the request body " +
                     " if captured any input validation errors,send a response with the error messages in the response body";
        createFile(msg);
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Customer savedCustomer = repository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @PostMapping("/addCustomers")
    public List<Customer> addCustomers(@RequestBody List<Customer> customers) {
        String msg = "This method is used to add multiple Customers";
        createFile(msg);
        return service.saveCustomers(customers);
    }

    @GetMapping("/customers")
    public List<Customer> findAllCustomers() {
        String msg = "This method is used to getting all the Customers";
        createFile(msg);
        return service.getCustomers();
    }

    @GetMapping("/customerById/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") String idString) {
        String msg = "This method is used to get the customer by id";
        createFile(msg);
        Customer customer = new Customer();
        try {
            int ids = Integer.parseInt(idString);//check format of id
            Optional<Customer> optionalCustomer = repository.findById(ids);
            if (!optionalCustomer.isPresent()) {//check given id availability
                return ResponseEntity.badRequest().body("Customer ID does not exist.");
            } else {
                customer = service.getCustomerById(ids);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid id format...Accept only integer");
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/customerByName/{name}")
    public ResponseEntity<Object> getCustomerByName(@PathVariable String name) {
        String msg = "This method is used to get the customer by name";
        createFile(msg);
        if (!name.matches("[a-zA-Z]+")) {//check whether the entered search query is valid string or not
            return ResponseEntity.badRequest().body("Invalid search query. Please enter a valid string.");
        }
        Customer customer = new Customer();
        Optional<Customer> optionalCustomer = Optional.ofNullable(service.getCustomerByName(name));
        if (!optionalCustomer.isPresent()) {//check given name availability
            return ResponseEntity.badRequest().body("Customer Name does not exist.");
        } else {
            customer = service.getCustomerByName(name);
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/starting-with/{startingLetter}")
    public List<Customer> getCustomersByNameStartingWith(@PathVariable String startingLetter) {
        String msg = "This method is used to get the customer by name starting with" + startingLetter;
        createFile(msg);
        return service.getCustomersByNameStartingWith(startingLetter);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody Customer customer, BindingResult result){
        String msg = "This method is used to Update the existing customer" +
                     "Before updating the customer it will Validate the request body " +
                     "If captured any input validation errors,send a response with the error messages in the response body";
        createFile(msg);
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Customer updatedCustomer =service.updateCustomer(customer);
        return ResponseEntity.ok(customer);
    }

    @RequestMapping("/template/accounts")
    public String getAccounts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String msg = "This method is used to get the accounts from AccountMs using restTemplate";
        createFile(msg);
        return restTemplate.exchange("http://localhost:9191/accounts", HttpMethod.GET, entity, String.class).getBody();
    }

    @RequestMapping("/template/deleteByCustomerId/{id}")
    public String deleteCustomer(@PathVariable int id) {
        String msg = "This method will check whether the Entered customer is available or not." +
                     "if it is available,Before deleting the customer,this method will delete the accounts of particular customer from AccountMS";
        createFile(msg);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Optional<Customer> customer = repository.findById(id);
        if (!customer.isPresent()) {
            return ("Customer ID does not exist");
        } else {
            restTemplate.exchange("http://localhost:9191/deleteByCustomerId/" + id, HttpMethod.DELETE, entity, String.class).getBody();
            repository.deleteById(id);
            return ("Customer deleted successfully  " + id);
        }
    }

    @GetMapping("/template/customerById/{id}")
    public String getCustomerById(@PathVariable int id) {
       //this method is used to check the existing customer by using customer id from AccountMS
        Optional<Customer> accountOptional =repository.findById(id);
        if (!accountOptional.isPresent()) {
            return ("no");
        }
        else {
            return ("yes");
        }
    }

    private void createFile(String msg) {
        {
            try {
                File myObj = new File("C:\\Users\\admin\\IdeaProjects\\kanmani\\CreateFile.txt");
                myObj.createNewFile();
                System.out.println("File created: " + myObj.getName());

            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            try {
                FileWriter myWriter = new FileWriter("C:\\Users\\admin\\IdeaProjects\\kanmani\\CreateFile.txt", true);
                BufferedWriter bufferedWriter = new BufferedWriter(myWriter);
                PrintWriter printWriter = new PrintWriter(bufferedWriter);
                printWriter.println(msg + "\n");
                printWriter.flush();
                bufferedWriter.flush();
                myWriter.flush();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/log")
    private StringBuilder ReadFile() {
        StringBuilder fileRead = new StringBuilder();
        String line = " ";
        try {
            File myObj = new File("C:\\Users\\admin\\IdeaProjects\\kanmani\\CreateFile.txt");
            FileReader filereader = new FileReader(myObj);
            BufferedReader reader = new BufferedReader(filereader);
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                fileRead.append(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return (fileRead);
    }
}
