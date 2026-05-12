# URL Shortener — Coding Challenge

*Please spend around 1-2 hours on the coding part of this challenge. Scale down the requirements to fit the time allowed if needed. It's also A-okay to update the README outside of that time.*

*And it goes without saying, but it doesn't make sense to use agents!*

## Getting Started

You will need Java 21 or newer installed.

To build and run:

```shell
./mvnw package
java -jar target/url-shortener.jar
```

On Windows:

```shell
mvnw.cmd package
java -jar target/url-shortener.jar
```

The server starts on **http://localhost:8080**.

---

## The Task

Implement a URL shortener API inside `UrlShortenerServlet.java`.

**It will:**

- Accept a long URL and return an HTML response showing the original URL and its shortened equivalent
- It MUST always return the same short code for a given original URL
- Resolve a short code and redirect the browser to the original URL
- This MUST be robust when used at scale and suitable for making publicly available.

In-memory storage is fine.

You're welcome to add dependencies to `pom.xml` and introduce new classes, but please use the `Servlet` class provided. There are no other constraints.

**Please include a short note in this README** covering:
- How to use your API (endpoints, request format, examples)
- Any assumptions or shortcuts you made
- Anything you would do differently with more time


## Developer notes

- A simple form is rendered at `/`
- A POST request to `/` will create a shortened URL
- The shortened URL is returned in the response body
- The user may return to the form to shorten another URL
- All previously shortened URLs are displayed along with links to the shortened URLs
- Clicking on a shortened URL will redirect the browser to the original URL, in a new tab using a 302 Found response code
- Redirect URLs begin with /r/ and are case-insensitive
- http:// or https:// is required in the form
- Stored URLS are trimmed and are case-sensitive, and must be unique to generate distinct codes

## API

Base URL: `http://localhost:8080`

| Method | Path             | Description                          | Success         | Errors |
|--------|------------------|--------------------------------------|-----------------|--------|
| GET    | `/`              | Render the submission form           | `200 OK` (HTML) | —      |
| POST   | `/`              | Create (or look up) a short code     | `200 OK` (HTML) | `400` if `url` is missing/invalid |
| GET    | `/r/{shortcode}` | Redirect to the original URL         | `302 Found`     | `404` if the code is unknown, `400` if malformed |

### POST `/`
Form-encoded body with a single field:

- `url` - the URL to shorten (must include `http://` or `https://`)

**Implementation choices**
- All URLs are stored in memory using a H2 database.
- JPA is used to demonstrate an ORM approach, with a long primary key and a string index on a sha256 hash of the original URL
- The hash is not immune to collisions, but it is very unlikely to ever be a problem
- The shortened URL is not actually stored in the database, but is generated using a two-way algorithm called Crockford's Base32. This allows sequential long auto-ids to be used as primary keys (very fast on large tables), which are easily converted to deterministic alphanumeric short codes.
- The auto ids start at 10000, purely for aesthetic reasons.
- When a shortened URL is requested, the short code is converted back to the original URL via string to long conversion, and a database search is performed.
- I have opted for separation of concerns. There is a persistence layer, a service layer, and a controller layer (the servlets).
- Generating a short URL and redirecting are separate controllers, so separate servlets are used.
- No DI framework is used, dependencies are injected via constructor.
- The database entities are not passed any higher than the service layer. Instead we use a DTO - an immutable record type. This practice ensures that only explicit fields are passed to the controller layer, with the mapping to DTOs happening in the service layer.
- Services throw exceptions for business logic errors. The controller layer handles these exceptions and returns http responses.

**If I had more time**
- The insert is likely prone to race conditions, due to the check-then-insert behaviour. Given more time, we would implement a retry - if the insert fails, it is due to collision - which means we can look up the short code in the database.
- Get codes listing is unbound, it's for debug only. In production this would be a terrible idea
- We should support HEAD as well as GET requests for the redirects
- Spring DI would be a nice addition.
- Storing the shortened URLs in the database would be a nice convenience for developers, but is not strictly required as it is deterministically generated.
- The solution has basic unit tests for the encoder, but none for the servlets and services. This was due to time constraints.
- After the POST request, the page simply shows the shortened URL. Normally, after a POST, the browser would redirect to a GET page, so that F5 or cached browser pages won't result in the same data being resubmitted. Because this service is idempotent, there is no danger in leaving the raw POST result - but given more time this should be resolved.
- The page should not need a full refresh to show the shortened URL. JavaScript could be used to update the page without a full refresh.
- Styles should be added to the page.
- The errors are shown as raw Tomcat error messages. They should be formatted in a more user-friendly way.