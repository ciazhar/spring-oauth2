# DOKUMENTASI OAUTH2 #

# Anatomi Sistem #
Sistem ini terdiri dari beberapa aplikasi yang salin berintegrasi sesuai fungsinya masing-masing. Aplikasi tersebut diantaranya yaitu :
- Authorization Server
- Resource Server
- Client Application

## Authorization Server ##
Authorization Server yaitu adalah server yang menyediakan service untuk otorisasi atau berfungsi sebagai `open gate` yang menghubungkan antara Client Application dan Resource Server.

## Resource Server ##
Resource Server yaitu server yang menyediakan service yang berhubungan dengan data data client yang biasanya disimpan di database. Resource Server dapat berupa kumpulan api.

## Client Application ##
Client Application yaitu applikasi yang berhubungan langsung dengan user dan berfungsi untuk menerima request user. Client Application dapat berupa Aplikasi Web Clint Side(html/css/js) atau Mobile Apps(android/ios)

## Grant Type pada OAuth2 ##
 OAuth 2 sendiri memberikan beberapa grant type sesuai fungsinya masing-masing. Grant type tersebut diantaranya :
 - Authorization code
 - Implicit
 - dll
