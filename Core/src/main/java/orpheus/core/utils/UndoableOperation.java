package orpheus.core.utils;

/**
 * Represents something which can be done and undone
 */
public interface UndoableOperation<T> {
    
    /**
     * Does the operation on the given operand
     * @param operand the operand to do something to
     */
    public void doOperation(T operand);

    /**
     * Undoes the affect of doOperation on the given operand,
     * assuming doOperation has already been called on the operand
     * @param operand the operand to undo this operation on
     */
    public void undoOperation(T operand);
}
