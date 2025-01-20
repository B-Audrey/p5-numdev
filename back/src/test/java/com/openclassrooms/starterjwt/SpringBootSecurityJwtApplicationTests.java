package com.openclassrooms.starterjwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class SpringBootSecurityJwtApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	void main() {
		// Ce test appelle directement la méthode main
		SpringBootSecurityJwtApplication.main(new String[]{});
	}

}
