package com.sprints.UniversityRoomBookingSystem.Exception;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String message) {
        super(message);
    }
}
