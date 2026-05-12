# Actions I took when working on this project

For this task, given the limited time I opted for a test-driven approach. When thinking about how to build a URL shortener, I opted to break it down in to the characteristics required of a URL shortner - sourced mainly from the README.md, plus some common requirements.

## Prep the local project

1. Install Java 21
1. Create actions markdown file
1. Git init to start tracking changes
1. Create test folder

## Clock start

1. Build and run the project to confirm it works as-is
1. Create a branch - `feature/shorten-url`
1. Major assumptions and reasoning
   - This project will use HTTP verbs POST to create a shortened URL, and GET to return the original URL. This is typical of this kind of service
   - Possible return states: 4xx (bad request), 5xx (server error), 201 Created on POST, 302 Found on GET. These are typical of this kind of service
   - We use 302 Found instead of 301 Moved Permanently because we do not want the URLs to cache in a browser (allows for editing the URL at a later date)
   - The project will serve under HTTP. We assume SSL termination is handled elsewhere
   - We will redirect GET, OPTIONS and HEAD requests to ensure maximum functionality in the browser
   - We will not redirect POST, PUT, DELETE, PATCH, TRACE, CONNECT, or other HTTP verbs
1. Define requirements/test cases
    - A URL shortener must be idempotent
    - A URL shortener must be able to shorten a URL
    - A shortened URL must be unique
    - A URL shortener must be able to expand a shortened URL and redrect the user
    - HEAD requests must be supported
    - OPTIONS requests must be supported
    - GET requests must be supported
    - POST requests must be supported
    - No other HTTP verbs may be supported

https://central.sonatype.com/search
I wanted Hibernate as the ORM, and h2 as the database
I chose recent versions with a fair number of packages using it