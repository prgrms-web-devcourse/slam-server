package org.slams.server.alarm.dto;

/**
 * Created by yunyun on 2021/12/09.
 */
public class CursorRequest {

    private final int size;
    private final int lastId;
    private final boolean isFirst;

    public CursorRequest(int size, int lastId, boolean isFirst){
        this.size = size;
        this.lastId = lastId;
        this.isFirst = isFirst;
    }
}
