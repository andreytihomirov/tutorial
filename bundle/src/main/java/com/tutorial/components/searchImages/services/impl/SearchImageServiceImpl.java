package com.tutorial.components.searchImages.services.impl;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.foundation.Image;
import com.tutorial.components.searchImages.services.SearchImageService;
import com.tutorial.components.searchPages.model.CustomPage;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrey.tihomirov on 11.02.2016.
 */
@Service
@Component(immediate = true)
public class SearchImageServiceImpl implements SearchImageService {

    private static final Logger log = LoggerFactory.getLogger(SearchImageServiceImpl.class);

    private String path;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public List<Image> searchPagesByRendition(int width, int height, Resource currentResource) {
        List<Image> images = null;

        ResourceResolver resourceResolver = null;

        try {
            resourceResolver = resourceResolverFactory.getAdministrativeResourceResolver(null);
        } catch (LoginException e) {
            log.error("Can't instantiate resourceResolver");
        }

        if(resourceResolver != null) {
            Session session = resourceResolver.adaptTo(Session.class);

            if(session != null) {
                QueryManager queryManager;

                try {
                    queryManager = session.getWorkspace().getQueryManager();
                    if(queryManager != null) {
                        prepareSearchPath(currentResource);
                        String sql = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([/content/dam]) and CONTAINS(s.*, 'thumbnail.100.100.png')";
                        Query query = queryManager.createQuery(sql, Query.JCR_SQL2);
                        QueryResult result = query.execute();
                        NodeIterator nodeIterator = result.getNodes();

                        if(nodeIterator.hasNext()) {
                            images = new ArrayList<Image>();
                            while(nodeIterator.hasNext()) {
                                Node node = nodeIterator.nextNode();
                            }
                        }

                    }
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
        }

        return images;
    }

    private void prepareSearchPath(Resource resource) {
        Page page;
        page = resource.adaptTo(Page.class);

        if(page != null) {
            com.day.cq.wcm.api.Page absoluteParent = page.getAbsoluteParent(1);
            if(absoluteParent != null) {
                path = absoluteParent.getPath();
            }
        } else {
            prepareSearchPath(resource.getParent());
        }
    }

}
