# Websocket Integration Tests

## Purpose

Show that we can integration test our websocket applications without spinning up http server.
This will speed up our tests.

For example, our current public api websocket integration tests spin up a http server.
Introduces race conditions, lose control of messaging, debugging is unreliable, slow.
Maybe worse, for euclid, we don't have integration and every test is run in acceptance, 
and acceptance needs K8S to be up.  

Repo Link
https://github.com/lmax-north/websocket-integration-test

It may not be the nicest example, but it shows that it is possible.

## Demo

- Test Comparisons
    - Integration with Vertx
    - Integration
    - Thread Counts

- Verify no messages
    - Test durations

    
- Implementation



- Finish
  - Rest
  - Maybe cool
  - Feedback `/give`


