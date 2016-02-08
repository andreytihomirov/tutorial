package com.tutorial.components.searchPages.services;

import com.tutorial.components.searchPages.model.CustomPage;
import org.apache.sling.api.resource.Resource;

import java.util.List;

/**
 * Created by andrey.tihomirov on 04.02.2016.
 */
public interface SearchPagesService {

    List<CustomPage> searchPagesByTag(String tag, int pageCount, Resource currentResource);
}
