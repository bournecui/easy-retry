package com.github.bournecui.easyretry.util;

/**
 * Defines a functor interface implemented by classes that perform a predicate
 * test on an object.
 * <p>
 * A <code>Predicate</code> is the object equivalent of an <code>if</code> statement.
 * It uses the input object to return a true or false value, and is often used in
 * validation or filtering.
 * <p>
 * Standard implementations of common predicates are provided by
 * {@link PredicateUtils}. These include true, false, instanceof, equals, and,
 * or, not, method invokation and null testing.
 *
 * @author James Strachan
 * @author Stephen Colebourne
 * @version $Revision: 646777 $ $Date: 2008-04-10 14:33:15 +0200 (Thu, 10 Apr 2008) $
 * @since Commons Collections 1.0
 */
public interface Predicate {

    /**
     * Use the specified parameter to perform a test that returns true or false.
     *
     * @param object the object to evaluate, should not be changed
     *
     * @return true or false
     *
     * @throws ClassCastException       (runtime) if the input is the wrong class
     * @throws IllegalArgumentException (runtime) if the input is invalid
     * @throws FunctorException         (runtime) if the predicate encounters a problem
     */
    boolean evaluate(Object object);

}
