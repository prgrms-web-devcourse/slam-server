package org.slams.server.chat.repository;

import org.slams.server.chat.entity.ChatContents;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yunyun on 2021/12/16.
 */
public interface ChatContentsRepository extends JpaRepository<ChatContents, Long> {
}
