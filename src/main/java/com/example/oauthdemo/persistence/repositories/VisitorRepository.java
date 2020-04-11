package com.example.oauthdemo.persistence.repositories;

import com.example.oauthdemo.persistence.entities.Visitor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
    Optional<Visitor> findByUsername(String username);
}