package com.laniakea.core;

import com.laniakea.exection.KearpcException;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author luochang
 * @version MessageCallBack.java, v 0.1 2019年05月30日 12:19 luochang Exp
 */
public class MessageCallBack<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final int WAITIMES = 8 * 1000;

    private Channel channel;

    private MessageResponse response;
    private Lock            lock   = new ReentrantLock();
    private Condition       finish = lock.newCondition();

    public MessageCallBack( Channel channel){
        this.channel = channel;
    }


    public T create() throws Throwable {
        try {
            lock.lock();
            finish.await(WAITIMES, TimeUnit.MILLISECONDS);
            if (this.response != null ) {
                if(this.response.getError() == null){
                    return (T) this.response.getResult();
                }else{
                    channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                    throw new KearpcException(this.response.getError());
                }
            } else {
                logger.error("task is overtime than {} milliseconds", WAITIMES);
                throw new KearpcException("task is overtime");
            }
        } finally {
            lock.unlock();
        }
    }

    public void finish(MessageResponse reponse) {
        try {
            lock.lock();
            this.response = reponse;
            finish.signal();
        } finally {
            lock.unlock();
        }
    }
}
