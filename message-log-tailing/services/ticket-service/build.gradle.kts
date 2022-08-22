plugins {
	kotlin("jvm")
	kotlin("plugin.spring")
}

dependencies {
	val debeziumVersion = "2.0.0.Beta1"
	implementation(project(":api"))

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

	// https://mvnrepository.com/artifact/io.debezium/debezium-api
	implementation("io.debezium:debezium-api:$debeziumVersion")
	// https://mvnrepository.com/artifact/io.debezium/debezium-connector-mongodb
	implementation("io.debezium:debezium-connector-mongodb:$debeziumVersion")
	// https://mvnrepository.com/artifact/io.debezium/debezium-embedded
	implementation("io.debezium:debezium-embedded:$debeziumVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName<Jar>("jar") {
	enabled = false
}