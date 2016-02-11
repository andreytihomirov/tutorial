package com.tutorial.components.searchImages.services;

import com.day.cq.wcm.foundation.Image;
import org.apache.sling.api.resource.Resource;

import java.util.List;

/**
 * Created by andrey.tihomirov on 11.02.2016.
 */
public interface SearchImageService {

    List<Image> searchPagesByRendition(int width, int height, Resource currentResource);
}
