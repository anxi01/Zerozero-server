package com.zerozero.domain.user.domain;

import com.zerozero.domain.chat.domain.ChatMessage;
import com.zerozero.domain.chat.domain.ChatRoom;
import com.zerozero.domain.store.domain.Store;
import com.zerozero.global.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nickname;

  @Email
  private String email;

  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany
  @JoinColumn(name = "user_id")
  private List<ChatMessage> chatMessages = new ArrayList<>();

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private List<Store> stores = new ArrayList<>();

  @ManyToOne
  private ChatRoom chatRoom;

  public void enterChatRoom(ChatRoom chatRoom) {
    this.chatRoom = chatRoom;
  }

  public void exitChatRoom() {
    this.chatRoom = null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
