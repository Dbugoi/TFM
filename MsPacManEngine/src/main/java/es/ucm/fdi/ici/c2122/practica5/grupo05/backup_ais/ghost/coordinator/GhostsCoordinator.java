package es.ucm.fdi.ici.c2122.practica5.grupo05.backup_ais.ghost.coordinator;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsCoordinator {
    private enum GhostIntention {
        AMBUSH, RUN_AWAY, NONE
    }

    private class GhostInfo {
        private final GHOST ghost;
        private GhostIntention intention;
        private int lastUpdateTick;
        private MOVE nextMove;

        public GhostInfo(GHOST ghost) {
            this.ghost = ghost;
            intention = GhostIntention.NONE;
            lastUpdateTick = -1;
        }
    }

    private final Map<GHOST, GhostInfo> ghostToInfo;
    private int lastGameTick = 0;

    public GhostsCoordinator() {
        this.ghostToInfo = new EnumMap<>(GHOST.class);
        for (GHOST ghost : GHOST.values()) {
            ghostToInfo.put(ghost, new GhostInfo(ghost));
        }
    }

    public MOVE requestAmbushMove(Game game, GHOST ghost) {
        if (lastGameTick != game.getTotalTime())
            resetOldIntentions();

        updateIntention(ghost, GhostIntention.AMBUSH);
        List<GHOST> newAmbushers = ghostsWithIntentionTo(GhostIntention.AMBUSH);

        Map<GHOST, MOVE> newMoves = AmbushCoordinator.getAmbushMoves(game, newAmbushers);
        for (GHOST ambusher : newAmbushers) {
            ghostToInfo.get(ambusher).nextMove = newMoves.get(ambusher);
        }

        lastGameTick = game.getTotalTime();

        return ghostToInfo.get(ghost).nextMove;
    }

    public MOVE requestRunAwayMove(Game game, GHOST ghost) {
        if (lastGameTick != game.getTotalTime())
            resetOldIntentions();

        updateIntention(ghost, GhostIntention.RUN_AWAY);
        List<GHOST> newRunaways = ghostsWithIntentionTo(GhostIntention.RUN_AWAY);

        Map<GHOST, MOVE> newMoves = RunawayCoordinator.getRunawayMoves(game, newRunaways);
        for (GHOST ambusher : newRunaways) {
            ghostToInfo.get(ambusher).nextMove = newMoves.get(ambusher);
        }

        lastGameTick = game.getTotalTime();

        return ghostToInfo.get(ghost).nextMove;
    }

    private void resetOldIntentions() {
        for (GhostInfo info : ghostToInfo.values()) {
            if (info.lastUpdateTick < lastGameTick)
                info.intention = GhostIntention.NONE;
        }
    }

    private void updateIntention(GHOST ghost, GhostIntention intention) {
        ghostToInfo.get(ghost).intention = intention;
        ghostToInfo.get(ghost).lastUpdateTick = lastGameTick;
    }

    private List<GHOST> ghostsWithIntentionTo(GhostIntention intention) {
        List<GHOST> ghosts = new ArrayList<>(GHOST.values().length);
        for (GhostInfo info : ghostToInfo.values()) {
            if (info.intention == intention)
                ghosts.add(info.ghost);
        }
        return ghosts;
    }

}
