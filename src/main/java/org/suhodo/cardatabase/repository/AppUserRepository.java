package org.suhodo.cardatabase.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.suhodo.cardatabase.domain.AppUser;

// 이 리포지터리는 RestController의 역할을 하지 않고,
// 리포지터리의 기능만 수행하겠다.
@RepositoryRestResource(exported = false)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // username으로 정보 조회하는 Query Method
    Optional<AppUser> findByUsername(String username);
}
