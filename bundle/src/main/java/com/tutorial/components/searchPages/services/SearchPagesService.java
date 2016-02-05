package com.tutorial.components.searchPages.services;

import com.tutorial.components.searchPages.model.Page;

import java.util.List;

/**
 * Created by andrey.tihomirov on 04.02.2016.
 */
public interface SearchPagesService {

    List<Page> searchPagesByTag(String tag, int pageCount);
}
