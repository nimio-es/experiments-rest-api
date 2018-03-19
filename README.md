# Experiments

This project started as a technical test proposed by [theam.io](http://theam.io/en) in order to collaborate with them. A lot of incredible people work there.

I Finally adopted this project as perfect excuse to try and learn  different technologies traying to maintain the initial proposal. Although I will probably change many things to focus on each case in what I think is important.

## The original requirements 

What I had to do:

- Rest API for Customers. Each customer have to be defined with 
    - First Name
    - Last Name
    - National Document of Identity
    - Image (optional)
    - With each customer also store last purchases information

- Only admin roles can access the API. Restrict access using OAuth 2.

- Optionally, but really optional, one or more cucumber tests to check one or more end-points.


## Branches

Originally my purpouse was to use only master branch to work in one alternative each time until finish this and pass to another technology making a tag. No branches, I thought.

However, given that in my professional life I also have to try new things unexpectedly, I have moved to a model in which I will have as many branches as I need to test things in parallel and, from there, create tags when I get bored or do not need that branch anymore.

Therefore, in the master branch there will be nothing left (the story for the curious) except the reference to the branches in progress and the possible tags of those things that are already closed.

Visit the branches or tags to see the particularities of each one.

## Live branches

### Spring Boot with Kotlin and Gradle

See: https://github.com/saulo-alvarado/experiments-rest-api/tree/jvm_kotlin_and_springboot_with_gradle

One of the languages that I am most interested in these days is Kotlin. And one of the reasons is because it is easy to integrate with Java and has a library for Functional Programming that has caught my attention: [Arrow](http://arrow-kt.io/).

Professionally I have always been tied to Maven and I have rarely been able to prove anything with [Gradle](https://gradle.org/). It was time for me to let go of my hair and give him some cane.

## Tags (or closed branches)

### JVM Using Spark Framework

Java, Maven and [Spark Framework](http://sparkjava.com/), because it's really easy to start any API with it and it's light.

### Spring Boot with Java, Kotlin and Maven

The base of the branch Spring boot with Kotlin and Gradle. Starting with Kotlin but some things left half made.

