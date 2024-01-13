import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
	id("org.asciidoctor.jvm.convert") version "3.3.2"

	val kotlinVersion = "1.6.21"

	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	kotlin("plugin.jpa") version kotlinVersion
	kotlin("plugin.allopen") version kotlinVersion
	kotlin("plugin.noarg") version kotlinVersion
	kotlin("kapt") version "1.3.61"
}
allOpen {
	annotation("javax.persistence.Entity")
}

noArg {
	annotation("javax.persistence.Entity")
}

buildscript {

}


group = "com.strawberry"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17
val querydslVersion: String = "5.0.0"

repositories {
	mavenCentral()
	maven("https://jitpack.io")
	maven("https://plugins.gradle.org/m2/")
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation ("ch.qos.logback:logback-classic:1.2.11")

	/**
	 * testing
	 */
	implementation("org.junit.jupiter:junit-jupiter:5.9.2")
	implementation("org.mockito:mockito-core:4.11.0")
	implementation("org.mockito:mockito-junit-jupiter:4.11.0")



	/**
	* spring security
	*/
	implementation("org.springframework.security:spring-security-web:5.7.3")
	implementation("org.springframework.security:spring-security-config:5.7.3")
	implementation("org.springframework.security:spring-security-core:5.7.3")
	implementation("org.springframework.security:spring-security-test:5.7.3")
	implementation ("org.springframework.boot:spring-boot-starter-security")
	implementation("org.mapstruct:mapstruct:1.5.3.Final")
	implementation("org.mapstruct:mapstruct-processor:1.5.3.Final")
	runtimeOnly("mysql:mysql-connector-java:8.0.30")

	/**tests and mockmvc */
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

	/**
	 * jwt
	 */
	implementation("io.jsonwebtoken:jjwt:0.9.1")

	/** email service */
	implementation("org.springframework.boot:spring-boot-starter-mail")

	/** querydsl */
	kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")
	implementation("com.querydsl:querydsl-jpa:$querydslVersion")
	kapt("org.springframework.boot:spring-boot-configuration-processor")

	/** validator */
	implementation("org.springframework.boot:spring-boot-starter-validation")

	/** swagger  */
	implementation("io.springfox:springfox-boot-starter:3.0.0")

	/** h2 db for testing */
	testImplementation("com.h2database:h2")

}

kotlin.sourceSets.main {
	setBuildDir("$buildDir")
}

tasks.getByName<Jar>("jar") {
	enabled = false
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val asciidoctorExt: Configuration by configurations.creating
dependencies {
	asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")
}


tasks {
	val snippetsDir by extra { file("build/generated-snippets") }

	test {
		outputs.dir(snippetsDir)
	}

	asciidoctor {
		inputs.dir(snippetsDir)
		configurations(asciidoctorExt.name)
		dependsOn(test)
		sources(

		)
		doLast {
			copy {
				from("build/docs/asciidoc")
				into("src/main/resources/static/docs")
			}
		}
	}

	build {
		dependsOn(asciidoctor)
	}
}