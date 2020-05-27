package com.etu.infrastructure.workflow.strategy.sql.step;

import com.etu.infrastructure.state.dto.runtime.rm.RModelLink;
import com.etu.infrastructure.state.dto.runtime.rm.RModelLinkSide;
import com.etu.infrastructure.state.dto.runtime.rm.RModelRelation;
import com.etu.infrastructure.state.dto.runtime.rm.RModelState;
import com.etu.infrastructure.workflow.strategy.sql.dto.SqlGenerationState;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Component
public class SortRelationsStep implements GenerateSqlStep {

    @Override
    public void process(RModelState source, SqlGenerationState target) {
        Map<RModelRelation, List<RModelRelation>> adjacencyList = buildAdjacencyList(source);
        Map<RModelRelation, VertexState> vertexState = buildVertexState(source);
        Deque<RModelRelation> orderedRelations = new ArrayDeque<>();

        source.getRelations().forEach(table -> makeStep(table, adjacencyList, vertexState, orderedRelations));

        target.setSourceRelations(new ArrayList<>(orderedRelations));
    }

    private Map<RModelRelation, List<RModelRelation>> buildAdjacencyList(RModelState rmState) {
        Map<RModelRelation, List<RModelRelation>> adjacencyList = new HashMap<>();

        rmState.getRelations()
                .forEach(table -> adjacencyList.put(table, getAdjacentTables(table, rmState)));

        return adjacencyList;
    }

    private List<RModelRelation> getAdjacentTables(RModelRelation table, RModelState rmState) {
        List<RModelLink> relations = rmState.getLinks().stream()
                .filter(fromTable(table))
                .collect(toList());

        return relations.stream()
                .map(RModelLink::getLinkSideTo)
                .map(RModelLinkSide::getRelation)
                .collect(toList());
    }

    private Predicate<RModelLink> fromTable(RModelRelation table) {
        return relation -> Objects.equals(relation.getLinkSideFrom().getRelation(), table);
    }

    private Map<RModelRelation, VertexState> buildVertexState(RModelState rmState) {
        Map<RModelRelation, VertexState> vertexState = new HashMap<>();
        rmState.getRelations().forEach(table -> vertexState.put(table, VertexState.WHITE));

        return vertexState;
    }

    private void makeStep(RModelRelation table, Map<RModelRelation, List<RModelRelation>> graphStructure, Map<RModelRelation, VertexState> state, Deque<RModelRelation> orderedList) {
        if (state.get(table) == VertexState.BLACK) {
            return;
        }

        if (state.get(table) == VertexState.GRAY) {
            throw new IllegalStateException();
        }

        if (state.get(table) == VertexState.WHITE) {
            state.put(table, VertexState.GRAY);
            graphStructure.get(table)
                    .forEach(i -> makeStep(i, graphStructure, state, orderedList));
            state.put(table, VertexState.BLACK);
            orderedList.addFirst(table);
        }
    }

    private enum VertexState {
        WHITE, GRAY, BLACK
    }
}
