package com.LibGO.Library.repository;

import com.LibGO.Library.model.Due;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DueRepository extends JpaRepository<Due,Long> {

}
