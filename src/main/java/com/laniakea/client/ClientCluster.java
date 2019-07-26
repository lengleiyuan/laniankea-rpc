package com.laniakea.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wb-lgc489196
 * @version Cluster.java, v 0.1 2019年07月26日 10:21 wb-lgc489196 Exp
 */
public class ClientCluster {


    private AddressMannager addressMannager;


    protected AtomicInteger countOfInvoke = new AtomicInteger(0);

}
