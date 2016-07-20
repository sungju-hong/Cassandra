package org.wso2.cassandra.statistics;

import org.apache.cassandra.db.ColumnFamilyStoreMBean;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ColumnFamilyStats {
    static String ColumnFamilyStatsString = "org.apache.cassandra.db:type=ColumnFamilies,keyspace=system,columnfamily=NodeIdInfo";

    public static void main(String[] args) throws IOException {
        String serviceURL = "service:jmx:rmi:///jndi/rmi://localhost:7199/jmxrmi";
        JMXServiceURL jmxUrl = new JMXServiceURL(serviceURL);
        Map<String, String[]> authenticationInfo = new HashMap<String, String[]>();
        JMXConnector jmxCon = JMXConnectorFactory.connect(jmxUrl, authenticationInfo);
        try {
            MBeanServerConnection catalogServerConnection = jmxCon.getMBeanServerConnection();
            ColumnFamilyStoreMBean cfstatMbean = locateMBean(new ObjectName(ColumnFamilyStatsString),
                    ColumnFamilyStoreMBean.class, catalogServerConnection);
            System.out.println("Maximum Compaction Threshold >> " + cfstatMbean.getMaximumCompactionThreshold());
            System.out.println("Minimum Compaction Threshold >> " + cfstatMbean.getMinimumCompactionThreshold());
        } catch (Exception e) {
            if (jmxCon != null) {
                jmxCon.close();
            }
            e.printStackTrace();
        }
    }

    private static <T> T locateMBean(ObjectName name, Class<T> mBeanClass, MBeanServerConnection catalogServerConnection) {
        return JMX.newMBeanProxy(catalogServerConnection, name, mBeanClass);
    }
}
