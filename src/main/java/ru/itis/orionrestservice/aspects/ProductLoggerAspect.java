package ru.itis.orionrestservice.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.itis.orionrestservice.models.AccessLogEntry;
import ru.itis.orionrestservice.models.Product;
import ru.itis.orionrestservice.repositories.AccessLogEntryRepository;
import ru.itis.orionrestservice.repositories.ProductRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Component
@Aspect
@Slf4j
public class ProductLoggerAspect {

    private final AccessLogEntryRepository logEntryRepository;

    public ProductLoggerAspect(AccessLogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    @AfterReturning(pointcut = "execution(* ru.itis.orionrestservice.repositories.ProductRepository.*(..))", returning = "returnValue")
    private void logRepo(JoinPoint jp, Object returnValue) {
        if (returnValue == null) {
            return;
        }
        Product returnEntity = null;
        if (returnValue instanceof Optional) {
            if (((Optional<?>) returnValue).isEmpty()) {
                return;
            }
            else {
                returnEntity = (Product) ((Optional<?>) returnValue).get();
            }

        }
        else if(returnValue instanceof Product) {
            returnEntity = (Product) returnValue;
        }
        String methodName = jp.getSignature().toShortString();
        methodName = methodName.substring(methodName.indexOf(".") + 1, methodName.indexOf("("));
        if(methodName.equals("getProductsToDelete")) {
            return;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal.equals("anonymousUser")) ?
                (String) principal :
                ((User) principal).getUsername();

        log.info("productRepository is being called");
        log.info("methodName: " + methodName);
        log.info("with args: " + Arrays.toString(jp.getArgs()));
        log.info("by user: " + username);
        log.info("result: " + returnValue);

        AccessLogEntry logEntry = AccessLogEntry.builder()
                .product(returnEntity)
                .timestamp(Instant.now())
                .username(username)
                .action(methodName)
                .build();
        logEntryRepository.save(logEntry);
    }
}
