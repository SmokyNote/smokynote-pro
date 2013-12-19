package com.smokynote.inject;

/**
 * @author Maksim Zakharov
 * @since 1.0
 */
public interface Injector {

    /**
     * Inspect given object and provide it with required dependencies.
     * All injectable field must be annotated with {@link javax.inject.Inject} annotation
     * and declared as non-private (package level visibility is OK).
     *
     * @param target object to inject dependencies to
     */
    void inject(Object target);
}
