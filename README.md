Simple "Hello, World" with BlueEyes
===================================

To run locally:
---------------

1) Clone this repo
2) Create a BlueEyes conf file:

    echo "server {\n  port = 8585\n  sslEnable = false\n}" > server.conf

3) Compile the app and create a start script:

    sbt stage

4) Run the app:

    target/start


To run on Heroku:
-----------------

1) Clone this repo
2) Have the Heroku client installed and setup (ssh keys, logged in, etc)
3) Create an app on Heroku:

    heroku create -s cedar

4) Push the app to Heroku:

    git push heroku master

