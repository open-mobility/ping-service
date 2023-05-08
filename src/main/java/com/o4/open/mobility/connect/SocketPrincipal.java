package com.o4.open.mobility.connect;

import javax.security.auth.Subject;
import java.security.Principal;

public class SocketPrincipal implements Principal {
    private final String uniquesSessionId;
    private final String displayName;

    public SocketPrincipal(String uniqueSessionId, String displayName) {
        this.uniquesSessionId = uniqueSessionId;
        this.displayName = displayName;
    }

    /**
     * This will return unique UUID, assigned to user on creation of the session
     * @return String uniqueSessionId
     */
    @Override
    public String getName() {
        return uniquesSessionId;
    }

    /**
     * Return displayName
     * @return displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }

    @Override
    public String toString() {
        return "SocketPrincipal{" +
                "uniquesSessionId='" + uniquesSessionId + '\'' +
                ", username='" + displayName + '\'' +
                '}';
    }
}
