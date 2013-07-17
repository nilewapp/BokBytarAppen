## BokBytarAppen

This project aims to provide a matchmaking service for students wishing
buy, sell and share student literature.

### Dependencies

* [AndroidNilewapp](http://github.com/nilewapp/AndroidNilewapp)
* [ActionBarSherlock](http://actionbarsherlock.com)

### Using the app

1. Clone the repository into an Eclipse workspace

        $ cd <your workspace>
        $ git clone https://github.com/nilewapp/BokBytarAppen.git

2. Import the project in Eclipse with File > Import > Android > Existing Android Code Into Workspace and select the newly cloned directory

3. Add the dependencies listed above to your Eclipse workspace and add them as required projects in Properties > Java Build Path > Projects. 

4. Make sure 'Android Private Libraries' is checked in Properties > Java Build Path > Order and Export.

### Using the app with the server

Follow the instructions listed in the server repository [README](https://github.com/nilewapp/BokBytarAppenServer)

### TODO

#### Registration and Authentication with server

Since the user has to be authenticated with the
server, a Facebook login would unnecessarily create two points of 
authentication. Generating a password unknown by the user and storing it
on the device would be a good solution which would make most use convenient
for the user. However, the user should be able to use his account on a different
devices and it will not be trivial to securely sync the multiple devices. We will
therefore remove the Facebook dependency and create our own solution. 

At registration the user should be prompted for an email address and 
a password. These should be submitted to the server which will store
the email address and a salted and hashed (using BCrypt) version of the 
password. The salt should also be stored on the server.

Validation of user credentials should be done by comparing the stored hash
with a hash generated in the same way from the provided password and the
stored salt. If the two hashes match the user is authenticated. 
This creates a session which in turn should be authenticated as described [here](http://jaspan.com/improved_persistent_login_cookie_best_practice).


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
