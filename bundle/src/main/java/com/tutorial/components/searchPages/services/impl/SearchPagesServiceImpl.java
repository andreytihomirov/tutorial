package com.tutorial.components.searchPages.services.impl;

import com.day.cq.wcm.api.Page;
import com.tutorial.components.searchPages.model.CustomPage;
import com.tutorial.components.searchPages.services.SearchPagesService;
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
 * Created by andrey.tihomirov on 04.02.2016.
 */
@Service
@Component(immediate = true)
public class SearchPagesServiceImpl implements SearchPagesService {

    private static final Logger log = LoggerFactory.getLogger(SearchPagesServiceImpl.class);

    private String path;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public List<CustomPage> searchPagesByTag(String tag, int pageCount, Resource currentResource) {

        List<CustomPage> customPages = null;
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
                        String sql = "SELECT * FROM [nt:base] WHERE ISDESCENDANTNODE([" + path + "]) and CONTAINS([cq:tags], '" + tag + "')";
                        Query query = queryManager.createQuery(sql, Query.JCR_SQL2);
                        query.setLimit(pageCount);
                        QueryResult result = query.execute();
                        NodeIterator nodeIterator = result.getNodes();

                        if(nodeIterator.hasNext()) {
                            customPages = new ArrayList<CustomPage>();
                            while(nodeIterator.hasNext()) {
                                Node node = nodeIterator.nextNode();
                                CustomPage customPage = new CustomPage();
                                customPage.setTitle(node.getProperty("jcr:title").getString());
                                customPage.setLink(node.getParent().getPath());
                                customPages.add(customPage);
                            }
                        }
                    }
                } catch (RepositoryException e) {
                    log.error("Repository Exception: " + e);
                }
            }
        }

        return customPages;
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
