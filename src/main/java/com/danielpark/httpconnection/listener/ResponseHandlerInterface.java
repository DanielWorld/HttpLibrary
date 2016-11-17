package com.danielpark.httpconnection.listener;

/**
 * Interface to standardize implementations
 */
public interface ResponseHandlerInterface {

    /**
     * Returns whether the handler is asynchronous or synchronous
     *
     * @return boolean if the ResponseHandler is running in synchronous mode
     */
    boolean getUseSynchronousMode();

    /**
     * Can set, whether the handler should be asynchronous or synchronous
     *
     * @param useSynchronousMode whether data should be handled on background Thread on UI Thread
     */
    void setUseSynchronousMode(boolean useSynchronousMode);
}
