# Reactive Spring Boot R2DBC Project

This is a clean starter project for building Reactive applications using Spring WebFlux and Spring Data R2DBC with PostgreSQL.

## Stack
- Java 21
- Spring Boot 2.5.7 (Reactive)
- PostgreSQL
- Maven


## Running
```cmd
mvn spring-boot:run
```

## Video Streaming Demo
### Setup
1.  **Place Video**:
    - For MKV: Put `video1.mkv` in `src/main/resources/videos/`.
    - For MP4: Put `video1.mp4` in `src/main/resources/videos/`.
2.  **Run App**: `mvn spring-boot:run`

### Playback
- **Browser Player**: `http://localhost:8080/index.html` (Points to MP4 by default).
- **Direct Stream (MP4)**: `http://localhost:8080/stream/video1` (Default Endpoint)
- **Direct Stream (MKV)**: `http://localhost:8080/stream/mkv/video1`
- **FTP Stream (Demo)**: `http://localhost:8080/stream/ftp/video1` (Requires FTP server)
- **DB Stream (Simulated)**: `http://localhost:8080/stream/db/video1` (Returns dummy bytes)

> **Note**: Database (R2DBC/Flyway) dependencies are currently **disabled** in `pom.xml` and `application.properties` to focus on this streaming demo.

## How it Works (Step-by-Step)

1.  **Request**: The browser sends a `GET` request to `/stream/{title}`. It often includes a `Range` header (e.g., `bytes=0-`) to ask for specific parts of the video.
2.  **Controller**: The `StreamingController` receives this request.
3.  **Service**: The `StreamingService` uses Spring's `ResourceLoader` to find the video file in the `classpath:videos` folder.
4.  **Response**:
    - The controller returns a reactive `Mono<Resource>`.
    - **Spring WebFlux Magic**: Spring automatically handles the `Range` headers!
    - If the browser asks for a chunk, Spring returns status `206 Partial Content` with just that chunk.
    - This allows the user to seek (skip forward/backward) instantly without downloading the whole file.

## Troubleshooting

### Video plays audio but no video (Black Screen)?
- **Cause**: Browsers (Chrome, Edge, Safari) do **not** natively support the **MKV** container format. They can often decode the audio track but fail on the video track.
- **Solution**:
    - Use **.mp4** files (H.264 codec) for browser testing.
    - To test MKV streaming, use a desktop player like **VLC** (`Media -> Open Network Stream -> http://localhost:8080/stream/video`).
