## BokBytarAppen

This project aims to provide a matchmaking service for students wishing
buy, sell and share student literature.

### Dependencies

* [AndroidNilewapp](http://github.com/nilewapp/AndroidNilewapp)
* [ActionBarSherlock](http://actionbarsherlock.com)
* [FacebookSDK](http://developers.facebook.com/docs/getting-started/facebook-sdk-for-android/3.0/)

### Using the app

1. Clone the repository into an Eclipse workspace

        $ cd <your workspace>
        $ git clone https://github.com/nilewapp/BokBytarAppen.git

2. Import the project in Eclipse with File > Import > Android > Existing Android Code Into Workspace and select the newly cloned directory

3. Add the dependencies listed above to your Eclipse workspace and add them as required projects in Properties > Java Build Path > Projects. 

4. Make sure 'Android Private Libraries' is checked in Properties > Java Build Path > Order and Export.

5. Follow the [tutorial](https://developers.facebook.com/docs/getting-started/facebook-sdk-for-android/3.0/) on how to use the Facebook API with the app. Most importantly, add the app ID you obtain from Facebook to `/res/values/strings.xml` or the file of your choosing.

### Using the app with the server

Follow the instructions listed in the server repository [README](https://github.com/nilewapp/BokBytarAppenServer)

### Copyright and license

 Copyright 2013 Robert Welin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
