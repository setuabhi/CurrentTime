1. What is Docker? How is it different from a Virtual Machine?

        Answer:
        Docker is a platform for building, running, and managing containers.
        
        Containers are lightweight, share the host OS kernel, and only package the application + its dependencies.
        
        Virtual Machines include a full guest OS, making them heavier.

        👉 In short: Containers start faster and use less resources compared to VMs.

2. What is a container? What’s inside a Docker image?

       Answer:
    
       Container: A running instance of a Docker image. It’s an isolated environment with its own filesystem, processes, and network.
       Image: A read-only blueprint that contains everything needed to run an app (base OS libraries, runtime, app code, config).

3. Difference between Image and Container?

       Answer:
    
       Image: Static blueprint (like a class).
       Container: Running instance of an image (like an object).

4. What is a Dockerfile? Can you explain a simple one?

       Answer:
       A Dockerfile is a script with instructions to build an image.
       Example for a Spring Boot app:
    
       FROM openjdk:17-jdk-slim //This provides the Java 17 JDK environment so your app can run.
       COPY target/*.jar app.jar // Copies the built application JAR (target/*.jar) from your local machine into the container’s filesystem. Inside the container, it will be available as app.jar in the working directory (/ by default unless you set WORKDIR).
       ENTRYPOINT ["java", "-jar", "app.jar"] // Defines the command that runs when the container starts, this is how it executes: java -jar app.jar


Commands:

1. docker info

        Checks if Docker daemon is running and provides system-wide information.
        
        Shows version, storage driver, number of containers, images, etc.
        👉 Interview note: Use it when troubleshooting if Docker is alive.

2. docker build -t current-usa-time-v2 .

       Builds a Docker image from the Dockerfile in the current directory (.), that's why dot at last
       -t tags the image with a name (current-usa-time).
       👉 Example: After this, you can run docker images and see it listed.

3. docker run -p dockerPort:codePort current-usa-time-v2

          Runs a container from the image current-usa-time-v2.
    
          -p maps host port → container port
            container port= application yaml port
            host port you can give anything

       Example: -p 8888:7777 means access app on localhost:8888.
       👉 Host port = left side, container port = right side.


4. docker tag current-usa-time seturini/current-usa-time-v2:latest1

       Creates a new tag for an existing local image.
    
       Prepares the image to be pushed to a registry under a username (seturini) and version (latest).

5. docker push seturini/current-usa-time-v2:latest1

       Pushes the tagged image to Docker Hub (or registry).
       👉 After this, others can pull and run your image.

6. docker pull seturini/current-usa-time-v2:latest1

       Downloads an image from Docker Hub (or other registry) to your local machine.
       👉 Useful if you want to run an image without building it locally.



7. docker ps

        Lists all running containers.
        
        Shows ID, name, status, ports, etc.

8. docker ps -a

       Lists all containers (running + stopped + exited).
       👉 Useful to see history.

9. docker stop <containerId>

       Stops a running container gracefully (sends SIGTERM, then SIGKILL if needed).

10. docker-compose.yml

       YAML file used to define and configure multi-container applications.
    
       Instead of many docker run commands, you define services, networks, volumes here.
       👉 Example: A Spring Boot app + MySQL DB together.

11. docker run

        Creates and starts a new container from an image.
        👉 Every docker run creates a new container.

12. docker start <containerId>

        Starts an existing (stopped) container.
        👉 Difference: docker run = new container, docker start = restart old one.

13. docker stop <containerId>

        Stops a running container (gracefully).
        👉 Mention: To force stop instantly, use docker kill.

14. docker rm <containerId>

        Removes a stopped container permanently.
        👉 Must stop container first, otherwise add -f flag.

15. docker-compose.override.yml

        Optional file to override or extend settings in docker-compose.yml.
    
        Useful for dev/test differences (e.g., enable debug mode locally but not in prod).

16. docker logs <containerId>

        Displays logs (stdout/stderr) from a container.
        👉 Add -f to stream logs live (docker logs -f containerId).