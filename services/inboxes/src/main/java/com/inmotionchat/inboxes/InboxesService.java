package com.inmotionchat.inboxes;

import com.inmotionchat.core.soa.InMotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InboxesService extends InMotionService {

    protected final static Logger log = LoggerFactory.getLogger(InboxesService.class);

    public InboxesService() {
        super(log);
    }

    @Override
    public String getServiceName() {
        return "InboxesService";
    }

    @Override
    public String getServiceConfigFileName() {
        return "inboxes.toml";
    }

}
