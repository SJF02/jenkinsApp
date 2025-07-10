package org.suhodo.cardatabase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.suhodo.cardatabase.domain.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

}
