package com.griddynamics.jagger.engine.e1.collector.invocation;

import com.griddynamics.jagger.invoker.InvocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/** Listener, executed before, after invocation
 * @author Gribov Kirill
 * @n
 * @par Details:
 * @details Possible applications for invocation listener: @n
 * @li Collect some parameters during test run and save as metrics *
 * @n
 * To view test listener implementations click here @ref Main_Listeners_group
 * @n
 * @ingroup Main_Listeners_Base_group */
public interface InvocationListener<Q, R, E>  {

    /** Method is executed before invocation starts
     * @param invocationInfo - describes start invocation information*/
    void onStart(InvocationInfo<Q, R, E> invocationInfo);

    /** Method is executed after invocation starts
     * @param invocationInfo - describes invocation result information*/
    void onSuccess(InvocationInfo<Q, R, E> invocationInfo);

    /** Method is executed when some invocation exception happens
     * @param invocationInfo - describes invocation information
     * @param e - invocation exception*/
    void onFail(InvocationInfo<Q, R, E> invocationInfo, InvocationException e);

    /** Method is executed when invocation was interrupted by some error
     * @param invocationInfo - describes invocation information
     * @param error - invocation error*/
    void onError(InvocationInfo<Q, R, E> invocationInfo, Throwable error);

    public static class Composer implements InvocationListener{
        private static Logger log = LoggerFactory.getLogger(Composer.class);

        private List<InvocationListener> listeners;

        public Composer(List<InvocationListener> listeners) {
            this.listeners = listeners;
        }

        public static InvocationListener compose(List<InvocationListener> listeners){
            return new Composer(listeners);
        }

        @Override
        public void onStart(InvocationInfo invocationInfo) {
            for (InvocationListener listener : listeners){
                try{
                    listener.onStart(invocationInfo);
                }catch (RuntimeException ex){
                    log.error("Failed to call onStart in {} listener-invocation", listener.toString(), ex);
                }
            }
        }

        @Override
        public void onSuccess(InvocationInfo invocationInfo) {
            for (InvocationListener listener : listeners){
                try{
                    listener.onSuccess(invocationInfo);
                }catch (RuntimeException ex){
                    log.error("Failed to call onSuccess in {} listener-invocation", listener.toString(), ex);
                }
            }
        }

        @Override
        public void onFail(InvocationInfo invocationInfo, InvocationException e) {
            for (InvocationListener listener : listeners){
                try{
                    listener.onFail(invocationInfo, e);
                }catch (RuntimeException ex){
                    log.error("Failed to call onFail in {} listener-invocation", listener.toString(), ex);
                }
            }
        }

        @Override
        public void onError(InvocationInfo invocationInfo, Throwable error) {
            for (InvocationListener listener : listeners){
                try{
                    listener.onError(invocationInfo, error);
                }catch (RuntimeException ex){
                    log.error("Failed to call onError in {} listener-invocation", listener.toString(), ex);
                }
            }
        }
    }
}
