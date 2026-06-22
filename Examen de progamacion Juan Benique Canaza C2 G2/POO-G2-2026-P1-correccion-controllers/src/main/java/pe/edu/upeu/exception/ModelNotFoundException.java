package pe.edu.upeu.exception;

/**
 * Excepción lanzada cuando no se encuentra un recurso por su ID.
 * (paquete renombrado de 'exeption' a 'exception' para corrección ortográfica)
 */
public class ModelNotFoundException extends RuntimeException {
    public ModelNotFoundException(String message) {
        super(message);
    }
}
