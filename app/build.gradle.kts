plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    id("io.github.farimarwat.lokalenow") version "1.2"
}

android {
    namespace = "com.example.uipj"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.uipj"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation("com.squareup.picasso:picasso:2.8")
    implementation("com.saadahmedev.popup-dialog:popup-dialog:2.0.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui-android:1.5.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //circle indicator
    implementation("me.relex:circleindicator:2.1.6")

    //material
    implementation("com.google.android.material:material:1.10.0")

    //date picker
    implementation("com.github.swnishan:materialdatetimepicker:1.0.0")

    //swipe to delete
    implementation("it.xabaras.android:recyclerview-swipedecorator:1.4")

    //AvatarImageGenerator
    implementation("com.github.amoskorir:avatarimagegenerator:1.5.0")

    //picasso
    implementation("com.squareup.picasso:picasso:2.8")

    //relativelayouts
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    //Support Library
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    //Card stack view
    //implementation("com.yuyakaido.android:card-stack-view:2.3.4")
    implementation("com.github.yuyakaido:cardstackview:2.3.4")

    //circle progress bar
    implementation("com.github.jakob-grabner:Circle-Progress-View:1.4")
    //easy flip view
    implementation("com.wajahatkarim:EasyFlipView:3.0.3")

    //lottie
    implementation("com.airbnb.android:lottie:6.2.0")

    //bottom sheet
    implementation("com.github.Kennyc1012:BottomSheetMenu:5.1")

    //popup dialog
    implementation("com.saadahmedev.popup-dialog:popup-dialog:1.0.5")

    //glide image
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //simple search view
    implementation("com.github.Ferfalk:SimpleSearchView:0.2.1")

}