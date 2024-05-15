package org.design_manager_project.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageUtils {
    public static PageRequest buildPageRequest(Integer page, Integer limit) {
        page = (page == null || page == 0) ? Constants.DEFAULT_PAGE : page - Constants.DEFAULT_PAGE;
        limit = (limit == null || limit == 0) ? Constants.DEFAULT_LIMIT : limit;
        return PageRequest.of(page, limit);
    }

    public PageRequest buildPageRequest(Integer page, Integer limit, Sort sort) {
        page = page == null ? Constants.DEFAULT_PAGE : page - Constants.DEFAULT_PAGE;
        limit = limit == null ? Constants.DEFAULT_LIMIT : limit;
        return PageRequest.of(page, limit, sort);
    }
}
