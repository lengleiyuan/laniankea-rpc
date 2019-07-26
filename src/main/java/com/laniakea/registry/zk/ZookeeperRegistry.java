package com.laniakea.registry.zk;

import com.laniakea.registry.*;
import com.laniakea.serialize.KearpcSerializeProtocol;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import static com.laniakea.config.KearpcConstants.CUTTER;
import static com.laniakea.config.KearpcConstants.ETC;
import static com.laniakea.kit.LaniakeaKit.*;

/**
 * @author luochang
 * @version ZookeeperRegistry.java, v 0.1 2019年07月04日 15:24 luochang Exp
 */
public class ZookeeperRegistry extends Registry {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private CuratorFramework  zkClient;

    private final static int CONNECT_TIMEOUT = 20000;

    private final static byte[]  PROVIDER_ONLINE  = new byte[] { 1 };

    private ProviderObserver observer;

    private CopyOnWriteArrayList<PathChildrenCache> pathChildrenCaches = new CopyOnWriteArrayList<>();

    private Map<Provider, String> providerUrls = new ConcurrentHashMap<>();

    private Map<Consumer, String> consumerUrls = new ConcurrentHashMap<>();


    @Override
    public void start(){

        if(null != zkClient){
            return;
        }

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFrameworkFactory.Builder zkClientuilder = CuratorFrameworkFactory.builder()
                .connectString(address)
                .sessionTimeoutMs(CONNECT_TIMEOUT * 3)
                .connectionTimeoutMs(CONNECT_TIMEOUT)
                .canBeReadOnly(false)
                .retryPolicy(retryPolicy)
                .defaultData(null);

        zkClient = zkClientuilder.build();

        zkClient.getConnectionStateListenable().addListener((client,state)-> {

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("reconnect to zookeeper,recover provider and consumer data");
                }
                if (state == ConnectionState.RECONNECTED) {
                    providerUrls.keySet().forEach(provider -> register(provider));
                    consumerUrls.keySet().forEach(consumer -> subscribeCreateNode(consumer));
                }
        });
        observer = new ProviderObserver();
        zkClient.start();

    }

    @Override
    public void register(Provider provider) {
        try {
            String url;
            String optionalUrl = providerUrls.get(provider);
            if(null != optionalUrl){
                url = optionalUrl;
            }else{
                String providerPath = buildProviderPath(provider.getUniqueId());
                String addressStr = provider.getHost() + ":" + provider.getPort();
                url = providerPath + CUTTER + addressStr + ETC + provider.getProtocol().name();
                providerUrls.putIfAbsent(provider,url);
            }
            try {
                zkClient.create().creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(url, PROVIDER_ONLINE);
            } catch (KeeperException.NodeExistsException nodeExistsException ) {
                LOGGER.warn("provider has exists in zookeeper, provider=" + url,nodeExistsException);
            }

        } catch (Exception e) {
            LOGGER.warn("Failed to Register provider to zookeeper",e);
        }


    }

    @Override
    public void register(ProviderGroup providerGroup) {
        providerGroup.getProviders().forEach(provider -> register(provider));
    }

    @Override
    public void unRegister(Provider provider) {
        String providerPath = buildProviderPath(provider.getUniqueId());
        String addressStr = hostport(provider.getHost(),provider.getPort());
        String url = providerPath + CUTTER + addressStr + ETC + provider.getProtocol().getStr();
        try {
            zkClient.delete().forPath(url);
        } catch (Exception e) {
            LOGGER.warn("provider to unRegister in zookeeper, provider=" + url,e);
        }

    }

    public void subscribeCreateNode(Consumer consumer){
        try {
            String url;
            String optionalUrl = consumerUrls.get(consumer);
            if (null != optionalUrl) {
                url = optionalUrl;
            } else {
                String consumerPath = buildConsumerPath(consumer.getRemoteKey());
                String addressStr = hostport(consumer.getSocketAddress().getHostName(),consumer.getSocketAddress().getPort());
                url = consumerPath + CUTTER + addressStr;
                consumerUrls.putIfAbsent(consumer, url);
            }
            try {
                zkClient.create().creatingParentContainersIfNeeded()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(url, PROVIDER_ONLINE);
            } catch (KeeperException.NodeExistsException nodeExistsException) {
                LOGGER.warn("consumer has exists in zookeeper, consumer=" + url, nodeExistsException);
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to register consumer to zookeeper!", e);
        }
    }

    @Override
    public void unRegister(ProviderGroup providerGroup) {
        providerGroup.getProviders().forEach(provider -> unRegister(provider));
    }

    @Override
    public ProviderGroup subscribe(Consumer consumer) {
        List<String> providerAddress = null;
        subscribeCreateNode(consumer);
        try {
            String providerPath = buildProviderPath(consumer.getRemoteKey());
            try {
                providerAddress = zkClient.getChildren().forPath(providerPath);
                observer.add(consumer.getNativeKey(),providerAddress);
            } catch (KeeperException.NodeExistsException nodeExistsException) {
                providerAddress = new CopyOnWriteArrayList<>();
                LOGGER.warn("consumer has notexists in zookeeper, path=" + providerPath,nodeExistsException);
            }

            PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, providerPath, true);
            pathChildrenCaches.addIfAbsent(pathChildrenCache);
            pathChildrenCache.getListenable().addListener((client,event)-> {
                String path = event.getData().getPath();
                switch (event.getType()) {
                    case CHILD_ADDED:
                        observer.add(consumer.getNativeKey(),buildEndCutter(path));
                        break;
                    case CHILD_REMOVED:
                        observer.delete(consumer.getNativeKey(),buildEndCutter(path));
                        break;
                    case CHILD_UPDATED:
                        observer.update(consumer.getNativeKey(),buildEndCutter(path));
                        break;
                    default:
                        break;
                }
            });
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            LOGGER.warn("Failed to register consumer to zookeeper!",e);
        }

        ProviderGroup providerGroup = new ProviderGroup();
        providerAddress.forEach(path -> {
            String address = buildAdress(path);
            Provider provider = new Provider(ip(address),
                    port(address),null,
                    KearpcSerializeProtocol.valueOf(buildProtocal(path)));
            providerGroup.add(provider);
        });

        return providerGroup;
    }

    @Override
    public void unSubscribe(Consumer consumer) {
        String consumerPath = buildConsumerPath(consumer.getRemoteKey());
        String addressStr = hostport(consumer.getSocketAddress().getHostName(),consumer.getSocketAddress().getPort());
        String url = consumerPath + CUTTER + addressStr;
        try {
            zkClient.delete().forPath(url);
        } catch (Exception e) {
            LOGGER.warn("provider to unRegister in zookeeper, provider=" + url,e);
        }
    }

    @Override
    public void destroy() {
        pathChildrenCaches.forEach(v->{
            try {
                v.close();
            } catch (IOException e) {
                LOGGER.warn("Close PathChildrenCache error!",e);
            }
        });
        zkClient.close();
        providerUrls.clear();
        consumerUrls.clear();
    }



}
