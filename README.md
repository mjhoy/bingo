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
$ sbt ~fastLinkJS
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

Assumes a working `aws` command with access to a S3 bucket and a
cloudfront distribution.

Set `S3_BUCKET_NAME` and `CLOUDFRONT_DISTRIBUTION_ID` env vars, then run:

```sh
export S3_BUCKET_NAME=<bucket>
export CLOUDFRONT_DISTRIBUTION_ID=<cloudfront_id>
./scripts/deploy
```

Also deploys on merges to `main` on Github.
