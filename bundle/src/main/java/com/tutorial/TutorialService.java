package com.tutorial;

/**
 * A simple service interface
 */
public interface TutorialService {
    
    /**
     * @return the name of the underlying JCR repository implementation
     */
    public String getRepositoryName();

}