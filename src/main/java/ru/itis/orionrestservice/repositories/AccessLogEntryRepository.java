package ru.itis.orionrestservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.orionrestservice.models.AccessLogEntry;

public interface AccessLogEntryRepository extends JpaRepository<AccessLogEntry, Long> {
}
