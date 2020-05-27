package com.etu.infrastructure.workflow.strategy.transform.step;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelState;
import com.etu.infrastructure.workflow.strategy.transform.dto.TransformationState;

public interface TransformStep {
    void process(ERModelState source, TransformationState target);
}
