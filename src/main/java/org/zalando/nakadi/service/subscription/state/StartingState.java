package org.zalando.nakadi.service.subscription.state;

import org.zalando.nakadi.exceptions.NoStreamingSlotsAvailable;
import org.zalando.nakadi.service.subscription.model.Partition;
import org.zalando.nakadi.service.subscription.model.Session;
import java.io.IOException;

public class StartingState extends State {
    @Override
    public void onEnter() {
        getZk().runLocked(this::createSubscriptionLocked);
    }

    /**
     * 1. Checks, that subscription node is present in zk. If not - creates it.
     * <p>
     * 2. Registers session.
     * <p>
     * 3. Switches to streaming state.
     */
    private void createSubscriptionLocked() {
        // check that subscription initialized in zk.
        if (getZk().createSubscription()) {
            // if not - create subscription node etc.
            getZk().fillEmptySubscription(getKafka().getSubscriptionOffsets());
        } else {
            final Session[] sessions = getZk().listSessions();
            final Partition[] partitions = getZk().listPartitions();
            if (sessions.length >= partitions.length) {
                switchState(new CleanupState(new NoStreamingSlotsAvailable(partitions.length)));
                return;
            }
        }

        registerSession();

        try {
            getOut().onInitialized(getSessionId());
            switchState(new StreamingState());
        } catch (final IOException e) {
            getLog().error("Failed to notify of initialization. Switch to cleanup directly", e);
            switchState(new CleanupState(e));
        }
    }
}
