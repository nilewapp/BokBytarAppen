## TODO

### Registration and Authentication with server

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



