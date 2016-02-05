package com.tutorial.components.searchPages.services.impl;

import com.tutorial.components.searchPages.model.Page;
import com.tutorial.components.searchPages.services.SearchPagesService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
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

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public List<Page> searchPagesByTag(String tag, int pageCount) {

        List<Page> pages = null;
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
                        String sql = "SELECT * FROM [nt:base] WHERE ISDESCENDANTNODE([/content/geometrixx-outdoors]) and CONTAINS([cq:tags], '" + tag + "')";
                        Query query = queryManager.createQuery(sql, Query.JCR_SQL2);
                        query.setLimit(pageCount);
                        QueryResult result = query.execute();
                        NodeIterator nodeIterator = result.getNodes();

                        if(nodeIterator.hasNext()) {
                            pages = new ArrayList<Page>();
                            while(nodeIterator.hasNext()) {
                                Node node = nodeIterator.nextNode();
                                Page page = new Page();
                                page.setTitle(node.getProperty("jcr:title").getString());
                                page.setLink(node.getPath());
                                pages.add(page);
                            }
                        }
                    }
                } catch (RepositoryException e) {
                    log.error("Repository Exception: " + e);
                }
            }
        }

        return pages;
    }

}
