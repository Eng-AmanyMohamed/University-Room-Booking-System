package com.sprints.UniversityRoomBookingSystem.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Constraint(validatedBy = NoOverlapValidator.class) // Specify the validator class
@Target({ ElementType.TYPE }) // This annotation will be used on classes
@Retention(RetentionPolicy.RUNTIME) // This annotation will be available at runtime
public @interface NoOverlap {
    String message() default "Booking times overlap with an existing booking"; // Default error message
    Class<?>[] groups() default {}; // Grouping constraints
    Class<? extends Payload>[] payload() default {}; // Additional data
}