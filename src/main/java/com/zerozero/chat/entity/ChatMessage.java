package com.zerozero.chatmessage.entity;

import com.zerozero.chatroom.entity.ChatRoom;
import com.zerozero.common.entity.BaseEntity;
import com.zerozero.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ChatMessage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String content;

  @ManyToOne
  private User user;

  @ManyToOne
  private ChatRoom chatRoom;
}
