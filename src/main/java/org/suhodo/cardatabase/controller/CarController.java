package org.suhodo.cardatabase.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.suhodo.cardatabase.domain.Car;
import org.suhodo.cardatabase.domain.Owner;
import org.suhodo.cardatabase.repository.CarRepository;
import org.suhodo.cardatabase.repository.OwnerRepository;

import lombok.RequiredArgsConstructor;

/*
 * Client : (브라우저상의) React App: Axios/Fetch 라이브러리 사용
 * Server : (톰캣을 연동한) Spring Boot App: @RestController 설정 클래스
 * 통신 : application/json 헤더에 설정
 * 데이터 : json 문자열(js객체를 문자열로 변환)
 */
@RestController
@RequiredArgsConstructor
public class CarController {

    private final OwnerRepository ownerRepository;
    private final CarRepository carRepository;

    @GetMapping("/cars")
    public List<Car> getCars(){
        return carRepository.findAll();
    }

    // ADMIN과 USER 모두 접근 가
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/car/{id}")
    public Car getCarById(@PathVariable("id") Long id) {
        Optional<Car> result = carRepository.findById(id);
        return result.orElse(null);
    }
    
    // ADMIN만 가능
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/owner/{id}")
    public Owner getOwnerById(@PathVariable("id") Long id) {
        Optional<Owner> result = ownerRepository.findById(id);
        return result.orElse(null);
    }
}

/*
 * [Rest Controller 생성하는 법]
 * 1. Class위에 @RestController을 붙인다.
 *    그리고 원하는 주소에 매핑된 메서드를 생성한다.
 * 
 * 2. implementation 'org.springframework.boot:spring-boot-starter-data-rest'
 *    를 사용해서, 기존의 repository를 repository/controller의 2가지 용도로
 *    즉시 사용하게 하는 방법이다.
 *    spring.data.rest.basePath=/api
 *    이 주소가 접근 경로이다.
 * 
 *    GET             : 읽기(Read)
 *    POST            : 생성(Create)
 *    PUT             : 업데이트(Update), 모든 필드를 전송
 *    PATCH           : 업데이트(Update), 업데이트 할 필드만 전송
 *    DELETE          : 삭제(Delete)
 */
