corpbingo
=========

Play some corporate bingo.

Uses [scalajs](https://www.scala-js.org) and the
[laminar](https://laminar.dev) UI library.

## Requirements

- [sbt](https://www.scala-sbt.org)
- [node](https://nodejs.org/en/)

## Development

### Running a local server

To compile the JavaScript, run a sbt server, and then the `~fastLinkJS` command:

```
$ sbt
[info] welcome to sbt 1.4.7 (Azul Systems, Inc. Java 1.8.0_202)
[...]
[info] started sbt server
sbt:Bingo> ~fastLinkJS
```

Now, you need an HTTP server serving from the root of the repository. The
simplest is to use Python's `http.server` (Python 3):

```sh
python -m http.server
```

Now visit http://localhost:8080.

### Tests

There are none! Yet. Just run `sbt checkAll` to make sure the code passes
lint. To try to fix errors, run `sbt fixAll`.

## Deploying

With appropriate S3 permissions:

```sh
./scripts/deploy
```
