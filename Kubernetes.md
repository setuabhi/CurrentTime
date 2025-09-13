**Kubernetes:**

      Why we need it ?
      1. Scaling: Automatically scale containers based on demand.
      2. High Availability: Ensure containers are restarted and rescheduled if they fail.
      3. Load Balancing: Distribute traffic across multiple containers.
      4. Self-Healing: Automatically restart or reschedule failed containers.
      5. Service Discovery: Manage networking between containers automatically.
      6. Automated Rollouts: Handle safe deployments and rollbacks.
      7. Resource Management: Allocate CPU/memory to prevent overuse.
      8. Multi-container Coordination: Manage complex applications with multiple containers.
      9. Environment Consistency: Ensure consistent behavior across environments.
      10. Multi-cloud Support: Run containers across different cloud providers.


check kubernetes live or not: kubectl cluster-info

      1. In Kubernetes, a Pod is the smallest deployable unit that you can create and manage. It's a fundamental concept in Kubernetes and represents a single instance of a running process in your cluster:
      2. Imagine you have a web application that consists of a web server and a caching server. You could deploy these two components in separate containers within the same Pod. This allows them to easily communicate with each other (e.g., the web server can access the cache on localhost) and be managed as a single unit.
      3. You don’t directly manage Pods → Deployment does it for you (kubectl apply -f Deployment.yaml)
      4. kubectl apply -f service.yaml  --> A Service is a Stable Entry Point, Pods get a new IP each time they restart → bad for clients, A Service gives a stable IP/DNS to access Pods, It also does load balancing across multiple Pods.
    👉 Think:
        Pods = rooms in a hotel.
        Deployment = hotel manager ensuring enough rooms exist.
        Service = hotel reception desk with a fixed phone number → guests always call reception, not individual rooms.
      5. kubectl get deployment
      6. kubectl get service
      7. kubectl get pods
      8. to kill deployment: kubectl delete deployment current-usa-time-deployment ( will kill pods)
      9. to kill service: kubectl delete service current-usa-time-service
      10. Service type:
            a. ClusterIP: Default type if you don’t specify anything, Exposes the Service only inside the cluster, Other Pods/Services can talk to it, but outside world cannot. 👉 Use case: internal communication (e.g., backend → database).
            b. NodePort: Exposes the Service on a static port (30000–32767) on each Node’s IP.

![img.png](img.png)
![img_1.png](img_1.png)