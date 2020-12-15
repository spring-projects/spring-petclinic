package org.springframework.samples.petclinic.exception;

/**
 * Classe des Exceptions Fonctionnelles
 */
public class FunctionalException extends Exception {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;


    // ==================== Constructeurs ====================
    /**
     * Constructeur.
     *
     * @param pMessage -
     */
    public FunctionalException(String pMessage) {
        super(pMessage);
    }

    /**
     * Constructeur.
     *
     * @param pCause -
     */
    public FunctionalException(Throwable pCause) {
        super(pCause);
    }

    /**
     * Constructeur.
     *
     * @param pMessage -
     * @param pCause -
     */
    public FunctionalException(String pMessage, Throwable pCause) {
        super(pMessage, pCause);
    }
}
