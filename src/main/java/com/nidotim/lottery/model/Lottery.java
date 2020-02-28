package com.nidotim.lottery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lottery extends BaseEntity {

  @NotNull(message = "Name cannot be null")
  private String name;

  private int minNum;

  private int maxNum;

  private int numOfNumbers;

  private int numOfNumbersInTicket;

  private int leastWinningNumber;

  @Exclude
  @ToString.Exclude
  @LazyCollection(LazyCollectionOption.EXTRA)
  @OneToMany(mappedBy = "lottery", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JsonIgnore
  private Set<Game> posts = new HashSet<>();

}
