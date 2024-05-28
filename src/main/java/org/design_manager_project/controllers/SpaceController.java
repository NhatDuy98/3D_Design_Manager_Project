package org.design_manager_project.controllers;

import org.design_manager_project.dtos.ApiResponse;
import org.design_manager_project.dtos.space.SpaceDTO;
import org.design_manager_project.filters.SpaceFilter;
import org.design_manager_project.models.entity.Space;
import org.design_manager_project.services.SpaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController extends BaseController<Space, SpaceDTO, SpaceFilter, UUID>{
    private final SpaceService spaceService;
    protected SpaceController(SpaceService spaceService) {
        super(spaceService);
        this.spaceService = spaceService;
    }

    @GetMapping("/users/{id}")
    public ApiResponse getAllSpaceWithUserId(@PathVariable("id") UUID uuid, Pageable pageable){
        Page<SpaceDTO> spaces = spaceService.getAllSpaceWithUserId(pageable, uuid);

        if (spaces.isEmpty()){
            List<ApiResponse> apiResponses = new ArrayList<>();

            return ApiResponse.success(apiResponses);
        }

        return ApiResponse.success(spaces);
    }

    @GetMapping("/{spaceId}/users/{userId}")
    public ApiResponse getSpaceWithUserId(
            @PathVariable("spaceId") UUID spaceId,
            @PathVariable("userId") UUID userId
    ){
        Optional<SpaceDTO> spaceDTO = spaceService.getSpaceWithUserId(spaceId, userId);

        if (spaceDTO.isEmpty()){
            return ApiResponse.notFound();
        }

        return ApiResponse.success(spaceDTO);
    }

}
