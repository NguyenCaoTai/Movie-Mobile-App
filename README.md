# Movie-Mobile-App
Movie Mobile App

* Supported OS: from Android 5.0
* Supported devices (phone only)
* Supported features:
    + Show current location in Map
    + Search place in map
    + Show movie list information when tap the search place marker.
    + Show movie detail information when tap on exist movie in list. 
      
* How to setup, and run app.
    + Checkout the source code from master branch.
    + Register 2 API keys(google map API, Movie database API), and saving correspond both API key in local.properties at root of project.
        - Register the google map api key, and enable APIs (Maps SDK for Android, Places API). And saving as "MAPS_API_KEY" variable config, ex: MAPS_API_KEY="api_key"
        - Register the API key for The Movie Database API at https://developers.themoviedb.org/3/getting-started/introduction. And saving as "TMDB_API_KEY" variable config, ex: TMDB_API_KEY="api_key"
      
    + Open the project by Android Studio, build, run app, and make the toast. :)
    
* Note: 
    I need enhance some feature in app, such as: to draw route in map, loading indicator.
