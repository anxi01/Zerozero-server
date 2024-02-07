package com.zerozero.domain.chat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zerozero.domain.user.domain.User;
import com.zerozero.global.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoom extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToMany
  @JoinColumn(name = "chat_room_id")
  @JsonIgnore
  private List<User> users = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "chat_room_id")
  @JsonIgnore
  private List<ChatMessage> chatMessages = new ArrayList<>();
}