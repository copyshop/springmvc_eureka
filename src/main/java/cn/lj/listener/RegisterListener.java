package cn.lj.listener;

import com.netflix.appinfo.*;
import com.netflix.config.ConfigurationManager;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.EurekaClientConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class RegisterListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ConfigurationManager.getConfigInstance()
                .setProperty("eureka.region", "default");
        ConfigurationManager.getConfigInstance()
                .setProperty("eureka.serviceUrl.default", "http://localhost:8761/eureka");
        EurekaClientConfig eurekaClientConfig = new DefaultEurekaClientConfig();
        EurekaInstanceConfig eurekaInstanceConfig = new MyDataCenterInstanceConfig("springmvc");

        InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder()
                .setAppName("springmvc").setVIPAddress("springmvc").setIPAddr("172.0.0.1")
                .setLeaseInfo(LeaseInfo.Builder.newBuilder().setDurationInSecs(10).build())
                .setHostName("localhost").setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn)).build();

        ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(eurekaInstanceConfig, instanceInfo);
        EurekaClient eurekaClient = new DiscoveryClient(applicationInfoManager, eurekaClientConfig);
        eurekaClient.registerHealthCheck(new HealthCheckHandler() {
            @Override
            public InstanceInfo.InstanceStatus getStatus(InstanceInfo.InstanceStatus instanceStatus) {
                return InstanceInfo.InstanceStatus.UP;
            }
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
