package org.jboss.demo.angrytweet.dashboard;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.dashboard.annotation.Priority;
import org.jboss.dashboard.annotation.Startable;
import org.jboss.dashboard.annotation.config.Config;
import org.jboss.dashboard.database.DataSourceManager;
import org.jboss.dashboard.database.JNDIDataSourceEntry;
import org.jboss.dashboard.factory.InitialModuleRegistry;
import org.jboss.dashboard.kpi.KPIInitialModule;
import org.jboss.dashboard.workspace.export.ImportWorkspacesModule;

@ApplicationScoped
public class DashboardBuilder implements Startable {
    
    @Inject
    private InitialModuleRegistry initialModuleRegistry;
    
    @Inject @Config("WEB-INF/etc/appdata/initialData/angrytweetKPIs.xml")
    private String kpiFile;
    
    @Inject @Config("WEB-INF/etc/appdata/initialData/angrytweetWorkspace.xml")
    private String workspaceFile;
    
    @Inject
    DataSourceManager dataSourceManager;

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public void start() throws Exception {
        if (dataSourceManager.getDataSourceEntry("AngryClaimDB") == null) {
            JNDIDataSourceEntry entry = new JNDIDataSourceEntry();
            entry.setName("AngryClaimDB");
            entry.setTestQuery("select * from ticket;");
            entry.setJndiPath("java:jboss/datasources/AngryTweetDS");
            entry.save();
        }
        
        KPIInitialModule kpis = new KPIInitialModule();
        kpis.setName("org.jboss.demo.angrytweet.dashboard.angrytweetKPIs");
        kpis.setImportFile(kpiFile);
        kpis.setVersion(1);
        initialModuleRegistry.registerInitialModule(kpis);
        
        ImportWorkspacesModule workspace = new ImportWorkspacesModule();
        workspace.setName("org.jboss.demo.angrytweet.dashboard.angrytweetWorkspace");
        workspace.setImportFile(workspaceFile);
        workspace.setVersion(1);
        initialModuleRegistry.registerInitialModule(workspace);
    }

}
