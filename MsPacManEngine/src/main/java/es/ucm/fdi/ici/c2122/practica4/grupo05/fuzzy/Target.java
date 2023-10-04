package es.ucm.fdi.ici.c2122.practica4.grupo05.fuzzy;

import pacman.game.Constants.MOVE;

public class Target implements Comparable<Target> {
    private int node;
    private MOVE lastMove;

    public Target(int currentNode, MOVE lastMove) {
        this.setNode(currentNode);
        this.setLastMove(lastMove);
    }

    public int getNode() {
        return node;
    }

    public void setNode(int currentNode) {
        this.node = currentNode;
    }

    public MOVE getLastMove() {
        return lastMove;
    }

    public void setLastMove(MOVE lastMove) {
        this.lastMove = lastMove;
    }

    @Override
    public int compareTo(Target o) {
        if (node != o.node)
            return Integer.compare(node, o.node);
        else if (lastMove == o.lastMove)
            return 0;
        else {
            // UP > RIGHT > LEFT > DOWN > NEUTRAL
            switch (lastMove) {
                case UP:
                    return 1;
                case RIGHT:
                    return (o.lastMove == MOVE.UP) ? -1 : 1;
                case LEFT:
                    return (o.lastMove == MOVE.DOWN || o.lastMove == MOVE.NEUTRAL) ? 1 : -1;
                case DOWN:
                    return (o.lastMove == MOVE.NEUTRAL) ? 1 : -1;
                case NEUTRAL:
                    return -1;
                default:
                    return 1;
            }
        }
    }

    @Override
    public int hashCode() {
        // autogenerado
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lastMove == null) ? 0 : lastMove.hashCode());
        result = prime * result + node;
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        // autogenerado
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Target other = (Target) obj;
        if (lastMove != other.lastMove)
            return false;
        return node == other.node;
    }

}
