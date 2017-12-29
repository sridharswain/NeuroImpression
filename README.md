# NeuroImpression  
Know Your Customer Better

## Description
NeuroImpression is a mobile application which is basically a Financial Profiler which create a profile of a user according to his/her facial expression, age and gender. It uses Deep Learning Algorithm which when processed through Psychometric Rules and presents us with an unique customer profile which can be used to understand customer better. Nearly 6000(15 Sec) videos are used for training the LSTM Neural Network. Machine learning models and matching is used to train models and perform facial recognition to provide Cognitive results. It judges a customer based on features such as **Extroversion**, **Aggreable**, **Openness**, **Conscientious**, **Neuroticism**. Based on these Features of a customer it tell is one of **Anxious Investor**, **Hoarder**, **Cash Splasher**, **Fitbit Financer** or **Ostrich**.

## Requirements
* NPM (>4.9.1)
* Node
* Python (>3.0)
* JDK 
* Android Studio (>2.3.3)
* FFMPEG to extract image frame from video (`sudo apt-get install ffmpeg`)
* Chalearn Dataset
* IBM Watson Vision API (to get age and gender from image)

## Setting up
Clone the repository and change directory to NeuroImpression  
`git clone https://github.com/sridharswain/NeuroImpression.git && cd NeuroImpression`

#### Server Setup
* Go to server folder and install dependencies `cd server && npm install`
* Subscribe for IBM Watson Vision API and replace the api key in `LSTMPredictor.js`

### Android Application
#### Automatic
* Open the project(Android Folder) in android studio and let all dependencies to be installed automatically and press run to install debug apk to phone.
#### Manual
* Go to Android Folder and run gradlew with installDebug `cd Android && ./gradlew installDebug` to install dependencies and install the debug apk to connecte phone through ADB.

## Running the system
* Connect your phone and server machine on same network and connect them to internet.
* Start the server by `cd server && npm start`.
* Open `/FrontEnd/Dashboard/dashboard.html` on your browser to view dashboard on the machine.
* Open the app on the phone and capture a video of 5-10 seconds. The app will now send the video to server which will displayed on the dashboard in short time
* Here the calculation starts and results with charts and conclusions can be viewed on dashboard.

## Contributors

<B>Shivam Kapoor</B>
                  <p style="font-size: 14px;"><B>Project Leader and Front-End Developer</B><br>
                   <B>Github:</B> ConanKapoor<br>
                   <B>E-mail:</B> kapoor.shivam88@gmail.com<br>
                   <B>Contact:</B> (+91) 9521458982<br>
                   </p>
<B>Abhishek Singh</B>
                  <p style="font-size: 14px;"><B>DATA SCIENTIST</B><br>
                  <B>Github:</B> absingh31<br>
                  <B>E-mail:</B> absinghemail@gmail.com<br>
                  <B>Contact:</B> (+91) 8072561171<br></p>
<B>Sridhar Swain</B>
                  <p style="font-size: 14px;"><B>Mobile and Back-End Developer</B><br>
                  <B>Github:</B> sridharswain<br>
                  <B>E-mail:</B> sridharswain25@hotmail.com<br>
                  <B>Contact:</B> (+91) 9790700912<br>
                  </p>
  
