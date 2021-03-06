package org.infinispan.interceptors;

import org.infinispan.Cache;
import org.infinispan.factories.annotations.Inject;
import org.infinispan.factories.annotations.Start;
import org.infinispan.factories.annotations.Stop;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Anyone using the {@link org.infinispan.interceptors.SequentialInterceptorChain#addInterceptor(SequentialInterceptor, int)} method (or any of its
 * overloaded forms) or registering custom interceptors via XML should extend this base class when creating their own 
 * custom interceptors.
 * <p />
 * Annotations on custom interceptors, including {@link Inject}, {@link Start} and {@link Stop}
 * will not be respected and callbacks will not be made.
 * <p />
 * Instead, custom interceptor authors should extend this base class to gain access to {@link Cache} and {@link EmbeddedCacheManager},
 * from which other components may be accessed.  Further, lifecycle should be implemented by overriding {@link #start()}
 * and {@link #stop()} as defined in this class.
 *
 * @author Dan Berindei
 * @since 9.0
 */
public class BaseCustomSequentialInterceptor extends DDSequentialInterceptor {
   protected Cache<?, ?> cache;
   protected EmbeddedCacheManager embeddedCacheManager;

   @Inject
   private void setup(Cache<?, ?> cache, EmbeddedCacheManager embeddedCacheManager) {
      if (this.cache != null && this.cache != cache) {
         // see https://issues.jboss.org/browse/ISPN-5335
         throw new IllegalStateException("Setting up the interceptor second time;" +
               "this could be caused by the same instance of interceptor used by several caches.");
      }
      this.cache = cache;
      this.embeddedCacheManager = embeddedCacheManager;
   }

   @Start
   protected void start() {
      // Meant to be overridden
   }

   @Stop
   protected void stop() {
      // Meant to be overridden
   }
}
