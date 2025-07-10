package org.suhodo.cardatabase;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.suhodo.cardatabase.domain.AppUser;
import org.suhodo.cardatabase.domain.Car;
import org.suhodo.cardatabase.domain.Owner;
import org.suhodo.cardatabase.repository.AppUserRepository;
import org.suhodo.cardatabase.repository.CarRepository;
import org.suhodo.cardatabase.repository.OwnerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class CardatabaseApplication  implements CommandLineRunner {

	private final OwnerRepository ownerRepository;
	private final CarRepository carRepository;
	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(CardatabaseApplication.class, args);
	}
	
	public boolean getUser(){
		Optional<AppUser> appUser = appUserRepository.findByUsername("user");
		boolean isPresent = appUser.isPresent();
		return isPresent;
	}

	public void TestOwnerCar() {
		// 부모 엔티티
		Owner owner1 = Owner.builder()
				.firstname("John")
				.lastname("Johnson")
				.build();

		Owner owner2 = Owner.builder()
				.firstname("Mary")
				.lastname("Robinson")
				.build();

		// 부모 엔티티를 먼저 저장해야 한다.
		ownerRepository.saveAll(Arrays.asList(owner1, owner2));

		/* 자식 엔티티에 부모 엔티티를 연결시켜야한다. */

		// 자식 엔티티
		Car fordCar = Car.builder()
				.brand("Ford")
				.model("Mustang")
				.color("Red")
				.registrationNumber("T-111")
				.modelYear(2025)
				.price(5400)
				.owner(owner1)
				.build();
		carRepository.save(fordCar);

		Car kiaCar = Car.builder()
				.brand("Kia")
				.model("K-5")
				.color("Silver")
				.registrationNumber("K-111")
				.modelYear(2025)
				.price(6400)
				.owner(owner2)
				.build();
		carRepository.save(kiaCar);

		Car hyndaiCar = Car.builder()
				.brand("Hyundai")
				.model("Genesis")
				.color("Black")
				.registrationNumber("H-111")
				.modelYear(2025)
				.price(7800)
				.owner(owner2)
				.build();
		carRepository.save(hyndaiCar);
	}

	public void TestRegisterAppUser() {

		AppUser appUser0 = AppUser.builder()
				.username("user")
				.password(passwordEncoder.encode("user"))
				.role("USER")
				.build();

		AppUser appUser1 = AppUser.builder()
				.username("admin")
				.password(passwordEncoder.encode("admin"))
				.role("ADMIN")
				.build();

		appUserRepository.saveAll(Arrays.asList(appUser0, appUser1));
	}

	@Override
	public void run(String... args) throws Exception {
		if(getUser())
			return;

		TestRegisterAppUser();
		TestOwnerCar();
	}
}
