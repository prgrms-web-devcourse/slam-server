package org.slams.server.notification.Exception;

import org.slams.server.common.error.exception.EntityNotFoundException;

/**
 * Created by yunyun on 2021/12/16.
 */
public class NotificationNotFoundException extends EntityNotFoundException {
    public NotificationNotFoundException(String message) {
        super(message);
    }
}
