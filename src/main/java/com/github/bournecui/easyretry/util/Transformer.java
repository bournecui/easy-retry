package com.github.bournecui.easyretry.util;

/**
 * Defines a functor interface implemented by classes that transform one
 * object into another.
 * <p>
 * A <code>Transformer</code> converts the input object to the output object.
 * The input object should be left unchanged.
 * Transformers are typically used for type conversions, or extracting data
 * from an object.
 * <p>
 * Standard implementations of common transformers are provided by
 * {@link TransformerUtils}. These include method invokation, returning a constant,
 * cloning and returning the string value.
 * 
 * @since Commons Collections 1.0
 * @version $Revision: 646777 $ $Date: 2008-04-10 14:33:15 +0200 (Thu, 10 Apr 2008) $
 * 
 * @author James Strachan
 * @author Stephen Colebourne
 */
public interface Transformer {

    /**
     * Transforms the input object (leaving it unchanged) into some output object.
     *
     * @param input  the object to be transformed, should be left unchanged
     * @return a transformed object
     * @throws ClassCastException (runtime) if the input is the wrong class
     * @throws IllegalArgumentException (runtime) if the input is invalid
     * @throws FunctorException (runtime) if the transform cannot be completed
     */
    public Object transform(Object input);

}
