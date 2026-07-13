package com.atlas.storage.repository;

import com.atlas.core.project.Project;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {
    
    List<Project> selectByWorkspaceId(String workspaceId);
    
    List<Project> selectByStatus(Project.ProjectStatus status);
    
    @Select("SELECT * FROM project WHERE name = #{name} LIMIT 1")
    Project selectByName(String name);
}
