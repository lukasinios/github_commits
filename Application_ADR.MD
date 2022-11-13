# Github commit application architecture decision record
All architectural decisions will be in this file

#[12.11.2022] Project start

## Project structure
Project will be write in clean architecture with 3 separated layers. 
The application have logic to downloading data from different sources, maybe the sources or used libraries will be changed in future.
The application at this point will not have separate modules for functionalities due to the time limit in the project and the lack of good recognition.
Dependencies will be storage in settings.gradle and all modules should use dependencies from settings.

## View structure
The application will be use MVVM pattern with ViewModel.
The application will be used single activity.

## Testing
Due to time limit, there will be only unit tests in the project

## Used libraries
List of most important project libraries with a brief justification 

### Dependency Injection
Hilt - Quick setup. No need for advanced components after initial analysis

### Cache library
Room - Simple configuration allows you to store large amounts of data

### Api client
Retrofit2 - due to the time limit, a library well known by the development team will be used

### Asynchronous operations
Kotlin Coroutines - High level of concurrency with very small overhead

### Testing library
Junit4 - slower then Junit5 but faster to set up


