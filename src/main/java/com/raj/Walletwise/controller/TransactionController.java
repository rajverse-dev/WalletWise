package com.raj.Walletwise.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.raj.Walletwise.model.Transaction;
import com.raj.Walletwise.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionRepository repository;

    @GetMapping
    public List<Transaction> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Transaction add(@RequestBody Transaction transaction) {
        // Validation check before saving
        if (transaction.getDescription() == null || transaction.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty!");
        }
        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero!");
        }
        return repository.save(transaction);
    }

    // NEW: HTTP DELETE Endpoint to remove a transaction by its ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
        }
    }
}

// Simple custom runtime exception for missing data rows
@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
