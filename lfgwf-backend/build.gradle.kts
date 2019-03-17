import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.slf4j:slf4j-simple:1.7.26")
    implementation("io.ktor:ktor-server-core:1.1.3")
    implementation("io.ktor:ktor-server-netty:1.1.3")
    implementation("io.ktor:ktor-html-builder:1.1.3")
    implementation("com.google.code.gson:gson:2.8.5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes(
            "Main-Class" to "wf.lfg.MainKt"
        )
    }
    from(configurations.compileClasspath.map { if (it.isDirectory) it else zipTree(it) })
}
