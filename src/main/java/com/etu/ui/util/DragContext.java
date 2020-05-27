package com.etu.ui.util;

import javafx.scene.Node;

public class DragContext {
    private double deltaX;
    private double deltaY;
    private Node draggedNode;

    public DragContext(double deltaX, double deltaY, Node draggedNode) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.draggedNode = draggedNode;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public Node getDraggedNode() {
        return draggedNode;
    }
}
