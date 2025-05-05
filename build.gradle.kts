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
	implementation("org.springframework:spring-messaging")

	//  ActiveMQ JMS Broker
	implementation("org.springframework.boot:spring-boot-starter-activemq")

	// Lombok (Reduces Boilerplate Code)
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")

	// Jakarta Persistence API (JPA Annotations)
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

	// JWT Authentication Dependencies
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Firebase Admin SDK
	implementation("com.google.firebase:firebase-admin:9.2.0")

	// HTTP client
	implementation("com.squareup.okhttp3:okhttp:4.12.0")


	implementation("org.postgresql:postgresql:42.6.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
