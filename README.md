# Spring RSocket Getting Started

RSocket is a binary protocol for use on byte stream transports such as TCP, WebSockets, and Aeron.
It enables the following symmetric interaction models via async message passing over a single connection:

- request/response (stream of 1)
- request/stream (finite stream of many)
- fire-and-forget (no response)
- channel (bi-directional streams)

## Description

## Demo
### 1. Prepare Project
#### Dependency

- RSocket
```
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-rsocket")
}
```

#### RSocket Client CLI

```
$ curl -L https://github.com/making/rsc/releases/download/0.4.2/rsc-0.4.2.jar -o rsc.jar
```

<details>
<summary><code>$ java -jar rsc.jar --help</code></summary>
<pre>
<code>
$ java -jar rsc.jar --help

usage: rsc Uri [Options]

Non-option arguments:
[String: Uri]

Option                              Description
------                              -----------
--channel                           Shortcut of --im REQUEST_CHANNEL
-d, --data [String]                 Data. Use '-' to read data from
                                      standard input. (default: )
--dataMimeType, --dmt [String]      MimeType for data (default:
                                      application/json)
--debug                             Enable FrameLogger
--delayElements [Long]              Enable delayElements(delay) in milli
                                      seconds
--fnf                               Shortcut of --im FIRE_AND_FORGET
--help                              Print help
--im, --interactionModel            InteractionModel (default:
  [InteractionModel]                  REQUEST_RESPONSE)
--limitRate [Integer]               Enable limitRate(rate)
--log [String]                      Enable log()
-m, --metadata [String]             Metadata (default: )
--metadataMimeType, --mmt [String]  MimeType for metadata (default:
                                      text/plain)
-q, --quiet                         Disable the output on next
-r, --route [String]                Routing Metadata Extension
--request                           Shortcut of --im REQUEST_RESPONSE
--resume [Integer]                  Enable resume. Resume session duration
                                      can be configured in seconds. Unless
                                      the duration is specified, the
                                      default value (2min) is used.
-s, --setup [String]                Setup payload
--show-system-properties            Show SystemProperties for troubleshoot
--stacktrace                        Show Stacktrace when an exception
                                      happens
--stream                            Shortcut of --im REQUEST_STREAM
--take [Integer]                    Enable take(n)
-v, --version                       Print version
-w, --wiretap                       Enable wiretap
</code>
</pre>
</details>

### 2. Spring RSocket Project
#### Lazy Initialization
By default in Spring, all the defined beans, and their dependencies, are created when the application context is created.

By contrast, when we configure a bean with lazy initialization, the bean will only be created, and its dependencies injected, once they're needed.

<details>
<summary><code>lazy-initialization: true</code></summary>
<pre><code>
spring:
  main:
    lazy-initialization: true
</code></pre>
</details>

#### Controller
```kotlin
@Controller
class RSocketController {
    val server = "Server"
    val response = "Response"
    
    @MessageMapping("request-response")
    fun requestResponse(request: Message): Message {
        logger.info("Received request-response request: $request")
        return Message(server, response)
    }
}
```

#### Send a message to Sercer with RSocket Client CLI
```
$ java -jar rsc.jar \
  --debug --request \
  --data "{\"origin\":\"Client\",\"interaction\":\"Request\"}" \
  --route request-response \
  tcp://localhost:7000
```
```
2020-04-18 23:25:13.512 DEBUG --- [actor-tcp-nio-1] i.r.FrameLogger : sending ->
Frame => Stream ID: 1 Type: REQUEST_RESPONSE Flags: 0b100000000 Length: 69
Metadata:
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 10 72 65 71 75 65 73 74 2d 72 65 73 70 6f 6e 73 |.request-respons|
|00000010| 65                                              |e               |
+--------+-------------------------------------------------+----------------+
Data:
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 7b 22 6f 72 69 67 69 6e 22 3a 22 43 6c 69 65 6e |{"origin":"Clien|
|00000010| 74 22 2c 22 69 6e 74 65 72 61 63 74 69 6f 6e 22 |t","interaction"|
|00000020| 3a 22 52 65 71 75 65 73 74 22 7d                |:"Request"}     |
+--------+-------------------------------------------------+----------------+
2020-04-18 23:25:13.697 DEBUG --- [actor-tcp-nio-1] i.r.FrameLogger : receiving ->
Frame => Stream ID: 1 Type: NEXT_COMPLETE Flags: 0b1100000 Length: 81
Data:
         +-------------------------------------------------+
         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
+--------+-------------------------------------------------+----------------+
|00000000| 7b 22 6f 72 69 67 69 6e 22 3a 22 53 65 72 76 65 |{"origin":"Serve|
|00000010| 72 22 2c 22 69 6e 74 65 72 61 63 74 69 6f 6e 22 |r","interaction"|
|00000020| 3a 22 52 65 73 70 6f 6e 73 65 22 2c 22 69 6e 64 |:"Response","ind|
|00000030| 65 78 22 3a 30 2c 22 63 72 65 61 74 65 64 22 3a |ex":0,"created":|
|00000040| 31 35 38 37 32 31 39 39 31 33 7d                |1587219913}     |
+--------+-------------------------------------------------+----------------+
{"origin":"Server","interaction":"Response","index":0,"created":1587219913}
```

### 3. Fire and Forget
In this case, function should return `Unit` type
```
@MessageMapping("fire-and-forget")
fun findAndForget(request: Message): Unit
```

### 3. Stream
In this case, function should return `Flux` type
```
@MessageMapping("stream")
fun stream(request: Message): Flux<Message> = Flux.interval(Duration.ofSeconds(2))
                                              .map { index -> Message(server, stream, index) }
                                              .log()
}
```

Flux is a “Publisher” of data. It describes streams of 0 to N elements and offers a great many operators for processing streaming data.

### 4. Channel
Channels are bi-directional pipes that allow streams of data to flow in both direction.
With channels, a data stream from client-to-server can coexist alongside a data stream from server-to-client.

```
@MessageMapping("channel")
fun channel(config: Flux<Duration>): Flux<Message> =
        config
                .doOnNext { config -> logger.info("Frequency config is ${config.seconds} seconds.") }
                .switchMap { config -> Flux.interval(config)
                        .map{index -> Message(server, channel, index)}
                        .log()}
```

#### `doOnNext`
- Add behavior (side-effect) triggered when the Flux emits an item.

![doOnNext](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/doOnNextForFlux.svg)

#### `switchMap`
- Switch to a new Publisher generated via a Function whenever this Flux produces an item. As such, the elements from each generated Publisher are emitted in the resulting Flux.

![switchMap](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/doc-files/marbles/switchMap.svg)

## Features

- feature:1
- feature:2

## Requirement

## Usage

## Installation

## Licence

Released under the [MIT license](https://gist.githubusercontent.com/shinyay/56e54ee4c0e22db8211e05e70a63247e/raw/34c6fdd50d54aa8e23560c296424aeb61599aa71/LICENSE)

## Author

[shinyay](https://github.com/shinyay)
