package org.infinispan.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Utility methods for testing expected exceptions.
 *
 * @author Dan Berindei
 * @since 8.2
 */
public class Exceptions {
   public interface ExceptionRunnable {
      void run() throws Exception;
   }

   public static void assertException(Class<? extends Throwable> exceptionClass, Throwable t) {
      if (t == null) {
         throw new AssertionError("Should have thrown an " + exceptionClass, t);
      }
      if (t.getClass() != exceptionClass) {
         throw new AssertionError(
               "Wrong exception thrown: expected:<" + exceptionClass + ">, actual:<" + t.getClass() + ">", t);
      }
   }

   public static void assertException(Class<? extends Throwable> exceptionClass, String messageRegex,
         Throwable t) {
      assertException(exceptionClass, t);
      Pattern pattern = Pattern.compile(messageRegex);
      if (!pattern.matcher(t.getMessage()).matches()) {
         throw new AssertionError(
               "Wrong exception message: expected:<" + messageRegex + ">, actual:<" + t.getMessage() + ">",
               t);
      }
   }

   public static void assertException(Class<? extends Throwable> wrapperExceptionClass,
         Class<? extends Throwable> exceptionClass, Throwable t) {
      assertException(wrapperExceptionClass, t);
      assertException(exceptionClass, t.getCause());
   }

   public static void assertException(Class<? extends Throwable> wrapperExceptionClass,
         Class<? extends Throwable> exceptionClass, String messageRegex, Throwable t) {
      assertException(wrapperExceptionClass, t);
      assertException(exceptionClass, messageRegex, t.getCause());
   }

   public static void expectException(Class<? extends Throwable> exceptionClass, String messageRegex,
         ExceptionRunnable runnable) {
      Throwable t = extractException(runnable);
      assertException(exceptionClass, messageRegex, t);
   }

   public static void expectException(Class<? extends Throwable> wrapperExceptionClass,
         Class<? extends Throwable> exceptionClass,
         String messageRegex, ExceptionRunnable runnable) {
      Throwable t = extractException(runnable);
      assertException(wrapperExceptionClass, t);
      assertException(exceptionClass, messageRegex, t.getCause());
   }

   public static void expectException(Class<? extends Throwable> exceptionClass, ExceptionRunnable runnable) {
      Throwable t = extractException(runnable);
      assertException(exceptionClass, t);
   }

   public static void expectException(Class<? extends Throwable> wrapperExceptionClass,
         Class<? extends Throwable> exceptionClass, ExceptionRunnable runnable) {
      Throwable t = extractException(runnable);
      assertException(wrapperExceptionClass, t);
      assertException(exceptionClass, t.getCause());
   }

   public static void expectExecutionException(Class<? extends Throwable> exceptionClass, String messageRegex,
         Future<?> future) {
      Throwable t = extractException(() -> future.get(10, TimeUnit.SECONDS));
      assertException(ExecutionException.class, t);
      assertException(exceptionClass, messageRegex, t.getCause());
   }

   public static void expectExecutionException(Class<? extends Throwable> wrapperExceptionClass,
         Class<? extends Throwable> exceptionClass, String messageRegex, Future<?> future) {
      Throwable t = extractException(() -> future.get(10, TimeUnit.SECONDS));
      assertException(ExecutionException.class, t);
      assertException(wrapperExceptionClass, t.getCause());
      assertException(exceptionClass, messageRegex, t.getCause().getCause());
   }

   public static void expectExecutionException(Class<? extends Throwable> wrapperExceptionClass2,
         Class<? extends Throwable> wrapperExceptionClass, Class<? extends Throwable> exceptionClass,
         String messageRegex, Future<?> future) {
      Throwable t = extractException(() -> future.get(10, TimeUnit.SECONDS));
      assertException(ExecutionException.class, t);
      assertException(wrapperExceptionClass2, t.getCause());
      assertException(wrapperExceptionClass, exceptionClass, messageRegex, t.getCause().getCause());
   }

   public static void expectExecutionException(Class<? extends Throwable> exceptionClass, Future<?> future) {
      Throwable t = extractException(() -> future.get(10, TimeUnit.SECONDS));
      assertException(ExecutionException.class, t);
      assertException(exceptionClass, t.getCause());
   }

   public static void expectExecutionException(Class<? extends Throwable> wrapperExceptionClass,
         Class<? extends Throwable> exceptionClass, Future<?> future) {
      Throwable t = extractException(() -> future.get(10, TimeUnit.SECONDS));
      assertException(ExecutionException.class, t);
      assertException(wrapperExceptionClass, t.getCause());
      assertException(exceptionClass, t.getCause().getCause());
   }

   public static void expectExecutionException(Class<? extends Throwable> wrapperExceptionClass2,
         Class<? extends Throwable> wrapperExceptionClass, Class<? extends Throwable> exceptionClass,
         Future<?> future) {
      Throwable t = extractException(() -> future.get(10, TimeUnit.SECONDS));
      assertException(ExecutionException.class, t);
      assertException(wrapperExceptionClass2, t.getCause());
      assertException(wrapperExceptionClass, exceptionClass, t.getCause().getCause());
   }

   private static Throwable extractException(ExceptionRunnable runnable) {
      Throwable exception = null;
      try {
         runnable.run();
      } catch (Throwable t) {
         exception = t;
      }
      return exception;
   }
}
