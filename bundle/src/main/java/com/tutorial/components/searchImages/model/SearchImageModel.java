package com.tutorial.components.searchImages.model;

import com.day.cq.wcm.foundation.Image;
import com.tutorial.components.searchImages.services.SearchImageService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by andrey.tihomirov on 11.02.2016.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchImageModel {

    @Inject
    private SearchImageService searchImageService;

    @Self
    private Resource currentResource;

    private List<Image> images;

    @PostConstruct
    public void activate() {
        searchImageService.searchPagesByRendition(100, 100, currentResource);
    }
}
