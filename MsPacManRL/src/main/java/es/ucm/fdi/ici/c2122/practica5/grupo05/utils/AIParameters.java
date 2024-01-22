package es.ucm.fdi.ici.c2122.practica5.grupo05.utils;

/**
 * Parámetros para las inteligencias artificiales de MsPacman y los fantasmas.
 */
public class AIParameters {
    private AIParameters() {}

    public static final int LIMIT_FOR_EDIBLE_GHOST = 30;
    public static final int LIMIT_PILL = 300;
    public static final int LIMIT_DIST_TO_CAGE = 55;
    
    // ESTAS LAS USO PARA EL GHOST, NO USO LAS OTRAS POR SI SON DEL PACMAN
    public static final int LIMIT_TO_EDIBLES = 60;
    // Ponerlas en game utils? por si las quiero usar desde actions
    public static final int LIMIT_TO_MSPACMAN = 20000;
    public static final int LIMIT_TO_PPILLS_PAC = 19;
    public static final int LIMIT_TO_PPILS_GHOST_C = 50; // Chase
    public static final int LIMIT_TO_PPILS_GHOST_E = 45; // Edible
    public static final int LIMIT_TO_CHASE_GHOST = 60;
}
