package org.slams.server.chat.repository;

import org.slams.server.chat.entity.ChatConversationContent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by yunyun on 2021/12/18.
 */
public interface ChatConversationContentRepository extends JpaRepository<ChatConversationContent, Long> {
}
