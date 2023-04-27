# Prototype of an application for monitoring houseplant environment - ANDROID MOBILE APP
This is a 1/3 of a complete project solution for a bachelor's thesis. The thesis (and the underlying project) deals with designing and implementing multitier application for continuous monitoring of houseplant's environment (ambient temperature, light intensity and soil moisture). The application is meant to be used by multiple users with multiple plants and measuring devices.

# The functional parts of the projects
1. REST API with database for application logic (ASP.NET Core with Entity Framework Core): https://github.com/michalmusil/housePlantMeasurementsApi
2. Android navive app for presenting the data to users (Kotlin, Jetpack Compose): https://github.com/michalmusil/plant_monitor
3. ESP 8266 code for the device measuring the houseplant environment (C++ in Arduino IDE): https://github.com/michalmusil/housePlantMeasuringDevice

To make the project work as a whole, you have to get all of the parts working together.

## ANDROID MOBILE APP
The mobile app is implemented using Kotlin with Android SDK and Jetpack Compose for the UI layer and is based on MVVM architecture. The app is meant for regular users only, meaning that admin users don't get any additional functionalities. A frontend for admin users has not been developed yet, so the admin-speciffic operations still have to be posted manually using swagger. 

The app serves as the user interface for the whole application. The user manages his plants and measuring devices here. Main use case is creating representations of your individual houseplants and then assigning measuring devices to them (those that you physically own and have registered). The measuring devices measure your plant periodically and send the data to the REST API via WiFi. As the mobile app communicates with the REST API for all data querying and manipulation, the measurements can then be viewed on the plant's detail. Push notifications are sent by the REST API, when any measured value of plant's environment surpasses limits set by the user.

## How to make it work
To make the Android mobile app work, you need to:
1. Change the REST API base URL to your instance in: ApiConstants.kt and if you are using plain http on your REST API, then also in res/xml/testing_network_security_config.xml
2. Create a Firebase Project for the application (if you have already done so while setting up the REST API, use that one). Then set up the mobile app with the Firebase project.
3. Run the app on your Android mobile device or Android studio emulator.
4. It is possible, that the project will implicitly go into build flavour with mocked data. In that case, switch build flavour to prodDebug in Build > Select Build Variant

Helpers:
* Setting up an Android application with Firebase project: https://firebase.google.com/docs/android/setup