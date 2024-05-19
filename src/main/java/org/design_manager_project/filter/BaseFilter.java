package org.design_manager_project.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.design_manager_project.utils.Constants;
import org.design_manager_project.utils.PageUtils;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseFilter {
   private int limit = Constants.DEFAULT_LIMIT;
   private int page = Constants.DEFAULT_PAGE;
   private String search;

   public Pageable getPageable(){
      return PageUtils.buildPageRequest(this.page, this.limit);
   }
}
