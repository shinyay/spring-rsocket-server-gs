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
