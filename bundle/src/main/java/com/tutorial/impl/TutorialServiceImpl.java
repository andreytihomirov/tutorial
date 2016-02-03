package com.tutorial.impl;

import javax.jcr.Repository;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.jcr.api.SlingRepository;

import com.tutorial.TutorialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * One implementation of the {@link com.tutorial.TutorialService}. Note that
 * the repository is injected, not retrieved.
 */
@Service
@Component(metatype = false)
public class TutorialServiceImpl implements TutorialService {

    private Logger log = LoggerFactory.getLogger(TutorialServiceImpl.class);
    
    @Reference
    private SlingRepository repository;

    public String getRepositoryName() {

        return repository.getDescriptor(Repository.REP_NAME_DESC);
    }

}
