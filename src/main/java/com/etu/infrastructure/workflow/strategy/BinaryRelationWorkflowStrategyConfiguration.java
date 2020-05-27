package com.etu.infrastructure.workflow.strategy;

import com.etu.infrastructure.workflow.strategy.erm.relation.ERModelAddBinaryRelationWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.erm.relation.ERModelAddNSideRelationWorkflowStrategy;
import com.etu.infrastructure.workflow.strategy.erm.relation.ERModelRelationWorkflowStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.etu.infrastructure.workflow.service.WorkflowType.*;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSideType.*;
import static com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationType.*;

@Configuration
public class BinaryRelationWorkflowStrategyConfiguration {
    @Bean
    public ERModelRelationWorkflowStrategy erModelAddOneToOneRelationWorkflowStrategy() {
        return new ERModelAddBinaryRelationWorkflowStrategy(ONE_TO_ONE, ONE, ONE, ERM_ADD_ONE_TO_ONE_RELATION);
    }

    @Bean
    public ERModelRelationWorkflowStrategy erModelAddOneToManyRelationWorkflowStrategy() {
        return new ERModelAddBinaryRelationWorkflowStrategy(ONE_TO_MANY, ONE, MANY, ERM_ADD_ONE_TO_MANY_RELATION);
    }

    @Bean
    public ERModelRelationWorkflowStrategy erModelAddDependencyRelationWorkflowStrategy() {
        return new ERModelAddBinaryRelationWorkflowStrategy(DEPENDS_ON, MAIN, DEPENDENT, ERM_ADD_DEPENDENCY_RELATION);
    }

    @Bean
    public ERModelRelationWorkflowStrategy erModelAddCategoryRelationWorkflowStrategy() {
        return new ERModelAddBinaryRelationWorkflowStrategy(CATEGORY, PARENT, SPECIFIC, ERM_ADD_CATEGORY_RELATION);
    }

    @Bean
    public ERModelRelationWorkflowStrategy erModelAddManyToManyRelationWorkflowStrategy() {
        return new ERModelAddNSideRelationWorkflowStrategy(MANY_TO_MANY, MANY, ERM_ADD_MANY_TO_MANY_RELATION);
    }
}
