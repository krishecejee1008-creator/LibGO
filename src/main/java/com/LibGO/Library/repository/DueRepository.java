package com.LibGO.Library.repository;

import com.LibGO.Library.model.Due;
import com.LibGO.Library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DueRepository extends JpaRepository<Due,Long> {

    List<Due> findDueByDueIssuer(User dueIssuer);

}
