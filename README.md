# Java Uncheck Exceptions

## Problem

In Java language (source code) you can't throw checked exceptions if method doesn't declare that.

Typical solution is to wrap it into `RuntimeException`, but this way you [hide exception type](https://github.com/ReactiveX/RxJava/issues/5965).

There are big production projects that have a method that wrap exception into `RuntimeException` to avoid declaring checked exceptions in methods:

- [RxJava 2.x ExceptionHelper.wrapOrThrow](https://github.com/ReactiveX/RxJava/blob/v2.1.12/src/main/java/io/reactivex/internal/util/ExceptionHelper.java#L38)
- [Guava Throwables.propagate](https://github.com/google/guava/wiki/ThrowablesExplained), they even deprecated it and propose manual wrapping into `RuntimeException()`

## Goal

Throw checked `Exception`s and `Throwable`s **without wrapping** and **without declaring** it in method description.

## Implementation

From Java bytecode and JVM POV it's totally ok to throw checked exceptions anywhere, see [`ATHROW` opcode documentation](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.athrow).

The only problem is that you can't write Java code that does this because Java compiler won't let you compile it…

So I codegen bytecode that does it with [ASM](http://asm.ow2.org/) and then create a jar!

## API

Library has 0 dependencies, targets Java 6 and provides a single static method:

```java
UncheckExceptions.thr0w(Throwable t);
```

I highly recommend statically import `thr0w` method, so it'll look like standard `throw`:

```java
import static com.artemzin.uncheckexceptions.UncheckExceptions.thr0w;

public static void main(String[] args) { // Note that it doesn't declare `throws Exception`!
    thr0w(new Exception());
}
```

## Download

I haven't published it to Maven Central yet (hopefully will do soon, Gradle publishing is ridiculous).

However you can download jar to play with from [Releases page](https://github.com/artem-zinnatullin/java-uncheck-exceptions/releases).

## Contributing

### Project Structure

Project has two modules:

- `uncheck-exceptions-codegen` generates bytecode that can throw checked exception as unchecked (has unit tests).
- `uncheck-exceptions-integration-test` consumes uncheck-exceptions.jar to and runs integration tests to make sure library works as expected.

### Building

To build project you need Bash and JDK 7+, then run:

```console
ci/build.sh
```

It'll output something like:

```console
…
Done!
uncheck-exceptions.jar can be found in build/libs directory.
```

### IDE

You can import project in IntelliJ or your JVM IDE of choice.

Note:

>`uncheck-exceptions-integration-test` might be red in IDE until you run `ci/build.sh` because it links to jar that needs to be generated first.
>PRs to wire it properly via Gradle are very welcome!
