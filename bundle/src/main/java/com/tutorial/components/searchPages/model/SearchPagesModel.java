package com.tutorial.components.searchPages.model;

import com.tutorial.components.searchPages.services.SearchPagesService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by andrey.tihomirov on 03.02.2016.
 */

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SearchPagesModel {

    @Inject
    private SearchPagesService searchPagesService;

    @Inject
    private String title;

    @Inject
    private String htmlTag;

    @Inject
    private int pageCount;

    @Inject
    private String[] tags;

    private List<Page> pages;

    @PostConstruct
    public void activate() {
        pages = searchPagesService.searchPagesByTag(tags[0], pageCount);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlTag() {
        return htmlTag;
    }

    public void setHtmlTag(String htmlTag) {
        this.htmlTag = htmlTag;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
