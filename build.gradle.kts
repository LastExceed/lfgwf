plugins {
    kotlin("jvm") version "1.3.21" apply false
    id("kotlin2js") version "1.3.21" apply false
}

allprojects {
    group = "wf.lfg"
    version = "1.0-SNAPSHOT"

    repositories {
        jcenter()
    }
}
