# CardInfoFinder
An app used to search for Bank card Information and also features OCR functionality using the phone camera. This application makes use of Binlist API and Google Mobile Vision - Text recognition library.

## Features
* Scan bank card
* Manual digit input
* Retrieve card information

## Details
* A single Activity application, conforming to the MVVM application architecture.
* Consists of four Fragments under the fragment package, each representing a screen. Calls to the web are made using a Retrofit service. 
* Card scanning is implemented using the Google Mobile Vision library, scanning is done offline. OCR related code is situated in the OCR package and consists of configuration and data processing logic.
* Unit and UI tests

## Screens
<table border="0">
 <tr>
    <td><b style="font-size:30px">Landing Page</b></td>
    <td><b style="font-size:30px">Scan Screen</b></td>
    <td><b style="font-size:30px">Manual Input Screen</b></td>
    <td><b style="font-size:30px">Result Page</b></td>
 </tr>
 <tr>
    <td>
     <img src=https://github.com/dims26/CardInfoFinder/blob/master/screens/Landing_page.png width="180" height="320" />
   </td>
   <td>
    <img src=https://github.com/dims26/CardInfoFinder/blob/master/screens/Scan_page.png width="180" height="320" />
  </td>
   <td>
    <img src=https://github.com/dims26/CardInfoFinder/blob/master/screens/Input_page.png width="180" height="320" />
  </td>
   <td>
    <img src=https://github.com/dims26/CardInfoFinder/blob/master/screens/Result_page.png width="180" height="320" />
  </td>
 </tr>
 <tr>
  <td>Home page</td>
  <td>Card scanning page.</td>
  <td>Input card digits by hand.</td>
  <td>Load and view Card detail</td>
 </tr>
 </table>

## Built using

* [Android Jetpack](https://developer.android.com/jetpack/?gclid=Cj0KCQjwhJrqBRDZARIsALhp1WQBmjQ4WUpnRT4ETGGR1T_rQG8VU3Ta_kVwiznZASR5y4fgPDRYFqkaAhtfEALw_wcB) - Official suite of libraries, tools, and guidance to help developers write high-quality apps.
  * [Android KTX](https://developer.android.com/kotlin/ktx)
  * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
  * [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation)
* [Retrofit 2](https://github.com/square/retrofit) - A type-safe HTTP client for Android and Java.
* [Google Text Recognition](https://developers.google.com/vision/android/text-overview) Text Recognition API that recognizes text in any Latin based language.
