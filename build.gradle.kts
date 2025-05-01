plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation ("org.slf4j:slf4j-api:2.0.9")
    implementation ("ch.qos.logback:logback-classic:1.4.11")
    implementation ("org.projectlombok:lombok:1.18.22")
    implementation("mysql:mysql-connector-java:8.0.33")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}