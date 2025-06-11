package org.example.hotelexplorer.repository;

import org.example.hotelexplorer.entity.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactsRepository extends JpaRepository<Contacts, Long> {
}