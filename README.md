# Receipt App

This is an android application which utilizes Machine Learning to convert receipt photos into text and logs the data for the user to do expense tracking. 

### Using the App
This app uses Gradle, therefore on opening the app in Android Studio, the IDE should prompt you to install the dependencies. For you to test/use the app, you will need to have some pictures of receipts ready. The app will let you either take a picture with the phone camera or select the picture from the Gallery. If you're planning to run the app using a phone emulator, open the Gallery and you can simply drag pictures into it to place them in. 

## Functionality
The app takes advantage of Microsoft's Form Recognizer API, therefore you need to make sure you have internet available, otherwise it won't be able to make HTTP requests. HTTP requests are done using Google's Volley library. 

The data extracted from the receipt pictures are automatically stored in a database utilizing the Room library. Room allows persistent data storage, therefore the app will retain the receipt information on phone/emulator restart.
