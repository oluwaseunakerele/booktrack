package com.booktrack.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Logs entry/exit of controller, service and repository methods with duration.
 * Works together with the request filter (rid in MDC) so every line for a
 * request shares the same correlation id.
 */
@Aspect
@Component
public class LoggingAspect {

  private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

  // apply to your app packages; adjust package names if yours differ
  private static final String POINTCUT =
      "execution(* com.booktrack..controller..*(..)) || " +
      "execution(* com.booktrack..service..*(..))    || " +
      "execution(* com.booktrack..repository..*(..))";

  @Around(POINTCUT)
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    long t0 = System.currentTimeMillis();
    String sig = pjp.getSignature().toShortString();
    log.debug("-> {}", sig);
    try {
      Object out = pjp.proceed();
      log.debug("<- {} {}ms", sig, System.currentTimeMillis() - t0);
      return out;
    } catch (Exception e) {
      log.error("xx {} failed after {}ms", sig, System.currentTimeMillis() - t0, e);
      throw e;
    }
  }
}
