package petproject.authservice.service;

import java.util.Map;

public interface OutboxService {
    void createOutbox(String eventType, Object payload);
}
