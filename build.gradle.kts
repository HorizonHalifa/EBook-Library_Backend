plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.horizon.ebooklibrary"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring boot core dependencies
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // Database handling
	implementation("org.springframework.boot:spring-boot-starter-security") // Authentication
	implementation("org.springframework.boot:spring-boot-starter-validation") // Input validation
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// Lombok (Reduces Boilerplate Code)
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")

	// Jakarta Persistence API (JPA Annotations)
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

	// JWT Authentication Dependencies
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation("org.postgresql:postgresql:42.6.0")

	// H2 Database (Temporary database for testing)
	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
