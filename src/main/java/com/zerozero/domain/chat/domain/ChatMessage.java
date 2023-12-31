package com.zerozero.domain.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zerozero.domain.user.domain.User;
import com.zerozero.global.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ChatMessage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String message;

  @ManyToOne
  @JsonIgnore
  private User user;

  @ManyToOne
  @JsonIgnore
  private ChatRoom chatRoom;

  @Builder
  public ChatMessage(String message, User user, ChatRoom chatRoom) {
    this.message = message;
    this.user = user;
    this.chatRoom = chatRoom;
  }
}
