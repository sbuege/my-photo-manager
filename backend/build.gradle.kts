plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("jacoco")
}

group = "my"
version = "1.0.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// spring boot
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// core
	implementation("commons-io:commons-io:2.15.1")
	implementation("com.google.guava:guava:33.2.0-jre")
	implementation("org.apache.commons:commons-lang3:3.19.0")

	// lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// database
	implementation("com.h2database:h2")
	implementation("org.postgresql:postgresql")

	// imaging
	implementation("org.apache.commons:commons-imaging:1.0.0-alpha5")
	implementation("net.coobird:thumbnailator:0.4.20")
	implementation("com.drewnoakes:metadata-extractor:2.19.0")

	// json
	implementation("org.json:json:20231013")

	// test dependencies
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	jvmArgs("-XX:+EnableDynamicAgentLoading")
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		html.required.set(true)
	}

}

tasks.jacocoTestCoverageVerification {
	dependsOn(tasks.jacocoTestReport)

	violationRules {
		rule {
			limit {
				counter = "CLASS"
				value = "COVEREDRATIO"
				minimum = BigDecimal.valueOf(0.9)
			}
		}

		rule {
			limit {
				counter = "LINE"
				value = "COVEREDRATIO"
				minimum = BigDecimal.valueOf(0.8)
			}
		}

		rule {
			limit {
				counter = "BRANCH"
				value = "COVEREDRATIO"
				minimum = BigDecimal.valueOf(0.7)
			}
		}
	}

	classDirectories.setFrom(
		files(classDirectories.files.map {
			fileTree(it) {
				exclude("**/*Response.class")
				exclude("**/*Exception.class")
			}
		})
	)
}