## Build and run the project

This example Play project was created from a seed template. It includes all Play components and an Akka HTTP server. The project is also configured with filters for Cross-Site Request Forgery (CSRF) protection and security headers.

To build and run the project:

1. Use a command window to change into the example project directory`

2. Build the project. Enter: `sbt run`. The project builds and starts the embedded HTTP server. Since this downloads libraries and dependencies, the amount of time required depends partly on your connection's speed.

3. USe a curl command to use a POST request to add data to be converted to the data structure.

4. After the message `Server started, ...` displays, enter the following URL in a browser: <http://localhost:9000> and either /temperature, /distance or /volume to get the conversions
