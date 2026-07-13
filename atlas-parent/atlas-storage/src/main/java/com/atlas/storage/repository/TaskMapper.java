package com.atlas.storage.repository;

import com.atlas.core.task.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    
    List<Task> selectByProjectId(String projectId);
    
    List<Task> selectByStatus(Task.TaskStatus status);
    
    List<Task> selectByWorkflowId(String workflowId);
    
    @Select("SELECT * FROM task WHERE id = #{id}")
    Task selectById(String id);
}
