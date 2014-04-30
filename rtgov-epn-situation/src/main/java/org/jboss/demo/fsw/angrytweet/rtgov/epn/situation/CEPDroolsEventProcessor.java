package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.drools.core.ClockType;
import org.drools.core.time.SessionPseudoClock;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.internal.KnowledgeBaseFactory;
import org.overlord.rtgov.ep.EventProcessor;
import org.overlord.rtgov.internal.ep.DefaultEPContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CEPDroolsEventProcessor extends EventProcessor {
    
    private static final Logger LOG = LoggerFactory.getLogger(CEPDroolsEventProcessor.class.getName());
    
    private DefaultEPContext context=null;
    
    private KieSession session;
    
    private String ruleName;
    
    private static java.util.concurrent.atomic.AtomicInteger count=new java.util.concurrent.atomic.AtomicInteger();
    
    @Override
    public void init() throws Exception {
        context = new DefaultEPContext(getServices());
        session = createSession();
    }

    @Override
    public Serializable process(String source, Serializable e, int retriesLeft) throws Exception {
        // only handle events
        if ( !(e instanceof Event)) {
            return null;
        }
        
        Event event = (Event) e;
        
        Serializable result = null;
        
        synchronized (this) {
            
            LOG.info("Processing event '" + event + "'");
            
            context.handle(null);
            EntryPoint entryPoint = session.getEntryPoint(source);
            if (entryPoint != null) {
                //if event timestamp < clock, set timestamp to clock value
                //else advance clock to event time
                SessionPseudoClock clock = (SessionPseudoClock) session.getSessionClock();
                long clockTime = clock.getCurrentTime();
                LOG.debug("Session clock time = " + clockTime );
                long eventTime = event.getTstamp();
                LOG.debug("Event time stamp = " + eventTime);
                if (eventTime < clockTime) {
                    event.setTstamp(clockTime);
                } else {
                    clock.advanceTime(eventTime - clockTime, TimeUnit.MILLISECONDS);
                }                
                entryPoint.insert(event);
                session.fireAllRules();
            }
            
            result = (Serializable)context.getResult();

            if (result instanceof Exception) {
                throw (Exception)result;
            }
        }
        return result;
    }
    
    private KieSession createSession() throws Exception {
        KieSession session = null;
        KieBase kiebase = loadKieBase();
        
        if (kiebase != null) {
            KieSessionConfiguration sconf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
            sconf.setOption( ClockTypeOption.get( ClockType.PSEUDO_CLOCK.getId() ) );
            session = kiebase.newKieSession(sconf, null);
            session.setGlobal("epc", context);
        }
        return session;
    }
    
    private KieBase loadKieBase() throws Exception {
        String droolsRuleBase=getRuleName()+".drl";

        try {
            KieServices kieServices = KieServices.Factory.get();
            KieBaseConfiguration config = kieServices.newKieBaseConfiguration();
            config.setOption( EventProcessingOption.STREAM );
            ReleaseId releaseId = KieServices.Factory.get().newReleaseId("org.overlord.rtgov.tmp", getRuleName(),
                                    String.valueOf(count.getAndIncrement()));
            KieFileSystem kieFileSystem = kieServices.newKieFileSystem().generateAndWritePomXML(releaseId);

            kieFileSystem.write(kieServices.getResources().newClassPathResource(droolsRuleBase));

            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();
            Results results = kieBuilder.getResults();
            
            if (results.hasMessages(Message.Level.ERROR)) {
                StringBuffer buf=new StringBuffer();
                for (Message message : results.getMessages(Message.Level.ERROR)) {
                    buf.append("ERROR: "+message.toString().trim()+"\r\n");
                }
                throw new Exception(buf.toString());
            }

            KieContainer kieContainer = kieServices.newKieContainer(releaseId);
            return kieContainer.newKieBase(config);

        } catch (Throwable e) {
            String mesg=MessageFormat.format(
                    java.util.PropertyResourceBundle.getBundle(
                    "ep-drools.Messages").getString("EP-DROOLS-1"),
                    droolsRuleBase, getRuleName());
            
            LOG.error(mesg, e);
            
            throw new Exception(mesg, e);
        }        
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
