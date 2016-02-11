package com.tutorial.components.searchPages.services;

import org.apache.sling.api.resource.Resource;

import java.util.List;

/**
 * Created by andrey.tihomirov on 04.02.2016.
 */
public interface SearchPagesService {

    // old task
    // List<CustomPage> searchPagesByTag(String tag, int pageCount, Resource currentResource);

    // new task
    List<String> searchPagesByTag(int width, int height, Resource currentResource);
}
