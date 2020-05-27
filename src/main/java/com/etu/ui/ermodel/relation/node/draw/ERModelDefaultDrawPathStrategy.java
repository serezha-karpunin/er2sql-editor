package com.etu.ui.ermodel.relation.node.draw;

import com.etu.infrastructure.state.dto.runtime.erm.ERModelRelationSide;
import com.etu.ui.ermodel.canvas.ERModelJoinPoint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import org.springframework.stereotype.Component;

@Component
public class ERModelDefaultDrawPathStrategy {
    public Path getPath(ERModelRelationSide relationSide, ERModelJoinPoint jp, double toX, double toY) {

        switch (jp.getSide()) {
            case TOP:
            case BOTTOM:
                return buildVerticalPath(jp.getX(), jp.getY(), toX, toY, relationSide.isMandatory());
            case LEFT:
            case RIGHT:
            default:
                return buildHorizontalPath(jp.getX(), jp.getY(), toX, toY, relationSide.isMandatory());
        }
    }


    private Path buildVerticalPath(double fromX, double fromY, double toX, double toY, boolean mandatory) {
        Path path = new Path();

        path.getElements().add(new MoveTo(fromX, fromY));
        path.getElements().add(new LineTo(fromX, toY));
        path.getElements().add(new LineTo(toX, toY));

        path.setStrokeWidth(2d);

        if (!mandatory) {
            path.getStrokeDashArray().add(5d);
        }

        return path;
    }

    private Path buildHorizontalPath(double fromX, double fromY, double toX, double toY, boolean mandatory) {
        Path path = new Path();

        path.getElements().add(new MoveTo(fromX, fromY));
        path.getElements().add(new LineTo(toX, fromY));
        path.getElements().add(new LineTo(toX, toY));

        path.setStrokeWidth(2d);

        if (!mandatory) {
            path.getStrokeDashArray().add(5d);
        }

        return path;
    }
}
