# SwiftLogistics



#### Run Everything in docker-compose with One Command

1.  Make sure the `docker-compose.yml` file is saved in your project's root directory.
2.  Open a terminal in that same directory.
   3.  Run the command:
       ```bash
       docker-compose up -d
    
`up` tells Docker Compose to create and start the containers.
`-d` runs them in the background (detached mode).


#### Step 3: Verify and Connect

* **Check Status:** Run `docker-compose ps` or `docker ps` to see all three containers running.

* **Access RabbitMQ UI:** Open your web browser and go to **`http://localhost:15672`**. You can log in with `guest` / `guest` to see the RabbitMQ management dashboard.

#### Step 4: Stop Everything with One Command

you can stop and remove all the containers defined in the file with a single command from the same directory:

```bash
    docker-compose down