package com.zerozero.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "users")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nickname;

  @OneToMany
  @JoinColumn(name = "user_id")
  private List<ChatMessage> chatMessages = new ArrayList<>();

  @OneToMany
  @JoinColumn(name = "user_id")
  private List<Store> stores = new ArrayList<>();

  @ManyToOne
  private ChatRoom chatRoom;
}
