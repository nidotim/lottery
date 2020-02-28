package com.nidotim.lottery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nidotim.lottery.model.converter.SetToJsonConverter;
import com.nidotim.lottery.model.enums.GameStatus;
import com.nidotim.lottery.util.LotteryGenerator;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import org.springframework.util.CollectionUtils;

@Entity
@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game extends BaseEntity {

  private Instant startDate;

  private Instant endDate;

  @Enumerated(value = EnumType.STRING)
  private GameStatus status;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lottery_id", updatable = false)
  @JsonIgnore
  private Lottery lottery;

  @Exclude
  @ToString.Exclude
  @LazyCollection(LazyCollectionOption.EXTRA)
  @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JsonIgnore
  private List<Ticket> tickets = new ArrayList<>();

  @Convert(converter = SetToJsonConverter.class)
  private Set<Integer> numbers = new HashSet<>();

  @JsonIgnore
  public boolean isEnded() {
    return GameStatus.Ended.equals(getStatus());
  }

  @JsonIgnore
  public boolean isOpening() {
    return GameStatus.Opening.equals(getStatus());
  }

  @JsonIgnore
  public void open() {
    if(!isEnded()) {
      return;
    }
    LotteryGenerator generator = new LotteryGenerator(getLottery());
    Set<Integer> numbers = generator.generateNumbers();
    setNumbers(numbers);
    setStatus(GameStatus.Opened);
  }

  @JsonIgnore
  public Ticket buyTicket(User user, List<Integer> numberList) {
    Lottery lottery = getLottery();
    if(lottery == null) {
      return null;
    }
    LotteryGenerator generator = new LotteryGenerator(lottery);
    List<Number> numbers = new ArrayList<>();
    Ticket ticket = Ticket.builder()
        .game(this)
        .numberList(numbers)
        .user(user)
        .build();
    if(!CollectionUtils.isEmpty(numberList)) {
      Collections.sort(numberList);
      Number number = Number.builder()
          .numbers(numberList)
          .ticket(ticket)
          .build();
      numbers.add(number);
    }
    while(numbers.size() < lottery.getNumOfNumbersInTicket()) {
      List<Integer> generateNumberList = new ArrayList(generator.generateNumbers());
      Collections.sort(generateNumberList);
      Number number = Number.builder()
          .numbers(generateNumberList)
          .ticket(ticket)
          .build();
      numbers.add(number);
    }
    return ticket;
  }

  @JsonIgnore
  public List<Ticket> buyTickets(User user, Integer numOfTickets) {
    Lottery lottery = getLottery();
    if(lottery == null) {
      return null;
    }
    List<Ticket> tickets = new ArrayList<>();
    for(int i = 0; i < numOfTickets; i++) {
      tickets.add(buyTicket(user, null));
    }
    return tickets;
  }

}
